package Servlet;

import DAO.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getConsumerStats")
public class ServletConsumerStats extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDAO UserDAO = new UserDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String type = request.getParameter("type");

        Map<String, Object> responseData = new HashMap<>();

        if (type != null) {
            List<String> labels;
            List<Integer> values;

            switch (type) {
                case "regularCustomers":
                    labels = UserDAO.getRegularCustomerLabels();
                    values = UserDAO.getRegularCustomerData();
                    break;
                case "highValueCustomers":
                    labels = UserDAO.getHighValueCustomerLabels();
                    values = UserDAO.getHighValueCustomerData();
                    break;
                case "newCustomers":
                    labels = UserDAO.getNewCustomerLabels();
                    values = UserDAO.getNewCustomerData();
                    break;
                case "inactiveCustomers":
                    labels = UserDAO.getInactiveCustomerLabels();
                    values = UserDAO.getInactiveCustomerData();
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type de statistiques inconnu");
                    return;
            }

            responseData.put("labels", labels);
            responseData.put("values", values);
            objectMapper.writeValue(response.getWriter(), responseData);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type de statistiques non fourni");
        }
    }
}
