package util;

import DAO.CommandeDAO;
import model.Commande;
import model.Magasin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;

public class HibernateMain {
    public static void main(String[] args) {
        // 获取 Hibernate Session
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println("Hibernate a déjà connecté avec la base de données et toutes les tables sont crées");

        // Création d'une instance de CommandeDAO
        CommandeDAO commandeDAO = new CommandeDAO();

//        // US 5.2 : Récupérer et afficher la liste des commandes à préparer
//        System.out.println("\n📌 Liste des commandes à préparer (statut PAYEE) :");
//        List<Commande> commandesAPreparer = commandeDAO.getCommandesAPreparer();
//
//        if (commandesAPreparer.isEmpty()) {
//            System.out.println("❌ Aucune commande à préparer.");
//        } else {
//            for (Commande commande : commandesAPreparer) {
//                System.out.println("🛒 Commande ID: " + commande.getIdCommande() +
//                        ", Date: " + commande.getDateCommande() +
//                        ", Statut: " + commande.getStatut() +
//                        ", PrixTotal: " + commande.getPrixTotal() +
//                        ", Magasin: " + (commande.getMagasin() != null ? commande.getMagasin().getNomMagasin() : "Non renseigné"));
//            }
//        }

        // Test de la méthode getCommandeWithDetails
        int idCommandeTest = 1; // Remplacez par l'ID de la commande que vous souhaitez tester
        Commande commandeAvecDetails = commandeDAO.getCommandeWithDetails(idCommandeTest);
        if (commandeAvecDetails != null) {
            System.out.println("Commande ID: " + commandeAvecDetails.getIdCommande());
            System.out.println("Date: " + commandeAvecDetails.getDateCommande());
            System.out.println("Statut: " + commandeAvecDetails.getStatut());
            System.out.println("PrixTotal: " + commandeAvecDetails.getPrixTotal());
            System.out.println("Produits:");
            commandeAvecDetails.getComposers().forEach(composer -> {
                System.out.println("Produit: " + composer.getProduit().getNomProduit() +
                        ", Quantité: " + composer.getQuantite() +
                        ", Prix: " + composer.getProduit().getPrixUnit());
            });
        } else {
            System.out.println("Aucune commande trouvée avec l'ID: " + idCommandeTest);
        }
        session.close();
        sessionFactory.close();
    }
}