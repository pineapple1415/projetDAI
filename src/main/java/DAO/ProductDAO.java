package DAO;

import model.Produit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Set;

public class ProductDAO {
    public List<Produit> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit ", Produit.class).list();
        }
    }

    public List<Object[]> getFilteredProducts(List<String> categories, List<String> rayons) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p.imageUrl ,p.nomProduit, p.prixUnit FROM Produit p " +
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
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public List<Produit> getProductsByIds(Set<Long> ids) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produit WHERE idProduit IN :ids", Produit.class)
                    .setParameterList("ids", ids)
                    .list();
        }
    }

}