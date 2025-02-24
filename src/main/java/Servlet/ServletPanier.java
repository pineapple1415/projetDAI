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
import model.User;
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

            // --- 新增：根据用户状态获取 Cookie 名称 ---
            String cookieName = "panier"; // 默认未登录用户的 Cookie 名称
            HttpSession session = request.getSession(false);
            Long userId = null;

            // 如果用户已登录，生成带用户ID的 Cookie 名称
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                userId = Long.valueOf(user.getIdUser());
                cookieName = "userId_" + userId + "_panier"; // 格式：userId_123_panier
            }

            // --- 修改：根据 cookieName 查找对应 Cookie ---
            Map<Long, Integer> panierMap = new HashMap<>();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookieName.equals(cookie.getName())) {
                        String panierValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        ObjectMapper mapper = new ObjectMapper();
                        panierMap = mapper.readValue(panierValue, new TypeReference<HashMap<Long, Integer>>(){});
                        break;
                    }
                }
            }

            // --- 保留原有商品信息获取逻辑 ---
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
                    ProduitDansPanier produitDansPanier = new ProduitDansPanier();
                    produitDansPanier.setProduit(produit);
                    produitDansPanier.setQuantity(item.getValue());
                    cart.add(produitDansPanier);
                }
            }

            // --- 新增：可选合并未登录购物车（按需启用）---
            if (userId != null) {
                // 如果已登录，可在此合并未登录时的 panier Cookie（需额外实现）
                // handleMergeGuestCart(request, userId);
            }

            new ObjectMapper().writeValue(response.getWriter(), new CartResponse(cart));
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