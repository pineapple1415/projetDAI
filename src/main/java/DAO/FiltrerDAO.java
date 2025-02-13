package DAO;

import model.Categorie;
import model.Produit;
import model.Rayon;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class FiltrerDAO {
    public List<String> getNameCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select c.nomCategorie from Categorie c", String.class).list();
        }
    }

    public List<String> getNameRayon() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select r.nomRayon from Rayon r", String.class).list();
        }
    }



}
