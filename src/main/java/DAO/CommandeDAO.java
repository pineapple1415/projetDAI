package DAO;

import model.*;
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

    public void createCommande(Commande commande, Map<Long, Integer> panierMap) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // 1. 先持久化Commande并立即获取ID
            session.persist(commande);
            session.flush(); // 强制生成ID

            // 2. 创建关联的Composer
            for (Map.Entry<Long, Integer> entry : panierMap.entrySet()) {
                Produit produit = session.get(Produit.class, entry.getKey().intValue());

                // 3. 显式构建复合主键
                ComposerId composerId = new ComposerId();
                composerId.setIdCommande(commande.getIdCommande());
                composerId.setIdProduit(produit.getIdProduit());

                Composer composer = new Composer();
                composer.setId(composerId); // 关键：必须手动设置ID
                composer.setCommande(commande);
                composer.setProduit(produit);
                composer.setQuantite(entry.getValue());

                session.persist(composer);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("订单保存失败", e);
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

    public void updateCommande(Commande commande) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.merge(commande);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    /**
     * US 5.2 – Récupérer la liste des commandes à préparer par ordre de priorité.
     * Ici, on considère que les commandes avec le statut PAYEE doivent être préparées.
     * Les commandes sont triées par date (la plus ancienne en premier).
     */
    public List<Commande> getCommandesAPreparer() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Commande c JOIN FETCH c.magasin WHERE c.statut = :status ORDER BY c.dateCommande ASC",
                            Commande.class)
                    .setParameter("status", Statut.PAYEE)
                    .list();
        }
    }



    public List<Commande> getCommandesEnCours() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Commande c JOIN FETCH c.magasin WHERE c.statut = :status ORDER BY c.dateCommande ASC",
                            Commande.class)
                    .setParameter("status", Statut.EN_COURS)
                    .list();
        }
    }

    public List<Commande> getCommandesPretes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Commande c JOIN FETCH c.magasin WHERE c.statut = :status ORDER BY c.dateCommande ASC",
                            Commande.class)
                    .setParameter("status", Statut.PRETE)
                    .list();
        }
    }




    /**
     * US 5.1 – Consulter une commande à préparer pour un retrait.
     * Récupère une commande à partir de son identifiant.
     */
    public Commande getCommandeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Commande.class, id);
        }
    }

    /**
     * Méthode générique pour mettre à jour le statut d'une commande.
     */
    public void updateCommandeStatut(int idCommande, Statut nouveauStatut) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Commande commande = session.get(Commande.class, idCommande);
            if (commande != null) {
                commande.setStatut(nouveauStatut);
                session.update(commande);
            } else {
                throw new RuntimeException("Commande non trouvée pour l'id : " + idCommande);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erreur lors de la mise à jour de la commande", e);
        }
    }

    /**
     * US 5.3 – Marquer une commande en préparation pour un retrait.
     * Change le statut de la commande à EN_PREPARATION.
     */
    public void marquerCommandeEnPreparation(int idCommande) {
        updateCommandeStatut(idCommande, Statut.EN_COURS);
    }

    /**
     * US 5.4 – Finaliser la préparation d'une commande.
     * Change le statut de la commande à PRETE.
     */
    public void finaliserCommande(int idCommande) {
        updateCommandeStatut(idCommande, Statut.PRETE);
    }

    public Commande getCommandeWithDetails(int idCommande) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Commande c " +
                                    "LEFT JOIN FETCH c.composers comp " +
                                    "LEFT JOIN FETCH comp.produit " +
                                    "LEFT JOIN FETCH c.client " +
                                    "WHERE c.idCommande = :id", Commande.class)
                    .setParameter("id", idCommande)
                    .uniqueResult();
        }
    }

}
