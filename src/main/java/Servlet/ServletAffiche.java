package Servlet;

import DAO.ProductDAO;
import com.google.gson.Gson;
import model.Produit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/products")  // 访问URL
public class ServletAffiche extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO(); // 实例化 DAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Produit> products = productDAO.getAllProducts();  // 获取产品列表
        String json = new Gson().toJson(products); // 转换成 JSON

        response.getWriter().write(json); // 返回 JSON 响应
    }
}

