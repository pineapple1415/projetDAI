package DAO;

import model.Produit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDAO {
    public List<Produit> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit ", Produit.class).list();
        }
    }

    public List<Object[]> getFilteredProducts(List<String> categories, List<String> rayons) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p.imageUrl ,p.nomProduit, p.prixUnit,p.promotion FROM Produit p " +
                    "JOIN p.categorie c " +
                    "WHERE 1=1";

            if (!categories.isEmpty()) {
                hql += " AND c.nomCategorie IN (:categories)";
            }
            if (!rayons.isEmpty()) {
                hql += " AND r.nomRayon IN (:rayons)";
            }

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            if (!categories.isEmpty()) {
                query.setParameterList("categories", categories);
            }
            if (!rayons.isEmpty()) {
                query.setParameterList("rayons", rayons);
            }

            return query.list();
        }
    }

    public Produit getProduitByNom(String nomProduit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit WHERE nomProduit = :nomProduit", Produit.class)
                    .setParameter("nomProduit", nomProduit)
                    .uniqueResult();
        }
    }

    public List<Produit> searchProducts(String searchQuery) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produit WHERE LOWER(nomProduit) LIKE LOWER(:searchQuery)", Produit.class)
                    .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
                    .list();
        }
    }



    public void addProduit(Produit product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(product);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }


    public List<Produit> getProductsByIds(Set<Long> ids) {
        Set<Integer> intIds = ids.stream()
                .map(Long::intValue) // 转换 Long -> Integer
                .collect(Collectors.toSet());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit WHERE idProduit IN :ids", Produit.class)
                    .setParameterList("ids", intIds) // 传入 Integer 类型的 id 集合
                    .list();
        }
    }


    public Produit getProduitById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        }
    }



    // ProductDAO.java
    public boolean isProduitAvailable(int produitId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long totalStock = session.createQuery(
                            "SELECT SUM(s.nbStock) FROM Stocker s " +
                                    "WHERE s.produit.idProduit = :produitId", Long.class)
                    .setParameter("produitId", produitId)
                    .uniqueResult();
            return totalStock != null && totalStock > 0;
        }
    }


    // ProductDAO.java 新增方法
    public Map<Integer, Boolean> checkStockStatus(Set<Integer> produitIds) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                            "SELECT s.produit.idProduit, SUM(s.nbStock) " +
                                    "FROM Stocker s " +
                                    "WHERE s.produit.idProduit IN :produitIds " +
                                    "GROUP BY s.produit.idProduit", Object[].class)
                    .setParameterList("produitIds", produitIds)
                    .list();

            Map<Integer, Boolean> stockStatus = new HashMap<>();
            for (Object[] result : results) {
                int produitId = (Integer) result[0];
                long totalStock = (Long) result[1];
                stockStatus.put(produitId, totalStock > 0);
            }
            // 处理未找到库存记录的商品
            produitIds.forEach(id -> stockStatus.putIfAbsent(id, false));
            return stockStatus;
        }
    }


}