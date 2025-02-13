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
import java.util.List;
import java.util.Map;

@WebServlet("/produits")
public class ServletAffiche extends HttpServlet {
    private ProductDAO produitDAO = new ProductDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 解析过滤条件
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

        try (PrintWriter out = response.getWriter()) {
            out.println("<?xml version=\"1.0\"?>");
            out.println("<produits>");

            List<Object[]> produits = produitDAO.getFilteredProducts(selectedCategories, selectedRayons);

            for (Object[] produit : produits) {
                out.println("<produit>");
                out.println("<nomProduit><![CDATA[" + produit[0] + "]]></nomProduit>");
                out.println("<prixUnit><![CDATA[" + produit[1] + "]]></prixUnit>");
                out.println("</produit>");
            }

            out.println("</produits>");
        }
    }
}
