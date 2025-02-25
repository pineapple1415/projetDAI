package Servlet;

import DAO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;

// ServletCourse.java
@WebServlet("/course")
public class ServletCourse extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            List<Course> courses = new CourseDAO().getCoursesByUser(currentUser);

            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 配置Hibernate模块处理懒加载
            Hibernate5Module module = new Hibernate5Module();
            module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
            mapper.registerModule(module);

            mapper.writeValue(response.getWriter(), courses);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"数据加载失败\"}");
        }
    }
}
