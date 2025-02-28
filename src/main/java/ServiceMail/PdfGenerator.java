package ServiceMail;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.Commande;
import model.Client;
import model.Composer;
import model.Produit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class PdfGenerator {
    public static File generateCommandePdf(Commande commande, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filePath)));
        document.open();

        // Titre principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Détails de la commande #" + commande.getIdCommande(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Informations Client
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph clientTitle = new Paragraph("Informations Client", sectionFont);
        document.add(clientTitle);
        document.add(Chunk.NEWLINE);

        // On suppose que le client est une instance de Client (qui étend User)
        Client client = (Client) commande.getClient();
        document.add(new Paragraph("Nom complet : " + client.getNom() + " " + client.getPrenom()));
        document.add(new Paragraph("Email : " + client.getEmail()));
        document.add(new Paragraph("Téléphone : " + client.getTelephone()));
        document.add(new Paragraph("Adresse : " + client.getAdresse()));
        document.add(new Paragraph("Code postal : " + client.getCodePostal()));
        document.add(Chunk.NEWLINE);

        // Informations Commande
        Paragraph commandeTitle = new Paragraph("Informations Commande", sectionFont);
        document.add(commandeTitle);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Statut : " + commande.getStatut()));
        document.add(new Paragraph("Date de commande : " + commande.getDateCommande()));
        if (commande.getMagasin() != null) {
            document.add(new Paragraph("Magasin : " + commande.getMagasin().getNomMagasin() + " - " + commande.getMagasin().getAdresseMagasin()));
        }

        document.add(new Paragraph("Prix Total : " + commande.getPrixTotal() + " €"));
        document.add(Chunk.NEWLINE);

        // Produits commandés
        Paragraph produitsTitle = new Paragraph("Produits commandés", sectionFont);
        document.add(produitsTitle);
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        table.addCell(new PdfPCell(new Phrase("Nom du Produit", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Quantité", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Prix Unitaire (€)", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Total (€)", headerFont)));

        double totalCommande = 0;
        Set<Composer> produitsCommande = commande.getComposers();
        if (produitsCommande != null && !produitsCommande.isEmpty()) {
            for (Composer composer : produitsCommande) {
                Produit produit = composer.getProduit();
                int quantite = composer.getQuantite();
                double prixUnitaire = produit.getPrixUnit();
                double totalLigne = quantite * prixUnitaire;
                totalCommande += totalLigne;
                table.addCell(produit.getNomProduit());
                table.addCell(String.valueOf(quantite));
                table.addCell(String.format("%.2f €", prixUnitaire));
                table.addCell(String.format("%.2f €", totalLigne));
            }
        } else {
            PdfPCell cell = new PdfPCell(new Phrase("Aucun produit commandé"));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        document.add(table);

        Paragraph totalParagraph = new Paragraph("Total de la commande : " + String.format("%.2f €", totalCommande), headerFont);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.close();
        return new File(filePath);
    }
}
