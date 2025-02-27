package DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatistiqueDAO {
    private static final Logger logger = Logger.getLogger(StatistiqueDAO.class.getName());
    /**
     * 获取不同购买次数的客户占比
     */
    public List<Object[]> getCustomerPurchaseDistribution() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 计算每个用户的购买次数并分类
            List<Object[]> userOrders = session.createQuery(
                            "SELECT c.client.idUser, " +
                                    "CASE " +
                                    "   WHEN COUNT(c.idCommande) = 1 THEN '1 achat' " +
                                    "   WHEN COUNT(c.idCommande) BETWEEN 2 AND 5 THEN '2-5 achats' " +
                                    "   WHEN COUNT(c.idCommande) BETWEEN 6 AND 10 THEN '6-10 achats' " +
                                    "   ELSE '10+ achats' " +
                                    "END " +
                                    "FROM Commande c " +
                                    "GROUP BY c.client.idUser", Object[].class)
                    .list();

            // 转换成 Map<分类, 用户数>
            Map<String, Long> categoryCounts = userOrders.stream()
                    .collect(Collectors.groupingBy(row -> (String) row[1], Collectors.counting()));

            // 转换成 List<Object[]> 格式返回
            return categoryCounts.entrySet().stream()
                    .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                    .collect(Collectors.toList());
        }
    }

    /**
     * 计算产品的真实销售额（包含折扣）
     */
    public List<Object[]> getSalesPerProductFromOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p.nomProduit, SUM(c.quantite * p.prixUnit) " +
                                    "FROM Composer c " +
                                    "JOIN c.produit p " +
                                    "GROUP BY p.nomProduit " +
                                    "ORDER BY SUM(c.quantite * p.prixUnit) DESC", Object[].class)
                    .list();
        }
    }



    /**
     * 获取产品的库存情况
     */
    public List<Object[]> getStockPerProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p.nomProduit, SUM(s.nbStock) " +
                                    "FROM Stocker s " +
                                    "JOIN s.produit p " +
                                    "GROUP BY p.nomProduit " +
                                    "ORDER BY SUM(s.nbStock) DESC", Object[].class)
                    .list();
        }
    }



    /**
     * 获取某Rayon类别的销售占比
     */
    public List<Object[]> getSalesPerCategoryInRayon(int rayonId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT cat.nomCategorie, SUM(co.quantite * p.prixUnit) " +
                                    "FROM Composer co " +
                                    "JOIN co.produit p " +
                                    "JOIN p.categorie cat " +
                                    "WHERE cat.rayon.idRayon = :rayonId " +
                                    "GROUP BY cat.nomCategorie " +
                                    "ORDER BY SUM(co.quantite * p.prixUnit) DESC", Object[].class)
                    .setParameter("rayonId", rayonId)
                    .list();
        }
    }


    /**
     * 获取 Rayon 的销售占比
     */
    public List<Object[]> getSalesPerRayon() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT r.nomRayon, SUM(c.prixTotal) " +
                                    "FROM Commande c " +
                                    "JOIN Composer co ON c.idCommande = co.id.idCommande " +
                                    "JOIN Produit p ON co.id.idProduit = p.idProduit " +
                                    "JOIN Categorie cat ON p.categorie.idCategorie = cat.idCategorie " +
                                    "JOIN Rayon r ON cat.rayon.idRayon = r.idRayon " +
                                    "WHERE c.statut = 'PAYEE' OR c.statut = 'PRETE' " +
                                    "GROUP BY r.nomRayon " +
                                    "ORDER BY SUM(c.prixTotal) DESC", Object[].class)
                    .list();
        }
    }



    /**
     * 计算消费者月度购物时间统计
     */
    public List<Object[]> getMonthlyShoppingTimeStats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT MONTH(c.dateCommande), " +
                                    "ROUND(AVG(TIMESTAMPDIFF(MINUTE, c.dateAjoutPanier, c.dateCommande)), 2) " +
                                    "FROM Commande c " +
                                    "WHERE c.dateAjoutPanier IS NOT NULL AND c.dateCommande IS NOT NULL " +
                                    "GROUP BY MONTH(c.dateCommande) " +
                                    "ORDER BY MONTH(c.dateCommande)", Object[].class)
                    .list();
        }
    }

    /**
     * 计算月度订单准备时间统计
     */
    public List<Object[]> getMonthlyPreparationTimeStats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT MONTH(c.dateCommande), " +
                                    "ROUND(AVG(TIMESTAMPDIFF(MINUTE, c.dateCommande, c.finirPrepa)), 2) " +
                                    "FROM Commande c " +
                                    "WHERE c.dateCommande IS NOT NULL AND c.finirPrepa IS NOT NULL " +
                                    "GROUP BY MONTH(c.dateCommande) " +
                                    "ORDER BY MONTH(c.dateCommande)", Object[].class)
                    .list();
        }
    }


    public List<Object[]> getStockForNext7Days() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p.nomProduit, " +
                                    "COALESCE(SUM(s.nbStock), 0) AS stock_actuel, " +  // 当前库存

                                    // 预计销量 = 过去 7 天销量 + 未来 7 天订单
                                    "(COALESCE(SUM(CASE " +
                                    "    WHEN cmd.dateCommande BETWEEN CURRENT_DATE - 7 AND CURRENT_DATE " +
                                    "    THEN c.quantite ELSE 0 " +
                                    "END), 0) + " +
                                    "COALESCE(SUM(CASE " +
                                    "    WHEN cmd.dateCommande BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 " +
                                    "    THEN c.quantite ELSE 0 " +
                                    "END), 0)) AS ventes_estimees, " +

                                    // 预计库存 = 当前库存 - 过去 7 天销量 - 未来 7 天订单
                                    "(COALESCE(SUM(s.nbStock), 0) - " +
                                    " COALESCE(SUM(CASE " +
                                    "    WHEN cmd.dateCommande BETWEEN CURRENT_DATE - 7 AND CURRENT_DATE " +
                                    "    THEN c.quantite ELSE 0 " +
                                    "END), 0) - " +
                                    " COALESCE(SUM(CASE " +
                                    "    WHEN cmd.dateCommande BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 " +
                                    "    THEN c.quantite ELSE 0 " +
                                    "END), 0)) AS stock_prevu " +

                                    "FROM Produit p " +
                                    "LEFT JOIN Stocker s ON p.idProduit = s.produit.idProduit " +
                                    "LEFT JOIN Composer c ON p.idProduit = c.produit.idProduit " +
                                    "LEFT JOIN Commande cmd ON c.commande.idCommande = cmd.idCommande " +
                                    "GROUP BY p.nomProduit " +
                                    "ORDER BY (COALESCE(SUM(s.nbStock), 0) - " +
                                    "          COALESCE(SUM(CASE " +
                                    "              WHEN cmd.dateCommande BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 " + // 只影响排序的部分也要改
                                    "              THEN c.quantite ELSE 0 " +
                                    "          END), 0)) ASC",
                            Object[].class)
                    .list();
        }
    }

}
