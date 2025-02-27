package Servlet;

import DAO.StatistiqueDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/statistiques")
public class ServletStatistiques extends HttpServlet {
    private final StatistiqueDAO statistiqueDAO = new StatistiqueDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // 返回纯文本格式
        response.setCharacterEncoding("UTF-8");

        String type = request.getParameter("type");
        List<Object[]> data = null;

        switch (type) {
            case "customerPurchaseDistribution":
                data = statistiqueDAO.getCustomerPurchaseDistribution();
                break;
            case "salesPerProduct":
                data = statistiqueDAO.getSalesPerProductFromOrders();
                break;
            case "stockPerProduct":
                data = statistiqueDAO.getStockPerProduct();
                break;
            case "salesPerCategory":
                int rayonId = request.getParameter("rayonId") != null ? Integer.parseInt(request.getParameter("rayonId")) : 0;
                data = statistiqueDAO.getSalesPerCategoryInRayon(rayonId);
                break;
            case "salesPerRayon": // 🔥 新增支持 Rayon 的统计
                data = statistiqueDAO.getSalesPerRayon();
                break;
            default:
                response.getWriter().write("error: Invalid statistic type");
                return;
        }

        if (data == null || data.isEmpty()) {
            response.getWriter().write("error: No data available");
            return;
        }

        PrintWriter out = response.getWriter();
        StringBuilder labels = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Object[] row : data) {
            labels.append(row[0]).append(",");
            values.append(row[1]).append(",");
        }

        if (labels.length() > 0) labels.setLength(labels.length() - 1);
        if (values.length() > 0) values.setLength(values.length() - 1);

        out.println(labels);
        out.println(values);
    }
}
