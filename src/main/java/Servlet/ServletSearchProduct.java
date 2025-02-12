package Servlet;

import DAO.ProductDAO;
import model.Produit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/searchProduct")
public class ServletSearchProduct extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;  // 修正类名

    @Override
    public void init() {
        productDAO = new ProductDAO(); // 初始化DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("nomProduit");
        System.out.println("Recherche : " + searchQuery);


        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            request.setAttribute("error", "Aucun mot-clé de recherche fourni.");
            request.getRequestDispatcher("articleMotCle.jsp").forward(request, response);
            return;
        }

        // 使用 DAO 查询产品
        List<Produit> produits = productDAO.searchProducts(searchQuery);

        // 传递结果到 JSP 页面
        request.setAttribute("produits", produits);
        request.getRequestDispatcher("jsp/articleMotCle.jsp").forward(request, response);
    }
}
