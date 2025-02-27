package Servlet;

import DAO.CommandeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Commande;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletValiderSelection")
public class ServletValiderSelection extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String[] commandesSelectionnees = (String[]) request.getSession().getAttribute("commandesSelectionnees");

        if ("Confirmer".equals(action) && commandesSelectionnees != null) {
            try {
                for (String idCommande : commandesSelectionnees) {
                    int id = Integer.parseInt(idCommande);
                    commandeDAO.marquerCommandeEnPreparation(id);
                }
                response.sendRedirect("commande?action=listePreparationsPrioritaire");
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la mise à jour des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("Retour".equals(action)) {
            response.sendRedirect("jsp/AccueilPreparateur.jsp");
        } else {
            response.sendRedirect("commande?action=listePreparations");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String[] commandesSelectionnees = (String[]) request.getSession().getAttribute("commandesSelectionnees");

        if ("Confirmer".equals(action) && commandesSelectionnees != null) {
            try {
                for (String idCommande : commandesSelectionnees) {
                    int id = Integer.parseInt(idCommande);
                    commandeDAO.finaliserCommande(id);
                }
                response.sendRedirect("commande?action=listePreparationsPrioritaire");
            } catch (Exception e) {
                request.setAttribute("msgerreur", "Erreur lors de la mise à jour des commandes: " + e.getMessage());
                request.getRequestDispatcher("jsp/AccueilPreparateur.jsp").forward(request, response);
            }
        } else if ("Retour".equals(action)) {
            response.sendRedirect("jsp/AccueilPreparateur.jsp");
        } else {
            response.sendRedirect("commande?action=listePreparations");
        }
    }
}