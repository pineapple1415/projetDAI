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

@WebServlet("/filtrer")
public class ServletFiltrer extends HttpServlet {
    private final FiltrerDAO FiltrerDAO = new FiltrerDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if ("listfiltrer".equals(action)) {
            List<Categorie> categories = FiltrerDAO.getAllCategories();
            List<Rayon> rayons = FiltrerDAO.getAllRayon();

            // Création d'un objet JSON personnalisé
            String jsonResponse = objectMapper.writeValueAsString(new ResponseData(categories, rayons));
            resp.getWriter().write(jsonResponse);
        }
    }

    // Classe interne pour structurer la réponse JSON
    private static class ResponseData {
        public List<Categorie> categories;
        public List<Rayon> rayons;

        public ResponseData(List<Categorie> categories, List<Rayon> rayons) {
            this.categories = categories;
            this.rayons = rayons;
        }
    }
}
