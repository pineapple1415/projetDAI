package DAO;

import model.Categorie;
import model.Produit;
import model.Rayon;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class FiltrerDAO {

    // ✅ 获取所有类别（Catégories）
    public List<String> getNameCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c.nomCategorie FROM Categorie c", String.class).list();
        }
    }

    // ✅ 获取所有大类（Rayons）
    public List<String> getNameRayon() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT r.nomRayon FROM Rayon r", String.class).list();
        }
    }

    // ✅ 获取某个 Rayon 下面的所有 Catégories
    public List<String> getCategoriesByRayon(String rayonName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c.nomCategorie FROM Categorie c WHERE c.rayon.nomRayon = :rayonName", String.class)
                    .setParameter("rayonName", rayonName)
                    .list();
        }
    }


    public List<Produit> filterProductsByRayonAndCategories(List<String> categories) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Produit p WHERE 1=1";

            if (categories != null && !categories.isEmpty()) {
                hql += " AND p.categorie.nomCategorie IN (:categories)";
            }

            System.out.println("🔍 [DAO] Requête HQL: " + hql);

            Query<Produit> query = session.createQuery(hql, Produit.class);

            if (categories != null && !categories.isEmpty()) {
                query.setParameterList("categories", categories);
            }

            List<Produit> result = query.list();

            // ✅ 避免 `LazyInitializationException`
            for (Produit produit : result) {
                produit.setFournisseur(null);
                produit.setComposers(null);
                produit.setAjouts(null);
                produit.setStockers(null);
            }

            System.out.println("📦 [DAO] Produits trouvés dans DAO: " + result.size());
            return result;
        } catch (Exception e) {
            System.err.println("❌ [DAO] Erreur lors de l'exécution de la requête:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
