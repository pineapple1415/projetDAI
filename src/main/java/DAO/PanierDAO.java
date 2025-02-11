package DAO;

import model.panier;
import model.produit;
import model.User;
import model.produitDansPanier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class PanierDAO {
    public void addItemToPanier(User user, Long produitId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // 获取商品（只加载必要字段）
            produit produit = session.createQuery(
                            "SELECT new Produit(p.id, p.name, p.price) " + "FROM Produit p WHERE p.id = :id", produit.class).setParameter("id", produitId).uniqueResult();
            // 获取或创建购物车
            panier panier = session.createQuery(
                            "FROM Panier WHERE user = :user", panier.class)
                    .setParameter("user", user)
                    .uniqueResultOptional()
                    .orElseGet(() -> {
                        panier newPanier = new panier();
                        newPanier.setUser(user);
                        session.persist(newPanier);
                        return newPanier;
                    });

            // 添加购物车项
            produitDansPanier item = new produitDansPanier();
            item.setPanier(panier);
            item.setProduit(produit);
            session.persist(item);

            tx.commit();
        }
    }
}