package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.CommandeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/performance")
public class ServletPerformance extends HttpServlet {
    private final CommandeDAO commandeDAO = new CommandeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // 纯文本格式
        response.setCharacterEncoding("UTF-8");

        String chartType = request.getParameter("chartType");
        if (chartType == null) {
            response.getWriter().write("error: Invalid chart type");
            return;
        }

        List<Object[]> data;
        if ("shoppingTime".equals(chartType)) {
            data = commandeDAO.getMonthlyShoppingTimeStats();
        } else if ("preparationTime".equals(chartType)) {
            data = commandeDAO.getMonthlyPreparationTimeStats();
        } else {
            response.getWriter().write("error: Invalid chart type");
            return;
        }

        PrintWriter out = response.getWriter();
        StringBuilder labels = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Object[] row : data) {
            labels.append(row[0]).append(",");
            values.append(row[1]).append(",");
        }

        // 去掉最后的逗号
        if (labels.length() > 0) labels.setLength(labels.length() - 1);
        if (values.length() > 0) values.setLength(values.length() - 1);

        // 返回数据（第一行是月份，第二行是数值）
        out.println(labels.toString());
        out.println(values.toString());
    }
}
