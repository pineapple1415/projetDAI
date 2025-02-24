package Servlet;

import DAO.CommandeDAO;
import DAO.UserDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/client")
public class ServletClient extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            User user = (User) session.getAttribute("user");

            // 1. 合并购物车
            mergeGuestCart(request, response, Long.valueOf(user.getIdUser()));

            // 2. 获取用户历史订单
            CommandeDAO commandeDAO = new CommandeDAO();
            request.setAttribute("commandes", commandeDAO.getCommandesByUserId(user.getIdUser()));

            // 3. 转发到 client.jsp
            request.getRequestDispatcher("/jsp/client.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private void mergeGuestCart(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        // 读取临时购物车（未登录）
        Map<Long, Integer> guestCart = readCookieCart(request, "panier");
        // 读取用户专属购物车
        Map<Long, Integer> userCart = readCookieCart(request, "userId_" + userId + "_panier");

        // 合并数量
        guestCart.forEach((productId, quantity) ->
                userCart.put(productId, userCart.getOrDefault(productId, 0) + quantity)
        );

        // 保存合并后的购物车
        saveUserCart(response, userCart, userId);
        // 清除临时购物车
        clearGuestCart(response);
    }

    private Map<Long, Integer> readCookieCart(HttpServletRequest request, String cookieName) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return new HashMap<>();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                // 新增：对 Cookie 值进行 URL 解码
                String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
                return objectMapper.readValue(value, new TypeReference<HashMap<Long, Integer>>() {});
            }
        }
        return new HashMap<>();
    }

    private void saveUserCart(HttpServletResponse response, Map<Long, Integer> cart, Long userId) throws IOException {
        String cookieValue = objectMapper.writeValueAsString(cart);
        // 新增：对 JSON 字符串进行 URL 编码
        cookieValue = URLEncoder.encode(cookieValue, "UTF-8");

        Cookie cookie = new Cookie("userId_" + userId + "_panier", cookieValue);
        cookie.setMaxAge(7 * 24 * 3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private void clearGuestCart(HttpServletResponse response) {
        Cookie cookie = new Cookie("panier", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}