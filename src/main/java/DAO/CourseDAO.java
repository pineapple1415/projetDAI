package DAO;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, String> getProduitsByUserAndCourse(int idCourse) {
        Map<String, String> produitsMap = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT p.idProduit, p.nomProduit, p.imageUrl " +
                            "FROM Ajouter a " +
                            "JOIN a.produit p " +
                            "WHERE a.id.idCourse = :idCourse ", Object[].class);

            query.setParameter("idCourse", idCourse);

            List<Object[]> results = query.list();

            for (Object[] row : results) {
                Integer idProduit = (Integer) row[0];
                String produitNom = (String) row[1];
                String produitImage = (String) row[2];

                // 只存储 produitId 和 produitImage
                produitsMap.put(produitNom, produitImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produitsMap;
    }
}

