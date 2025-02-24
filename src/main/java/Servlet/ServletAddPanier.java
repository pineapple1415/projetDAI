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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;


@WebServlet("/addToPanier")
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
                // 已登录用户：使用 Cookie 存储（7天有效期）
                User user = (User) session.getAttribute("user");
                Long userId = user.getIdUser() != null ? user.getIdUser().longValue() : null;
                handleCookiePanier(request, resp, productId, quantity, 7, userId);
                responseData.put("type", "cookie");
                responseData.put("userId", user.getIdUser());

                resp.sendRedirect("ajouteSuccess"); // 重定向到成功页面
            } else {
                // 未登录用户（3天有效期）
                int cookieDays = 3;
                handleCookiePanier(request, resp, productId, quantity, cookieDays, null); // 添加 null 作为 userId 参数
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

    private void handleCookiePanier(
            HttpServletRequest req, HttpServletResponse resp,
            Long productId, int quantity, int maxAgeDays, Long userId
    ) throws IOException {
        Map<Long, Integer> panierMap = new HashMap<>();

        // 生成 Cookie 名称：带用户ID标识（如 userId_123_panier）
        String cookieName = userId != null ? "userId_" + userId + "_panier" : "panier";

        // 读取现有 Cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    try {
                        panierMap = objectMapper.readValue(
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

        // 更新购物车数据
        panierMap.put(productId, panierMap.getOrDefault(productId, 0) + quantity);

        // 保存新 Cookie
        String cookieValue = URLEncoder.encode(
                objectMapper.writeValueAsString(panierMap),
                "UTF-8"
        );

        Cookie newCookie = new Cookie(cookieName, cookieValue);
        newCookie.setMaxAge(maxAgeDays * 24 * 3600);
        newCookie.setPath("/");
        newCookie.setHttpOnly(true); // 增强安全性
        resp.addCookie(newCookie);

        System.out.println("✅ Cookie 更新：" + cookieName + " -> " + panierMap);
    }
}


