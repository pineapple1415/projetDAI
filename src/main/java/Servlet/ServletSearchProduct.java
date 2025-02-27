package Servlet;

import DAO.ProductDAO;
import model.Produit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        System.out.println("servlet recherche used"); // 添加日志

        String searchQuery = request.getParameter("nomProduit");
        System.out.println("Recherche : " + searchQuery);

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index"); // ✅ 使用 request.getContextPath() 获取根路径
            return;
        }

        // 使用 DAO 查询产品
        List<Produit> produits = productDAO.searchProducts(searchQuery);

        // 传递结果到 JSP 页面
        request.setAttribute("produits", produits);
        request.getRequestDispatcher("jsp/articleMotCle.jsp").forward(request, response);
    }
}
