package Servlet;

import DAO.CommandeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Commande;
import model.Magasin;

import java.io.IOException;
import java.util.List;

@WebServlet("/commande")
public class ServletCommande extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException {
        // Récupérer l'action demandée par l'utilisateur
        String act = request.getParameter("action");

        if (act.equals("consulter")) {
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
        } else if (act.equals("listePreparations")) {
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
        }else if (act.equals("listePreparationsPrioritaire")) {
            // US 5.2 – Consulter la liste des commandes à préparer par ordre de priorité
            try {
                Magasin magasin = (Magasin) request.getSession().getAttribute("magasin");
                List<Commande> commandes = commandeDAO.getCommandesEnCours();
                request.setAttribute("commandes", commandes);
                request.getRequestDispatcher("jsp/listeCommandesPrioritaire.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        }else if (act.equals("listePrioritairePrete")) {
            // US 5.2 – Consulter la liste des commandes à préparer par ordre de priorité
            try {
                Magasin magasin = (Magasin) request.getSession().getAttribute("magasin");
                List<Commande> commandes = commandeDAO.getCommandesPretes();
                request.setAttribute("commandes", commandes);
                request.getRequestDispatcher("jsp/listeCommandesPretes.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        }  else if (act.equals("retour")) {
            // Exemple d'une action "retour" pour revenir à une page d'accueil
            request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
        } else {
            // Action non reconnue
            request.setAttribute("msgerreur", "Action non reconnue: " + act);
            request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'action postée par l'utilisateur
        String act = request.getParameter("action");
        if (act == null) {
            doGet(request, response);
            return;
        }

        if (act.equals("marquerPreparation")) {
            // US 5.3 – Marquer une commande en préparation pour un retrait
            try {
                int idCommande = Integer.parseInt(request.getParameter("idCommande"));
                commandeDAO.marquerCommandeEnPreparation(idCommande);
                response.sendRedirect("commande?action=listePreparationsPrioritaire");
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la mise à jour de la commande: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if (act.equals("finaliserPreparation")) {
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
            // Pour les autres actions, on délègue au doGet
            doGet(request, response);
        }
    }
}
