package Servlet;

import DAO.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("🚀 ServletLogin est appelé !"); // 确保 Servlet 被调用

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("📩 Email reçu : " + email);
        System.out.println("🔑 Mot de passe reçu : " + password);

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            System.out.println("❌ Utilisateur introuvable !");
            response.sendRedirect("login.jsp?error=1");
            return;
        }

        System.out.println("✅ Utilisateur trouvé : " + user.getNom() + " " + user.getPrenom());
        System.out.println("🎭 Type d'utilisateur : " + user.getUserType());

        // 验证密码
        if (password.equals(user.getPassword())) {
            System.out.println("🔓 Mot de passe correct !");

            // 创建 session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            System.out.println("🎯 Session créée : " + session.getId());

            // 创建 Cookie
            Cookie loginCookie = new Cookie("userEmail", email);
            loginCookie.setMaxAge(30 * 60); // 30 分钟
            loginCookie.setHttpOnly(true);
            response.addCookie(loginCookie);

            // 根据用户类型跳转
            String redirectPath = request.getContextPath();
            switch (user.getUserType()) {
                case "CLIENT":
                    redirectPath += "/client";
                    break;
                case "GERANT":
                    redirectPath += "/gerant";
                    break;
                case "PREPARATEUR":
                    redirectPath += "/preparateur"; // 这里要和 Servlet 映射匹配
                    break;
                default:
                    redirectPath += "/jsp/index.jsp";
                    break;
            }
            System.out.println("🔄 Redirection vers : " + redirectPath);
            response.sendRedirect(redirectPath);
        } else {
            System.out.println("❌ Mot de passe incorrect !");
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
