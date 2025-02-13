package Servlet;

import DAO.PanierDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

@WebServlet("/annulerPanier")
public class ServletAnnulerPanier extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        try {
            if (session != null && session.getAttribute("user") != null) {
                // 已登录用户：清空数据库购物车
                User user = (User) session.getAttribute("user");
                new PanierDAO().viderPanier(user);
            } else {
                // 未登录用户：清空Cookie
                Cookie clearCookie = new Cookie("panier", "");
                clearCookie.setMaxAge(0); // 立即过期
                clearCookie.setPath("/"); // 匹配所有路径
                resp.addCookie(clearCookie);
            }
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}