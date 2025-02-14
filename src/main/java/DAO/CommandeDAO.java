package DAO;

import model.Commande;
import model.Magasin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

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
}
