package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import model.Produit;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProduitDansPanier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/panier")
public class ServletPanier extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<ProduitDansPanier> cart = (List<ProduitDansPanier>) session.getAttribute("cart");

        if (cart == null) cart = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String json = mapper.writeValueAsString(new CartResponse(cart));
            response.getWriter().write(json);
        } catch (JsonProcessingException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"数据序列化失败\"}");
        }
    }

    // 辅助类确保JSON结构正确
    private static class CartResponse {
        public List<ProduitDansPanier> items;
        public double total;

        public CartResponse(List<ProduitDansPanier> items) {
            this.items = items;
            this.total = items.stream()
                    .mapToDouble(item -> item.getProduit().getPrixUnit() * item.getQuantity())
                    .sum();
        }
    }
}