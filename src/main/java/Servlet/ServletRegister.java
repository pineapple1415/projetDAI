package Servlet;

import DAO.UserDAO;
import model.User;
import model.Client;
import model.Gerant;
import model.Preparateur;
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
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType"); // 确保前端传入 userType

        // 密码加密
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser;

        // 🏷 **实例化具体的子类**
        switch (userType.toUpperCase()) {
            case "CLIENT":
                String adresse = request.getParameter("adresse");
                String codePostal = request.getParameter("codePostal");
                String telephone = request.getParameter("telephone");
                newUser = new Client(nom, prenom, email, hashedPassword, adresse, codePostal, telephone);
                break;

            case "GERANT":
                newUser = new Gerant(nom, prenom, email, hashedPassword);
                break;

            case "PREPARATEUR":
                newUser = new Preparateur(nom, prenom, email, hashedPassword);
                break;

            default:
                throw new ServletException("Type d'utilisateur inconnu : " + userType);
        }

        // ✅ **正确：实例化 UserDAO 再调用 saveUser**
        UserDAO userDAO = new UserDAO();
        userDAO.saveUser(newUser);

        response.sendRedirect("login.jsp");
    }
}
