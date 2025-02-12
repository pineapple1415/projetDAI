package Servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import DAO.PanierDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;



@WebServlet("/addToPanier")
public class ServletAddPanier extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        System.out.println("------ Servlet被调用 ------"); // 确认是否执行

        // 解析请求参数
        Map<String, String> params = objectMapper.readValue(request.getReader(), HashMap.class);
        Long productId = Long.parseLong(params.get("productId"));
        int quantity = Integer.parseInt(params.getOrDefault("quantity", "1"));

        HttpSession session = request.getSession(false);
        Map<String, Object> responseData = new HashMap<>();

        if (session != null && session.getAttribute("user") != null) {
            // 已登录用户（7天有效期）
            User user = (User) session.getAttribute("user");
            new PanierDAO().addItemToPanier(user, productId, quantity);
            responseData.put("type", "db");
            responseData.put("userId", user.getId()); // 返回用户ID用于Cookie标识
        } else {
            // 未登录用户（3天有效期）
            int cookieDays = 3;
            handleCookiePanier(request, resp, productId, quantity, cookieDays);
            responseData.put("type", "cookie");
        }

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), responseData);
    }

    private void handleCookiePanier(HttpServletRequest req, HttpServletResponse resp,
                                    Long productId, int quantity, int maxAgeDays) throws IOException {
        // 数据结构：{ productId: quantity }
        Map<Long, Integer> panierMap = new HashMap<>();
        Cookie panierCookie = Arrays.stream(req.getCookies())
                .filter(c -> "panier".equals(c.getName()))
                .findFirst().orElse(null);

        // 解析现有Cookie
        if (panierCookie != null) {
            panierMap = objectMapper.readValue(
                    URLDecoder.decode(panierCookie.getValue(), "UTF-8"),
                    new TypeReference<HashMap<Long, Integer>>(){}
            );
        }

        // 更新数量
        panierMap.put(productId, panierMap.getOrDefault(productId, 0) + quantity);

        // 创建新Cookie
        String cookieValue = URLEncoder.encode(
                objectMapper.writeValueAsString(panierMap),
                "UTF-8"
        );

        Cookie newCookie = new Cookie("panier", cookieValue);
        newCookie.setMaxAge(maxAgeDays * 24 * 3600);
        newCookie.setPath("/");
        resp.addCookie(newCookie);
    }
}