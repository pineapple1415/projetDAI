package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * Servlet implementation class CtrlConfirmerSelection
 */
@WebServlet("/ServletConfirmerSelectionFinal")
public class ServletConfirmerSelectionFinal extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Récupération des identifiants des commandes sélectionnées
        String[] commandesSelectionnees = request.getParameterValues("commandesSelectionnees");

        if (commandesSelectionnees != null && commandesSelectionnees.length > 0) {
            // Mise en session de la liste des commandes sélectionnées
            request.getSession().setAttribute("commandesSelectionnees", commandesSelectionnees);

            // Redirection vers la page de confirmation
            request.getRequestDispatcher("jsp/confirmerSelectionFinal.jsp").forward(request, response);
        } else {
            // Si aucune commande sélectionnée, afficher un message d'erreur
            request.setAttribute("msg_info", "Vous devez choisir au moins une commande à sélectionner.");
            request.getRequestDispatcher("commande?action=listePreparations").forward(request, response);
        }
    }
}

