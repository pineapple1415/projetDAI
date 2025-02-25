package Servlet;

import DAO.CommandeDAO;
import model.Commande;
import model.Preparateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/preparateur")  // 直接映射
public class ServletPreparateur extends HttpServlet {
    private CommandeDAO commandeDAO = new CommandeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Preparateur preparateur = (Preparateur) session.getAttribute("user");

        if (preparateur == null) {
            System.out.println("⚠️ Aucun préparateur en session, redirection vers login.jsp");
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println("✅ Préparateur connecté : " + preparateur.getNom());
        System.out.println("🏬 Magasin : " + preparateur.getMagasin().getAdresseMagasin());

        // 获取 PAYEE 状态的订单
        List<Commande> commandes = commandeDAO.getCommandesPayees(preparateur.getMagasin());
        request.setAttribute("commandes", commandes);

        // 跳转到 JSP
        request.getRequestDispatcher("/jsp/AccueilPreparateur.jsp").forward(request, response);
    }
}
