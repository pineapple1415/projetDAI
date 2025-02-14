package Servlet;

import DAO.CommandeDAO;
import DAO.ComposerDAO;
import model.Commande;
import model.Composer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/preparerCommande")
public class ServletPreparerCommande extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO();
    private ComposerDAO composerDAO = new ComposerDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int commandeId = Integer.parseInt(request.getParameter("id"));

        Commande commande = commandeDAO.getCommandeById(commandeId);
        List<Composer> produits = composerDAO.getProduitsByCommande(commandeId);

        request.setAttribute("commandeId", commandeId);
        request.setAttribute("produits", produits);
        request.getRequestDispatcher("jsp/preparerCommande.jsp").forward(request, response);
    }
}
