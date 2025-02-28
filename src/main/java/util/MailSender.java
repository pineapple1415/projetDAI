package util;

import ServiceMail.EmailService;
import ServiceMail.PdfGenerator;
import model.Commande;
import model.Client;
import model.Statut;
import java.io.File;
import java.util.Date;

public class MailSender {
    public static void main(String[] args) {
        // Création d'une commande fictive
        Commande commande = createDummyCommande();
        String pdfFilePath = "commande.pdf";

        try {
            // Génération du PDF
            File pdfFile = PdfGenerator.generateCommandePdf(commande, pdfFilePath);
            System.out.println("PDF généré à : " + pdfFile.getAbsolutePath());

            // Informations d'envoi
            String recipientEmail = "pokedex694@gmail.com"; // Remplace par une adresse de test
            String subject = "Test d'envoi d'email";
            String messageBody = "<p>Bonjour,</p><p>Ceci est un test d'envoi d'e-mail.</p>";

            // 1️⃣ Envoi d'un e-mail simple (sans pièce jointe)
            System.out.println("➡️ Envoi de l'e-mail simple...");
            EmailService.sendSimpleEmail(recipientEmail, subject, messageBody);
            System.out.println("✅ E-mail simple envoyé avec succès !");

            // 2️⃣ Envoi d'un e-mail avec le PDF en pièce jointe
            System.out.println("➡️ Envoi de l'e-mail avec pièce jointe...");
            EmailService.sendEmailWithAttachment(recipientEmail, subject, messageBody, pdfFile);
            System.out.println("✅ E-mail avec PDF envoyé avec succès !");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Création d'une commande fictive pour tester l'envoi
    private static Commande createDummyCommande() {
        Client client = new Client();
        client.setNom("Doe");
        client.setPrenom("John");
        client.setEmail("john.doe@example.com");
        client.setTelephone("0123456789");
        client.setAdresse("123 Rue Exemple");
        client.setCodePostal("75001");

        Commande commande = new Commande();
        commande.setIdCommande(1);
        commande.setClient(client);
        commande.setStatut(Statut.EN_COURS);
        commande.setDateCommande(new Date());
        commande.setPrixTotal(100.0);

        return commande;
    }
}
