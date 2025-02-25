package DAO;

import model.Fournisseur;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class FournisseurDAO {

    public void saveFournisseur(Fournisseur fournisseur) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(fournisseur);
            tx.commit();
        }
    }

    public Fournisseur getFournisseurById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Fournisseur.class, id);
        }
    }

    public List<Fournisseur> getAllFournisseurs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Fournisseur", Fournisseur.class).list();
        }
    }
}
