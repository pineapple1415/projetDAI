package Servlet;

import DAO.StatistiqueDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/stockPrevu")
public class ServletStockPrevu extends HttpServlet {
    private final StatistiqueDAO statistiqueDAO = new StatistiqueDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        List<Object[]> stockPrevuData = statistiqueDAO.getStockForNext7Days();

        PrintWriter out = response.getWriter();
        StringBuilder labels = new StringBuilder();
        StringBuilder stockActuel = new StringBuilder();
        StringBuilder ventesEstimees = new StringBuilder();
        StringBuilder stockPrevu = new StringBuilder();

        for (Object[] row : stockPrevuData) {
            labels.append(row[0]).append(",");
            stockActuel.append(row[1]).append(",");
            ventesEstimees.append(row[2]).append(",");
            stockPrevu.append(row[3]).append(",");
        }

        if (labels.length() > 0) labels.setLength(labels.length() - 1);
        if (stockActuel.length() > 0) stockActuel.setLength(stockActuel.length() - 1);
        if (ventesEstimees.length() > 0) ventesEstimees.setLength(ventesEstimees.length() - 1);
        if (stockPrevu.length() > 0) stockPrevu.setLength(stockPrevu.length() - 1);

        out.println(labels);
        out.println(stockActuel);
        out.println(ventesEstimees);
        out.println(stockPrevu);
    }
}
