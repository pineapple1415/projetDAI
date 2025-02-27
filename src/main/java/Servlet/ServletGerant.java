package Servlet;

import DAO.CategorieDAO;
import DAO.RayonDAO;
import DAO.FournisseurDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/gerant")
public class ServletGerant extends HttpServlet {

    private final CategorieDAO categorieDAO = new CategorieDAO();
    private final RayonDAO rayonDAO = new RayonDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO(); // 补充声明

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Gerant gerant = (Gerant) session.getAttribute("user");

        if (gerant == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            // 默认访问gerant页面，加载categories, rayons 和 fournisseurs 数据
            List<Categorie> categories = categorieDAO.getAllCategories();
            List<Rayon> rayons = rayonDAO.getAllRayons();
            List<Fournisseur> fournisseurs = fournisseurDAO.getAllFournisseurs();

            request.setAttribute("categories", categories);
            request.setAttribute("rayons", rayons);
            request.setAttribute("fournisseurs", fournisseurs);

            request.getRequestDispatcher("/jsp/gerant.jsp").forward(request, response);
            return;
        }

// 如果action不为空，则进行后续switch操作
        String resultat;

        switch (action) {
            case "importer":
                resultat = "<p>📥 Fonctionnalité d'importation à compléter.</p>";
                break;
            case "etatStock":
                resultat = "<p>📊 État du stock à compléter.</p>";
                break;
            case "efficacite":
                resultat = "<p>⏳ Vérification efficacité à compléter.</p>";
                break;
            case "statistiques":
                resultat = "<p>📈 Édition statistiques à compléter.</p>";
                break;
            case "recommandation":
                resultat = "<p>🔧 Gestion recommandations à compléter.</p>";
                break;
            case "profils":
                resultat = "<p>👤 Consultation profils à compléter.</p>";
                break;
            case "habitudes":
                resultat = "<p>🕵️ Détection habitudes à compléter.</p>";
                break;
            default:
                resultat = "<p>⚠️ Action inconnue.</p>";
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(resultat);

    }
}