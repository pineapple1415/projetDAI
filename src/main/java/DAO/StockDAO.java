package DAO;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class StockDAO {

    public List<Magasin> getAllMagasins() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Magasin> magasins = session.createQuery("FROM Magasin", Magasin.class).list();

            // ✅ 调试信息
            System.out.println("Magasins trouvés: " + magasins.size());
            for (Magasin m : magasins) {
                System.out.println("Magasin ID: " + m.getIdMagasin() + " | Nom: " + m.getNomMagasin());
            }

            return magasins;
        }
    }



    public List<Rayon> getAllRayons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rayon", Rayon.class).list();
        }
    }

    public List<Categorie> getCategoriesByRayon(int rayonId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Categorie c WHERE c.rayon.idRayon = :rayonId", Categorie.class)
                    .setParameter("rayonId", rayonId)
                    .list();
        }
    }


    public List<Produit> getProduitsByCategorie(int categorieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit WHERE categorie.idCategorie = :categorieId", Produit.class)
                    .setParameter("categorieId", categorieId)
                    .list();
        }
    }

    public void ajouterStock(int magasinId, int produitId, int quantite) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 🔹 查找现有库存记录
            Stocker stock = session.createQuery(
                            "FROM Stocker s WHERE s.magasin.idMagasin = :magasinId AND s.produit.idProduit = :produitId",
                            Stocker.class)
                    .setParameter("magasinId", magasinId)
                    .setParameter("produitId", produitId)
                    .uniqueResult();

            if (stock != null) {
                // 如果存在，更新库存数量
                stock.setNbStock(stock.getNbStock() + quantite);
                session.update(stock);
            } else {
                // 如果不存在，创建新的库存记录
                Produit produit = session.get(Produit.class, produitId);
                Magasin magasin = session.get(Magasin.class, magasinId);
                stock = new Stocker(produit, magasin, quantite);
                session.save(stock);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}


