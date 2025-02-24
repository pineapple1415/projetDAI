package DAO;

import model.Commande;
import model.Composer;
import model.Magasin;
import model.Produit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;
import java.util.Map;

public class CommandeDAO {
    public List<Commande> getCommandesPayees(Magasin magasin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Commande WHERE statut = 'PAYEE' AND magasin = :magasin", Commande.class)
                    .setParameter("magasin", magasin)
                    .list();
        }
    }

    public Commande getCommandeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Commande.class, id);
        }
    }

    public void createCommande(Commande commande, Map<Long, Integer> panierMap) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // 保存主订单
            session.persist(commande);

            // 保存关联商品
            for (Map.Entry<Long, Integer> entry : panierMap.entrySet()) {
                Produit produit = session.get(Produit.class, entry.getKey());

                Composer composer = new Composer();
                composer.setCommande(commande);
                composer.setProduit(produit);
                composer.setQuantite(entry.getValue());

                session.persist(composer);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Commande> getCommandesByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Commande WHERE client.idUser = :userId ORDER BY dateCommande DESC",
                    Commande.class
            ).setParameter("userId", userId).list();
        }
    }


}
