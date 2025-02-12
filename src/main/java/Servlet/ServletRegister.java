package Servlet;

import DAO.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class ServletRegister extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();

        // Vérifier si l'email existe déjà
        if (userDAO.getUserByEmail(email) != null) {
            request.setAttribute("error", "Cet email est déjà utilisé.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Hachage du mot de passe avec BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Création de l'utilisateur
        User user = new User(name, prenom, email, telephone, address, hashedPassword);
        userDAO.saveUser(user);

        // Redirection vers login.jsp après succès
        response.sendRedirect("login.jsp");
    }
}
