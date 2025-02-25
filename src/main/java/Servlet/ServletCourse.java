package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import DAO.CourseDAO;
import model.Course;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/courses")
public class ServletCourse extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("pageLogin");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if ("getCourses".equals(action)) {
            // 获取当前用户的所有 Courses
            List<Course> courses = courseDAO.getCoursesByUser(user);

            // 构建 JSON 响应，包含 produit 信息
            List<Map<String, Object>> courseList = new ArrayList<>();
            for (Course course : courses) {
                Map<String, Object> courseData = new HashMap<>();
                courseData.put("idCourse", course.getIdCourse());
                courseData.put("texte", course.getTexte());

                // 获取当前 Course 关联的 produits
                Map<String, String> produits = courseDAO.getProduitsByUserAndCourse(course.getIdCourse());
                courseData.put("produits", produits);

                courseList.add(courseData);
            }

            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(courseList));
        }
    }
}
