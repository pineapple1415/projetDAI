package Servlet;

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
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}