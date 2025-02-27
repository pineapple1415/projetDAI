package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import model.Produit;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import model.ProduitDansPanier;
import model.User;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

// CheckStockServlet.java
@WebServlet("/checkStock")
public class ServletCheckStock extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Set<Integer> productIds = mapper.readValue(request.getInputStream(), new TypeReference<Set<Integer>>() {});

            Map<Integer, Boolean> stockStatus = productDAO.checkStockStatus(productIds);

            response.setContentType("application/json");
            mapper.writeValue(response.getWriter(), stockStatus);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"库存检查失败\"}");
        }
    }
}