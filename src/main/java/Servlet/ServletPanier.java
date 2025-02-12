package Servlet;

import DAO.ProductDAO;
import model.Produit;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ServletPanier extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //affiche panier
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        resp.setContentType("application/json");

        if ("listProducts".equals(action)) {
            List<Produit> products = productDAO.getAllProducts();
            objectMapper.writeValue(resp.getWriter(), products);
        }
    }
}