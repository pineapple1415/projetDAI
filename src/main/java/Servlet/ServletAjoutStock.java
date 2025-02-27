package Servlet;

import DAO.StockDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/ajouterStock")
public class ServletAjoutStock extends HttpServlet {
    private final StockDAO stockDAO = new StockDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String type = request.getParameter("type");
        StringBuilder responseText = new StringBuilder();

        try {
            if ("magasins".equals(type)) {  // ✅ 添加 magasins 逻辑
                List<Magasin> magasins = stockDAO.getAllMagasins();
                System.out.println("Nombre de magasins récupérés: " + magasins.size());

                for (Magasin magasin : magasins) {
                    responseText.append(magasin.getIdMagasin())
                            .append(",")
                            .append(magasin.getNomMagasin())
                            .append("\n");
                }
            } else if ("rayons".equals(type)) {
                List<Rayon> rayons = stockDAO.getAllRayons();
                for (Rayon rayon : rayons) {
                    responseText.append(rayon.getIdRayon()).append(",").append(rayon.getNomRayon()).append("\n");
                }
            } else if ("categories".equals(type)) {
                int rayonId = Integer.parseInt(request.getParameter("rayonId"));
                List<Categorie> categories = stockDAO.getCategoriesByRayon(rayonId);
                for (Categorie categorie : categories) {
                    responseText.append(categorie.getIdCategorie()).append(",").append(categorie.getNomCategorie()).append("\n");
                }
            } else if ("produits".equals(type)) {
                int categorieId = Integer.parseInt(request.getParameter("categorieId"));
                List<Produit> produits = stockDAO.getProduitsByCategorie(categorieId);
                for (Produit produit : produits) {
                    responseText.append(produit.getIdProduit()).append(",").append(produit.getNomProduit()).append("\n");
                }
            }

            response.getWriter().write(responseText.toString());
        } catch (Exception e) {
            response.getWriter().write("Erreur: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        try {
            int magasinId = Integer.parseInt(request.getParameter("magasin"));
            int produitId = Integer.parseInt(request.getParameter("produit"));
            int quantite = Integer.parseInt(request.getParameter("quantite"));

            stockDAO.ajouterStock(magasinId, produitId, quantite);
            response.getWriter().write("Stock ajouté avec succès !");
        } catch (Exception e) {
            response.getWriter().write("Erreur: " + e.getMessage());
        }
    }
}

