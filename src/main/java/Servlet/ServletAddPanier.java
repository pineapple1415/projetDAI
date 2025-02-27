package Servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/addToPanier")
public class ServletAddPanier extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        try{
            System.out.println("------ Servlet used ------"); // 确认是否执行

            // **解析请求参数（Map<String, String>）**
            Map<String, String> params = objectMapper.readValue(request.getReader(), new TypeReference<HashMap<String, String>>() {});

            // **确保 productId 和 quantity 先转为 String，再解析**
            String productIdStr = params.get("productId");
            if (productIdStr == null) {
                throw new IllegalArgumentException("❌ productId 参数缺失！");
            }
            Long productId = Long.parseLong(productIdStr); // ✅ 转换为 Long

            String quantityStr = params.getOrDefault("quantity", "1");
            int quantity = Integer.parseInt(quantityStr); // ✅ 转换为 Integer

            HttpSession session = request.getSession(false);
            Map<String, Object> responseData = new HashMap<>();
            Long userId = getUserIdFromSession(session); // 🔹 修正 session 获取 userId

            if (userId != null) {
                handleCookiePanier(request, resp, productId, quantity, 7, userId);
                responseData.put("type", "cookie");
                responseData.put("userId", userId);
                resp.sendRedirect("ajouteSuccess"); // ✅ 重定向
            } else {
                handleCookiePanier(request, resp, productId, quantity, 3, null);
                responseData.put("type", "cookie");
                resp.sendRedirect("ajouteSuccess"); // ✅ 重定向
            }

            // **返回 JSON 响应**
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), responseData);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // **🔹 修正 session 解析用户 ID**
    private Long getUserIdFromSession(HttpSession session) {
        if (session != null && session.getAttribute("user") != null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof User) {
                User user = (User) userObj;
                return (user.getIdUser() != null) ? user.getIdUser().longValue() : null;
            }
        }
        return null;
    }

    private void handleCookiePanier(
            HttpServletRequest req, HttpServletResponse resp,
            Long productId, int quantity, int maxAgeDays, Long userId
    ) throws IOException {
        Map<Long, Integer> panierMap = new HashMap<>();

        // **生成 Cookie 名称**
        String cookieName = (userId != null) ? "userId_" + userId + "_panier" : "panier";

        // **读取现有 Cookie**
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    try {
                        panierMap = new ObjectMapper().readValue(
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

        // **确保新商品不会覆盖已有数据**
        panierMap.put(productId, panierMap.getOrDefault(productId, 0) + quantity);

        // **调试日志**
        System.out.println("✅ 购物车更新：" + panierMap);

        // **保存新的 Cookie**
        String cookieValue = URLEncoder.encode(
                new ObjectMapper().writeValueAsString(panierMap),
                "UTF-8"
        );

        Cookie newCookie = new Cookie(cookieName, cookieValue);
        newCookie.setMaxAge(maxAgeDays * 24 * 3600);
        newCookie.setPath("/");
        newCookie.setHttpOnly(false); // 允许前端访问（测试时用）
        resp.addCookie(newCookie);

        // **防止浏览器缓存旧 Cookie**
        resp.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.addHeader("Pragma", "no-cache");
        resp.addHeader("Expires", "0");
    }


}
