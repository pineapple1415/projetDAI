package DAO;

import model.Categorie;
import model.Produit;
import model.Rayon;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class FiltrerDAO {
    public List<Categorie> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Categorie ", Categorie.class).list();
        }
    }

    public List<Rayon> getAllRayon() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Rayon ", Rayon.class).list();
        }
    }
}
