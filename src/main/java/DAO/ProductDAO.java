package DAO;

import model.Produit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ProductDAO {
    public List<Produit> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from produit ", Produit.class).list();
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





}