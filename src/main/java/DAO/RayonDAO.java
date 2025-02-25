package DAO;

import model.Rayon;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class RayonDAO {

    public void saveRayon(Rayon rayon) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(rayon);
            tx.commit();
        }
    }

    public Rayon getRayonById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Rayon.class, id);
        }
    }

    public List<Rayon> getAllRayons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rayon", Rayon.class).list();
        }
    }

    public Rayon getRayonByName(String nomRayon) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rayon WHERE nomRayon = :nom", Rayon.class)
                    .setParameter("nom", nomRayon)
                    .uniqueResult();
        }
    }
}
