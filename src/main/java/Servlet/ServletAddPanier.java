package Servlet;

import DAO.PanierDAO;
import model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/addToCart")
public class ServletAddPanier extends HttpServlet {
    private final PanierDAO panierDAO = new PanierDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 解析请求数据
        Map<String, String> requestData = objectMapper.readValue(req.getReader(), HashMap.class);
        Long productId = Long.parseLong(requestData.get("productId"));
        String productName = requestData.get("productName");

        // 获取会话信息
        HttpSession session = req.getSession(false);
        Map<String, Object> responseData = new HashMap<>();

        if (session != null && session.getAttribute("user") != null) {
            // 已登录用户,我们直接添加到他的panier里
            User user = (User) session.getAttribute("user");
            panierDAO.addItemToUserCart(user, productId, productName);
            responseData.put("success", true);
        } else {
            // 未登录用户，创建一个cookie临时存储
            String cookieCart = handleCookieCart(req, resp, productId, productName);
            responseData.put("success", true);
            responseData.put("cartCount", cookieCart.split(",").length);
        }

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), responseData);
    }

    private String handleCookieCart(HttpServletRequest req, HttpServletResponse resp,
                                    Long productId, String productName) {
        // 读取或创建Cookie
        Cookie[] cookies = req.getCookies();
        Optional<Cookie> cartCookie = Arrays.stream(cookies)
                .filter(c -> "tempCart".equals(c.getName()))
                .findFirst();

        String newCartValue;
        if (cartCookie.isPresent()) {
            newCartValue = cartCookie.get().getValue() +
                    String.format(";%d:%s", productId, productName);
        } else {
            newCartValue = String.format("%d:%s", productId, productName);
        }

        // 创建/更新Cookie
        Cookie newCookie = new Cookie("tempCart", newCartValue);
        newCookie.setMaxAge(3 * 24 * 60 * 60); // 3天有效期
        newCookie.setPath("/");
        resp.addCookie(newCookie);

        return newCartValue;
    }
}