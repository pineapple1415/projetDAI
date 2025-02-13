package Servlet;

import DAO.FiltrerDAO;
import model.Categorie;
import model.Rayon;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/filtrer")
public class ServletFiltrer extends HttpServlet {
    private final FiltrerDAO filtrerDAO = new FiltrerDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            String action = req.getParameter("action");

            if ("listfiltrer".equals(action)) {
                List<String> categories = filtrerDAO.getNameCategories();
                List<String> rayons = filtrerDAO.getNameRayon();

                if (categories == null || rayons == null) {
                    throw new NullPointerException("Les données de catégories ou de rayons sont null.");
                }

                // Création d'une structure de réponse JSON
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("categories", categories);
                responseData.put("rayons", rayons);

                // Conversion en JSON et envoi de la réponse
                resp.getWriter().write(objectMapper.writeValueAsString(responseData));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètre 'action' invalide.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Enregistre l'erreur dans les logs du serveur
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne du serveur : " + e.getMessage());
        }
    }
}
