package Servlet;

import DAO.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        // Vérification du mot de passe avec BCrypt
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // Création d'une session utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Création d'un cookie sécurisé
            Cookie loginCookie = new Cookie("userEmail", email);
            loginCookie.setMaxAge(30 * 60); // Expire après 30 minutes
            loginCookie.setHttpOnly(true);  // Empêche l'accès via JavaScript (protection contre XSS)
            response.addCookie(loginCookie);

            // Redirection vers le tableau de bord
            response.sendRedirect("index.jsp");
        } else {
            // Redirection avec un message d'erreur
            response.sendRedirect("login.jsp?error=1");
        }
    }
}

