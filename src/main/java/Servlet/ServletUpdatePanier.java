package Servlet;

import DAO.PanierDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

@WebServlet("/updateCart")
public class ServletUpdatePanier extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            String jsonString = jsonBody.toString();

            // 解析请求参数
            Map<String, String> params = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});
            Long productId = Long.parseLong(params.get("productId"));
            int newQuantity = Integer.parseInt(params.get("quantity"));

            System.out.println("productId = " + productId);
            System.out.println("newQuantity = " + newQuantity);
            HttpSession session = req.getSession(false);

            if (session != null && session.getAttribute("user") != null) {
                // 已登录用户：更新数据库
                User user = (User) session.getAttribute("user");
                new PanierDAO().updateCartItem(user, productId, newQuantity);
            } else {
                // 未登录用户：更新Cookie
                updateCookieCart(req, resp, productId, newQuantity);
            }

            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void updateCookieCart(HttpServletRequest req, HttpServletResponse resp,
                                  Long productId, int newQuantity) throws IOException {
        // 获取用户登录状态
        HttpSession session = req.getSession(false);
        String cookieName = "panier"; // 默认 Cookie 名称
        int maxAge = 3 * 24 * 3600;   // 默认有效期：3天

        // 动态生成 Cookie 名称和有效期
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            cookieName = "userId_" + user.getIdUser() + "_panier"; // 格式：userId_123_panier
            maxAge = 7 * 24 * 3600; // 已登录用户有效期：7天
        }

        Map<Long, Integer> panierMap = new HashMap<>();
        Cookie[] cookies = req.getCookies();

        // 1. 读取现有 Cookie（根据动态名称）
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) { // 动态匹配 Cookie 名称
                    try {
                        panierMap = mapper.readValue(
                                URLDecoder.decode(cookie.getValue(), "UTF-8"),
                                new TypeReference<HashMap<Long, Integer>>() {}
                        );
                    } catch (Exception e) {
                        panierMap = new HashMap<>();
                    }
                    break;
                }
            }
        }

        // 2. 更新数量
        panierMap.put(productId, newQuantity);

        // 3. 保存新 Cookie（使用动态名称和有效期）
        String cookieValue = URLEncoder.encode(
                mapper.writeValueAsString(panierMap),
                "UTF-8"
        );
        Cookie newCookie = new Cookie(cookieName, cookieValue);
        newCookie.setMaxAge(maxAge);       // 动态设置有效期
        newCookie.setPath("/");            // 确保全路径可访问
        newCookie.setHttpOnly(true);       // 增强安全性
        resp.addCookie(newCookie);
    }

}