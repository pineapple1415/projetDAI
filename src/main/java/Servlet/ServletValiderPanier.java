package Servlet;

import DAO.CommandeDAO;
import DAO.MagasinDAO;
import DAO.PanierDAO;
import DAO.ProductDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;


@WebServlet("/validerPanier")
public class ServletValiderPanier extends HttpServlet {
    private MagasinDAO magasinDAO = new MagasinDAO();
    private CommandeDAO commandeDAO = new CommandeDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. 获取当前用户
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                response.sendRedirect("pageLogin");
                return;
            }

            // 2. 获取商店ID
            int magasinId = Integer.parseInt(request.getParameter("magasin"));
            Magasin magasin = magasinDAO.getMagasinById(magasinId);

            // 3. 获取购物车数据
            Map<Long, Integer> panierMap = getPanierFromCookie(request);
            List<Produit> products = new ProductDAO().getProductsByIds(panierMap.keySet());

            // 4. 计算总价
            double total = calculateTotal(panierMap, products);

            // 5. 创建订单
            Commande commande = new Commande();
            commande.setPrixTotal(total);
            commande.setDateCommande(new Date());
            commande.setStatut(Statut.EN_COURS);
            commande.setClient(currentUser);
            commande.setMagasin(magasin);

            // 6. 保存订单及关联商品
            commandeDAO.createCommande(commande, panierMap);

            // 7. 清空购物车
            clearCartCookie(response);

            // 8. 跳转确认页面
            response.sendRedirect("confirmation.jsp?commandeId=" + commande.getIdCommande());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private Map<Long, Integer> getPanierFromCookie(HttpServletRequest request) throws IOException {
        // 解析Cookie逻辑（同之前代码）
        return null;
    }

    private double calculateTotal(Map<Long, Integer> panierMap, List<Produit> products) {
        return products.stream()
                .mapToDouble(p -> p.getPrixUnit() * panierMap.get(p.getIdProduit()))
                .sum();
    }

    private void clearCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("panier", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
