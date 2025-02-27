package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/produits")
public class ServletAffiche extends HttpServlet {
    private ProductDAO produitDAO = new ProductDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        HttpSession session = request.getSession();
        String applyFiltre = request.getParameter("applyFiltre");

        if ("true".equals(applyFiltre)) {
            session.removeAttribute("filters");
        }

        String filtersParam = request.getParameter("filters");
        List<String> selectedCategories = new ArrayList<>();
        List<String> selectedRayons = new ArrayList<>();

        if (filtersParam != null && !filtersParam.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, List<String>> filters = objectMapper.readValue(filtersParam, Map.class);
                selectedCategories = filters.getOrDefault("categories", new ArrayList<>());
                selectedRayons = filters.getOrDefault("rayons", new ArrayList<>());
                session.setAttribute("filters", filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Object[]> produits = produitDAO.getFilteredProducts(selectedCategories, selectedRayons);
        List<Map<String, Object>> produitsList = new ArrayList<>();

        String contextPath = request.getContextPath();

        for (Object[] produit : produits) {
            String rawImageUrl = (String) produit[1];
            String imageUrl;

            if (rawImageUrl.startsWith("http://") || rawImageUrl.startsWith("https://")) {
                imageUrl = rawImageUrl;
            } else {
                imageUrl = contextPath + "/" + rawImageUrl;
            }

            Map<String, Object> produitMap = new HashMap<>();
            produitMap.put("idProduit", ((Integer) produit[0]));
            produitMap.put("imageUrl", imageUrl);
            produitMap.put("nomProduit", produit[2]);
            produitMap.put("prixUnit", produit[3]);
            produitMap.put("promotion", produit[4]);
            produitsList.add(produitMap);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(produitsList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}

