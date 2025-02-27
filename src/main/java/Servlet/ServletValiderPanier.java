package Servlet;

import DAO.CommandeDAO;
import DAO.MagasinDAO;
import DAO.PanierDAO;
import DAO.ProductDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;


@WebServlet("/validerPanier")
public class ServletValiderPanier extends HttpServlet {
    private MagasinDAO magasinDAO = new MagasinDAO();
    private CommandeDAO commandeDAO = new CommandeDAO();
    private ProductDAO productDAO = new ProductDAO(); // 新增DAO实例

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

            // 3. 获取并过滤购物车数据（新增库存检查）
            Map<Long, Integer> panierMap = getPanierFromCookie(request);
            Map<Long, Integer> filteredPanier = filterOutOfStockItems(panierMap); // 关键修改

            // 4. 如果购物车已空则返回错误
            if (filteredPanier.isEmpty()) {
                response.sendRedirect("panier?error=empty");
                return;
            }

            // 5. 计算总价
            List<Produit> products = productDAO.getProductsByIds(filteredPanier.keySet());
            double total = calculateTotal(filteredPanier, products);

            // 6. 创建订单
            Commande commande = new Commande();
            commande.setPrixTotal(total);
            commande.setDateCommande(new Date());
            commande.setStatut(Statut.EN_COURS);
            commande.setClient(currentUser);
            commande.setMagasin(magasin);

            // 7. 保存订单及关联商品（使用过滤后的数据）
            commandeDAO.createCommande(commande, filteredPanier);

            // 8. 清空购物车
            clearCartCookie(request, response);

            // 9. 跳转确认页面
            response.sendRedirect("confirmation?commandeId=" + commande.getIdCommande());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("panier?error=stock");
        }
    }

    // 新增方法：过滤无库存商品
    private Map<Long, Integer> filterOutOfStockItems(Map<Long, Integer> panierMap) {
        Map<Long, Integer> filtered = new HashMap<>();

        panierMap.forEach((productId, quantity) -> {
            if (productDAO.isProduitAvailable(productId.intValue())) { // 假设DAO有库存检查方法
                filtered.put(productId, quantity);
            } else {
                System.out.println("[库存过滤] 商品ID " + productId + " 已售罄");
            }
        });

        return filtered;
    }

    // 修改原计算方法（添加有效性校验）
    private double calculateTotal(Map<Long, Integer> panierMap, List<Produit> products) {
        return products.stream()
                .filter(p -> panierMap.containsKey(Long.valueOf(p.getIdProduit())))
                .mapToDouble(p -> {
                    Integer quantity = panierMap.get(Long.valueOf(p.getIdProduit()));
                    // 双重校验（商品存在且库存充足）
                    if (quantity == null || quantity <= 0 || !productDAO.isProduitAvailable(p.getIdProduit())) {
                        return 0.0;
                    }
                    return p.getPrixApresPromotion() * quantity;
                })
                .sum();
    }

    private Map<Long, Integer> getPanierFromCookie(HttpServletRequest request) throws IOException {

        // 1. 查找名为 "panier" 的 Cookie
        String panierValue = null;
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
                    panierValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    ObjectMapper mapper = new ObjectMapper();
                    panierMap = mapper.readValue(panierValue, new TypeReference<HashMap<Long, Integer>>(){});
                    break;
                }
            }
        }

        if (panierValue == null || panierValue.isEmpty()) {
            return Collections.emptyMap();
        }

        // 2. 解析 JSON 字符串为 Map<Long, Integer>
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 3. 明确指定键和值的类型
            JavaType type = mapper.getTypeFactory().constructMapType(
                    Map.class,
                    Long.class,
                    Integer.class
            );
            return mapper.readValue(panierValue, type);
        } catch (JsonProcessingException e) {
            // 4. 处理 JSON 解析错误（例如：无效格式）
            System.err.println("Failed to parse panier cookie: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private void clearCartCookie(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            // 已登录用户：清空专属 Cookie
            User user = (User) session.getAttribute("user");
            String cookieName = "userId_" + user.getIdUser() + "_panier";

            // 创建立即过期的 Cookie 覆盖原有值
            Cookie clearCookie = new Cookie(cookieName, "");
            clearCookie.setMaxAge(0);      // 立即过期
            clearCookie.setPath("/");      // 路径需与写入时一致
            resp.addCookie(clearCookie);
        } else {
            // 未登录用户：清空默认 Cookie
            Cookie clearCookie = new Cookie("panier", "");
            clearCookie.setMaxAge(0);
            clearCookie.setPath("/");
            resp.addCookie(clearCookie);
        }
        resp.setStatus(200);
    }
}
