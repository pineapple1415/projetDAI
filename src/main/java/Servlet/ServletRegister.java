package Servlet;

import DAO.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class ServletRegister extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String address = request.getParameter("address");
        String codePostal = request.getParameter("codePostal");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String login = request.getParameter("email");
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
        User newUser = new User(name, prenom, address, codePostal, email, telephone, login, password, "client");

        userDAO.saveUser(newUser);

        // Redirection vers login.jsp après succès
        response.sendRedirect("login.jsp");
    }
}
