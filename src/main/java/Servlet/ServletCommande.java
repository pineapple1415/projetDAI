package Servlet;

import DAO.CommandeDAO;
import ServiceMail.EmailService;
import ServiceMail.PdfGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Commande;
import model.Magasin;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/commande")
public class ServletCommande extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'action demandée par l'utilisateur
        String act = request.getParameter("action");

        if ("envoyerMail".equals(act)) {
            int idCommande = Integer.parseInt(request.getParameter("id"));
            Commande commande = commandeDAO.getCommandeByIds(idCommande);
            if (commande != null && commande.getClient() != null) {
                try {
                    String recipientEmail = commande.getClient().getEmail();
                    String subject = "Votre commande #" + idCommande + " est prête !";
                    String messageBody = buildEmailBody(commande);


                    // 1️⃣ Envoi d'un e-mail simple (sans pièce jointe)
                    System.out.println("➡️ Envoi de l'e-mail simple...");
                    EmailService.sendSimpleEmail(recipientEmail, subject, messageBody);
                    System.out.println("✅ E-mail simple envoyé avec succès !");


                    request.setAttribute("message", "Email envoyé avec le PDF à " + recipientEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("message", "Erreur: " + e.getMessage());
                }
            }
            request.getRequestDispatcher("jsp/confirmationEmail.jsp").forward(request, response);
        } else if ("consulter".equals(act)) {
            // US 5.1 – Consulter une commande à préparer pour un retrait
            try {
                int idCommande = Integer.parseInt(request.getParameter("id"));
                Commande commande = commandeDAO.getCommandeWithDetails(idCommande);
                request.setAttribute("commande", commande);
                request.getRequestDispatcher("jsp/commandeDetail.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la consultation de la commande: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("listePreparations".equals(act)) {
            // US 5.2 – Consulter la liste des commandes à préparer par ordre de priorité
            try {
                Magasin magasin = (Magasin) request.getSession().getAttribute("magasin");
                List<Commande> commandes = commandeDAO.getCommandesAPreparer();
                request.setAttribute("commandes", commandes);
                request.getRequestDispatcher("jsp/listeCommandes.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("listePreparationsPrioritaire".equals(act)) {
            // US 5.2 – Consulter la liste des commandes en cours par ordre de priorité
            try {
                Magasin magasin = (Magasin) request.getSession().getAttribute("magasin");
                List<Commande> commandes = commandeDAO.getCommandesEnCours();
                request.setAttribute("commandes", commandes);
                request.getRequestDispatcher("jsp/listeCommandesPrioritaire.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("listePrioritairePrete".equals(act)) {
            // US 5.2 – Consulter la liste des commandes prêtes par ordre de priorité
            try {
                Magasin magasin = (Magasin) request.getSession().getAttribute("magasin");
                List<Commande> commandes = commandeDAO.getCommandesPretes();
                request.setAttribute("commandes", commandes);
                request.getRequestDispatcher("jsp/listeCommandesPretes.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("retour".equals(act)) {
            // Action "retour" pour revenir à la page d'accueil
            request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
        } else if ("login".equals(act)) {
            // Action "login" pour afficher la page de connexion
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        } else {
            // Action non reconnue
            request.setAttribute("msgerreur", "Action non reconnue: " + act);
            request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
        }
    }

    private String buildEmailBody(Commande commande) { // Remplacer User par Commande
        User client = commande.getClient();
        return "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "  <h2 style='color: #2c3e50;'>Bonjour  Mr" + client.getNom() + " " + client.getPrenom() + ",</h2>"
                + "  <p>Votre commande est prête pour retrait dans notre magasin.</p>"
                + "  <p>Ci-joint le lien pour choisir un creneau de retrait.</p>"
                + "  <p>Lien de retrait: <a href='http://localhost:8080/ProjetDAI_war/creneau?idCommande=" + commande.getIdCommande() + "'>Cliquez ici</a></p>"
                + "  <p>Voici en pièce-jointe les détails de la commande.</p>"
                + "  <p>Cordialement,<br/>L'équipe Magasin</p>"
                + "</body>"
                + "</html>";
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'action postée par l'utilisateur
        String act = request.getParameter("action");
        if (act == null) {
            doGet(request, response);
            return;
        }

        if ("marquerPreparation".equals(act)) {
            // US 5.3 – Marquer une commande en préparation pour un retrait
            try {
                int idCommande = Integer.parseInt(request.getParameter("idCommande"));
                commandeDAO.marquerCommandeEnPreparation(idCommande);
                response.sendRedirect("commande?action=listePreparationsPrioritaire");
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la mise à jour de la commande: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("finaliserPreparation".equals(act)) {
            // US 5.4 – Finaliser la préparation d'une commande
            try {
                int idCommande = Integer.parseInt(request.getParameter("idCommande"));
                commandeDAO.finaliserCommande(idCommande);
                response.sendRedirect("commande?action=listePreparations");
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la finalisation de la commande: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else {
            // Pour les autres actions, déléguer au doGet
            doGet(request, response);
        }
    }
}
