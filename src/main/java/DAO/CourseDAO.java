package DAO;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class CourseDAO {
    public List<Course> getCoursesByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT c FROM Course c " +
                                    "LEFT JOIN FETCH c.ajouts a " +
                                    "LEFT JOIN FETCH a.produit " +
                                    "WHERE c.client = :user", Course.class)
                    .setParameter("user", user)
                    .list();
        }
    }
}
