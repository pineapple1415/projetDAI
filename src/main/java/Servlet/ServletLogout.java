
package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class ServletLogout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取当前 Session
        HttpSession session = request.getSession(false); // 避免创建新的 Session
        if (session != null) {
            session.invalidate(); // 清除 Session
        }

        // 2. 重定向到 login.jsp 页面
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }
}
