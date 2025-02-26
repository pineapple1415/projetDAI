package DAO;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.ArrayList;
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

    public List<Map<String, Object>> getProduitsWithQuantity(int idCourse) {
        List<Map<String, Object>> produits = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT p.idProduit, p.nomProduit, p.imageUrl, a.nombre " +
                            "FROM Ajouter a " +
                            "JOIN a.produit p " +
                            "WHERE a.id.idCourse = :idCourse", Object[].class);
            query.setParameter("idCourse", idCourse);
            List<Object[]> results = query.list();

            for (Object[] row : results) {
                Map<String, Object> produitData = new HashMap<>();
                produitData.put("idProduit", row[0]);
                produitData.put("nom", row[1]);  // Produit 名称
                produitData.put("imageUrl", row[2]);  // Produit 图片
                produitData.put("nombre", row[3]);  // Produit 数量
                produits.add(produitData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void createCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public boolean updateCourseTexte(int idCourse, String newTexte) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, idCourse);
            if (course != null) {
                course.setTexte(newTexte);
                session.update(course);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }



    public void deleteCourse(int idCourse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, idCourse);
            if (course != null) {
                session.delete(course);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public boolean updateProduitQuantity(int courseId, int produitId, int newQty) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Ajouter ajouter = session.get(Ajouter.class, new AjouterId(courseId, produitId));
            if (ajouter != null) {
                ajouter.setNombre(newQty);
                session.update(ajouter);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeProduitFromCourse(int idCourse, int idProduit) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<?> query = session.createQuery(
                    "DELETE FROM Ajouter WHERE id.idCourse = :idCourse AND id.idProduit = :idProduit"
            );
            query.setParameter("idCourse", idCourse);
            query.setParameter("idProduit", idProduit);

            int result = query.executeUpdate();
            transaction.commit();

            return result > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }


}

