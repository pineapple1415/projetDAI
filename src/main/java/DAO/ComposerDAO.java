package DAO;

import model.Composer;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class ComposerDAO {
    public List<Composer> getProduitsByCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Composer WHERE commande.idCommande = :commandeId", Composer.class)
                    .setParameter("commandeId", commandeId)
                    .list();
        }
    }
}
