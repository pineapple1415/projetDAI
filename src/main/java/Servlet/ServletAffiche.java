package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        String filtersParam = request.getParameter("filters");
        List<String> selectedCategories = new ArrayList<>();
        List<String> selectedRayons = new ArrayList<>();

        if (filtersParam != null && !filtersParam.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, List<String>> filters = objectMapper.readValue(filtersParam, Map.class);
                selectedCategories = filters.getOrDefault("categories", new ArrayList<>());
                selectedRayons = filters.getOrDefault("rayons", new ArrayList<>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 获取数据库中的产品信息，包括 imageUrl
        List<Object[]> produits = produitDAO.getFilteredProducts(selectedCategories, selectedRayons);
        List<Map<String, Object>> produitsList = new ArrayList<>();

        for (Object[] produit : produits) {
            Map<String, Object> produitMap = new HashMap<>();
            produitMap.put("imageUrl", produit[0]); // ✅ 添加图片URL
            produitMap.put("nomProduit", produit[1]);
            produitMap.put("prixUnit", produit[2]);
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
