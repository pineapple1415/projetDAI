package Servlet;

import model.Gerant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/gerant")
public class ServletGerant extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Gerant gerant = (Gerant) session.getAttribute("user");

        if (gerant == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("/jsp/gerant.jsp").forward(request, response);
    }
}
