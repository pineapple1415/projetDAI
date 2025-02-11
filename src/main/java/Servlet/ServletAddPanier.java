package Servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import DAO.PanierDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;



@WebServlet("/addToPanier")
public class ServletAddPanier extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Long produitId = Long.parseLong(req.getParameter("produitId"));

        HttpSession session = req.getSession(false);
        Map<String, Object> responseData = new HashMap<>();

        if (session != null && session.getAttribute("user") != null) {
            // 已登录用户
            User user = (User) session.getAttribute("user");
            new PanierDAO().addItemToPanier(user, produitId);
            responseData.put("type", "db");
        } else {
            // 未登录用户使用 Cookie
            handleCookiePanier(req, response, produitId);
            responseData.put("type", "cookie");
        }

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handleCookiePanier(HttpServletRequest req, HttpServletResponse resp, Long produitId) {
        // 使用 JSON 格式存储商品ID数组
        Cookie[] cookies = req.getCookies();
        List<Long> produitIds = new ArrayList<>();

        Optional<Cookie> panierCookie = Arrays.stream(cookies)
                .filter(c -> "tempPanier".equals(c.getName()))
                .findFirst();

        if (panierCookie.isPresent()) {
            try {
                produitIds = objectMapper.readValue(panierCookie.get().getValue(),
                        new TypeReference<List<Long>>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        if (!produitIds.contains(produitId)) {
            produitIds.add(produitId);
        }

        Cookie newCookie = null;
        try {
            newCookie = new Cookie("tempPanier",
                    objectMapper.writeValueAsString(produitIds));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        newCookie.setMaxAge(7 * 24 * 3600);
        resp.addCookie(newCookie);
    }
}