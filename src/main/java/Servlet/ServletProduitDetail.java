package Servlet;

import DAO.ProductDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/produitDetail")
public class ServletProduitDetail extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String nomProduit = request.getParameter("nomProduit");

        if (nomProduit == null || nomProduit.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nom du produit est requis.");
            return;
        }

        Produit produit = productDAO.getProduitByNom(nomProduit);

        if (produit == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produit introuvable.");
            return;
        }

        Map<String, Object> produitData = new HashMap<>();
        produitData.put("idProduit", ((model.Produit) produit).getIdProduit());
        produitData.put("nomProduit", produit.getNomProduit());
        produitData.put("descriptionProduit", produit.getDescriptionProduit());
        produitData.put("origineProduit", produit.getOrigineProduit());
        produitData.put("prixUnit", produit.getPrixUnit());
        produitData.put("tailleProduit", produit.getTailleProduit());
        produitData.put("image", produit.getImageUrl());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(produitData);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}

