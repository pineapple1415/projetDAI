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

}
