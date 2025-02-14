package Servlet;

import model.Gerant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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