package DAO;

import model.Categorie;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class CategorieDAO {

    public void saveCategorie(Categorie categorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(categorie);
            tx.commit();
        }
    }

    public Categorie getCategorieById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Categorie.class, id);
        }
    }

    public List<Categorie> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Categorie", Categorie.class).list();
        }
    }

    public Categorie getCategorieByName(String nomCategorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Categorie WHERE nomCategorie = :nom", Categorie.class)
                    .setParameter("nom", nomCategorie)
                    .uniqueResult();
        }
    }
}
