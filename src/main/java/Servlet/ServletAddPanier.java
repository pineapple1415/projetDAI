package Servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import DAO.PanierDAO;
import model.User;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


//@WebServlet("/addToPanier")
public class ServletAddPanier extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        try{
            System.out.println("------ Servlet used ------"); // 确认是否执行

            // 解析请求参数
            Map<String, String> params = objectMapper.readValue(request.getReader(), HashMap.class);
            Long productId = Long.parseLong(params.get("productId"));
            int quantity = Integer.parseInt(String.valueOf(params.getOrDefault("quantity", "1")));


            HttpSession session = request.getSession(false);
            Map<String, Object> responseData = new HashMap<>();

            if (session != null && session.getAttribute("user") != null) {
                // 已登录用户（7天有效期）
                User user = (User) session.getAttribute("user");
                new PanierDAO().addItemToPanier(user, productId, quantity);
                responseData.put("type", "db");
                responseData.put("userId", user.getIdUser()); // 返回用户ID用于Cookie标识

                resp.sendRedirect("ajouteSuccess"); // 重定向到成功页面
            } else {
                // 未登录用户（3天有效期）
                int cookieDays = 3;
                handleCookiePanier(request, resp, productId, quantity, cookieDays);
                responseData.put("type", "cookie");

                resp.sendRedirect("ajouteSuccess"); // 重定向到成功页面
            }

            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), responseData);

        }catch (Exception e) {
            e.printStackTrace(); // 打印完整堆栈信息
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }


    }

    private void handleCookiePanier(HttpServletRequest req, HttpServletResponse resp,
                                    Long productId, int quantity, int maxAgeDays) throws IOException {
        Map<Long, Integer> panierMap = new HashMap<>();

        // 读取现有 Cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("panier".equals(cookie.getName())) {
                    try {
                        panierMap = objectMapper.readValue(
                                URLDecoder.decode(cookie.getValue(), "UTF-8"),
                                new TypeReference<HashMap<Long, Integer>>(){}
                        );
                    } catch (Exception e) {
                        // 如果解析失败，重置购物车
                        panierMap = new HashMap<>();
                    }
                    break;
                }
            }
        }

        // 更新数量
        panierMap.put(productId, panierMap.getOrDefault(productId, 0) + quantity);

        // 保存新 Cookie
        String cookieValue = URLEncoder.encode(
                objectMapper.writeValueAsString(panierMap),
                "UTF-8"
        );

        Cookie newCookie = new Cookie("panier", cookieValue);
        newCookie.setMaxAge(maxAgeDays * 24 * 3600);
        newCookie.setPath("/"); // 确保 Cookie 对所有路径可见
        resp.addCookie(newCookie);

        // 在handleCookiePanier方法中添加
        System.out.println("new Cookie : " + panierMap);
        System.out.println("set Cookie route: " + newCookie.getPath());

    }
}


