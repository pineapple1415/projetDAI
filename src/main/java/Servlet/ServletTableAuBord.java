package Servlet;


import DAO.ProductDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/getPurchasedProducts")
public class ServletTableAuBord extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductDAO ProductDAO = new ProductDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(401);
                response.getWriter().write("{\"error\": \"用户未登录\"}");
                return;
            }

            User user = (User) session.getAttribute("user");
            int userId = (user.getIdUser() != null) ? user.getIdUser() : -1; // ✅ 防止 `NullPointerException`

            // **调用 DAO 获取已购产品**
            List<Integer> purchasedProductIds = ProductDAO.getPurchasedProductsByUser(userId);

            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), purchasedProductIds);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}

