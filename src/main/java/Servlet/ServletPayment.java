package Servlet;

import DAO.CommandeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Commande;
import model.Statut;

import java.io.IOException;

@WebServlet("/ProcessPayment")
public class ServletPayment extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int commandeId = Integer.parseInt(request.getParameter("commandeId"));

            // 获取支付信息（虽然不需要处理但可以记录）
            String cardNumber = request.getParameter("cardNumber");
            String expiryMonth = request.getParameter("expiryMonth");
            String expiryYear = request.getParameter("expiryYear");
            String cvv = request.getParameter("cvv");

            // 更新订单状态
            CommandeDAO commandeDAO = new CommandeDAO();
            Commande commande = commandeDAO.getCommandeById(commandeId);
            commande.setStatut(Statut.PAYEE);
            commandeDAO.updateCommande(commande);

            response.sendRedirect("paymentSuccess?commandeId=" + commandeId);

        } catch (Exception e) {
        }
    }
}