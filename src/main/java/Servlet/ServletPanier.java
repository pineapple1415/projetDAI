package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import model.Produit;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import model.ProduitDansPanier;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/panier")
public class ServletPanier extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 读取购物车Cookie
            Cookie[] cookies = request.getCookies();
            String panierValue = "";
            for (Cookie cookie : cookies) {
                if ("panier".equals(cookie.getName())) {
                    panierValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }

            // 解析JSON数据
            ObjectMapper mapper = new ObjectMapper();
            Map<Long, Integer> panierMap = new HashMap<>(); // key - id value quantity
            if (!panierValue.isEmpty()) {
                panierMap = mapper.readValue(panierValue, new TypeReference<HashMap<Long, Integer>>(){});
            }

            // 获取商品详细信息
            ProductDAO productDAO = new ProductDAO();
            List<Produit> products = productDAO.getAllProducts();
            Map<Long, Produit> productInfoMap = new HashMap<>();
            for (Produit p : products) {
                productInfoMap.put(Long.valueOf(p.getIdProduit()), p);
            }

            List<ProduitDansPanier> cart = new ArrayList<>();
            for (Map.Entry<Long, Integer> item : panierMap.entrySet()) {
                Produit produit = productInfoMap.get(item.getKey());

                if (produit != null) {
                    Hibernate.initialize(produit.getComposers());
                }

                ProduitDansPanier produitDansPanier = new ProduitDansPanier();
                produitDansPanier.setProduit(produit);
                produitDansPanier.setQuantity(item.getValue());
                cart.add(produitDansPanier);
            }




            mapper.writeValue(response.getWriter(), new CartResponse(cart));
        } catch (Exception e) {
            e.printStackTrace();
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