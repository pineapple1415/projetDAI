package Servlet;

import DAO.FiltrerDAO;
import model.Produit;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/filtrer")
public class ServletFiltrer extends HttpServlet {
    private final FiltrerDAO filtrerDAO = new FiltrerDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            String action = req.getParameter("action");

            if ("listRayons".equals(action)) {
                // ✅ 获取所有 Rayons
                List<String> rayons = filtrerDAO.getNameRayon();
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("rayons", rayons);
                resp.getWriter().write(objectMapper.writeValueAsString(responseData));

            } else if ("listCategoriesByRayon".equals(action)) {
                // ✅ 获取某个 Rayon 下面的 Catégories
                String rayon = req.getParameter("rayon");
                List<String> categories = filtrerDAO.getCategoriesByRayon(rayon);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("categories", categories);
                resp.getWriter().write(objectMapper.writeValueAsString(responseData));

            } else if ("appliquerFiltre".equals(action)) {
                try {
                    String categoriesParam = req.getParameter("categories");

                    System.out.println("🔍 [Servlet] Categories reçues: " + categoriesParam);

                    List<String> selectedCategories = (categoriesParam != null && !categoriesParam.isEmpty())
                            ? Arrays.asList(categoriesParam.split(","))
                            : new ArrayList<>();

                    List<Produit> produitsFiltres = filtrerDAO.filterProductsByRayonAndCategories(selectedCategories);

                    System.out.println("📦 [Servlet] Produits trouvés: " + produitsFiltres.size());

                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write(objectMapper.writeValueAsString(produitsFiltres));
                } catch (Exception e) {
                    System.err.println("❌ [Servlet] Erreur lors de l'application du filtre:");
                    e.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne du serveur : " + e.getMessage());
                }
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètre 'action' invalide.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne du serveur : " + e.getMessage());
        }
    }
}
