package Servlet;

import DAO.CommandeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Commande;

import java.io.IOException;

@WebServlet("/creneau")
public class ServletReserveCreneau extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO(); // Assurez-vous que votre DAO utilise Hibernate

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupération des paramètres du formulaire
            int commandeId = Integer.parseInt(request.getParameter("idCommande"));
            String date = request.getParameter("date");         // Format : yyyy-MM-dd
            String timeSlot = request.getParameter("timeSlot");   // Exemple : "09:00-10:00"

            // Combinaison de la date et du créneau en une chaîne (par exemple "2025-03-01 09:00-10:00")
            String chosenCreneau = date + " " + timeSlot;

            System.out.println("[DEBUG] idCommande reçu : " + request.getParameter("idCommande"));
            System.out.println("[DEBUG] date reçue : " + request.getParameter("date"));
            System.out.println("[DEBUG] timeSlot reçu : " + request.getParameter("timeSlot"));

            // Récupération de la commande via Hibernate
            Commande commande = commandeDAO.getCommandeById(commandeId);
            if (commande != null) {
                // Mise à jour du créneau de retrait dans la commande
                commande.setCreneauRetrait(chosenCreneau);
                commandeDAO.updateCommande(commande); // Méthode update dans votre DAO qui effectue la transaction Hibernate

                request.setAttribute("message", "Votre créneau de retrait (" + chosenCreneau + ") a été réservé avec succès !");
            } else {
                request.setAttribute("message", "Commande non trouvée.");
            }
            request.getRequestDispatcher("jsp/confirmationCreneau.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Erreur lors de la réservation du créneau : " + e.getMessage());
            request.getRequestDispatcher("jsp/confirmationCreneau.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String commandeId = request.getParameter("idCommande");
        Commande commande = commandeDAO.getCommandeById(Integer.parseInt(commandeId));

        if(commande != null && commande.getCreneauRetrait() != null) {
            // Découper le créneau existant pour pré-remplir le formulaire
            String[] creneauParts = commande.getCreneauRetrait().split(" ");
            request.setAttribute("currentDate", creneauParts[0]);
            request.setAttribute("currentTimeSlot", creneauParts[1]);
        }

        request.setAttribute("idCommande", commandeId);
        request.getRequestDispatcher("jsp/choixCreneauRetrait.jsp").forward(request, response);
    }
}
