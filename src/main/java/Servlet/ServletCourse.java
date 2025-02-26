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
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non connecté");
            return;
        }

        String action = request.getParameter("action");

        if ("getCourses".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            User user = (User) session.getAttribute("user");
            List<Course> courses = courseDAO.getCoursesByUser(user);

            // 只返回 idCourse 和 texte
            List<Map<String, Object>> result = new ArrayList<>();
            for (Course course : courses) {
                Map<String, Object> courseData = new HashMap<>();
                courseData.put("idCourse", course.getIdCourse());
                courseData.put("texte", course.getTexte());
                result.add(courseData);
            }

            response.getWriter().write(objectMapper.writeValueAsString(result));
        } else if ("getProducts".equals(action)) {
            String idCourseParam = request.getParameter("idCourse");

            if (idCourseParam == null || !idCourseParam.matches("\\d+")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idCourse invalide");
                return;
            }

            int idCourse = Integer.parseInt(idCourseParam);
            List<Map<String, Object>> produits = courseDAO.getProduitsWithQuantity(idCourse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(produits));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action non reconnue");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non connecté");
            return;
        }

        String action = request.getParameter("action");
        ObjectMapper objectMapper = new ObjectMapper();
        CourseDAO courseDAO = new CourseDAO();

        if ("createCourse".equals(action)) {
            // ✅ 处理创建 Course
            Map<String, String> requestBody = objectMapper.readValue(request.getReader(), Map.class);
            String texte = requestBody.get("texte");

            if (texte == null || texte.trim().isEmpty()) {
                texte = "Sans description"; // 默认值
            }

            User user = (User) session.getAttribute("user");
            Course newCourse = new Course(user, texte);
            courseDAO.createCourse(newCourse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(newCourse));

        } else if ("editCourse".equals(action)) {
            try {
                // 解析 JSON 请求体
                Map<String, Object> requestBody = objectMapper.readValue(request.getReader(), Map.class);

                // 确保 `idCourse` 和 `texte` 存在
                if (!requestBody.containsKey("idCourse") || !requestBody.containsKey("texte")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données manquantes");
                    return;
                }

                int idCourse;
                try {
                    // 如果 `idCourse` 是 `Integer`，直接转换，否则解析为整数
                    Object idCourseObj = requestBody.get("idCourse");
                    if (idCourseObj instanceof Integer) {
                        idCourse = (Integer) idCourseObj;
                    } else {
                        idCourse = Integer.parseInt(idCourseObj.toString());
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Course invalide");
                    return;
                }

                // 解析 texte
                Object texteObj = requestBody.get("texte");
                String texte = (texteObj != null) ? texteObj.toString().trim() : "Sans description";
                if (texte.isEmpty()) {
                    texte = "Sans description"; // 默认值
                }

                // 调用 DAO 更新 Course
                boolean success = courseDAO.updateCourseTexte(idCourse, texte);
                if (!success) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Échec de la mise à jour");
                    return;
                }

                // 返回 JSON 响应
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\": \"Course modifié avec succès\"}");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur");
            }
        }else if ("updateQuantity".equals(action)) {
            try {
                // 解析 JSON 请求
                Map<String, Object> requestBody = objectMapper.readValue(request.getReader(), Map.class);

                // 调试日志，检查 requestBody
                System.out.println("Request Body: " + requestBody);

                // 检查 key 是否存在
                if (!requestBody.containsKey("courseId") || !requestBody.containsKey("produitId") || !requestBody.containsKey("newQty")) {
                    System.out.println("❌ Données manquantes: " + requestBody);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données manquantes");
                    return;
                }

                // 解析参数
                int courseId = (int) requestBody.get("courseId");
                int produitId = (int) requestBody.get("produitId");  // 确保使用 "produitId"
                int newQty = (int) requestBody.get("newQty");

                System.out.println("✅ Mise à jour de la quantité: courseId=" + courseId + ", produitId=" + produitId + ", newQty=" + newQty);

                boolean success;
                if (newQty == 0) {
                    success = courseDAO.removeProduitFromCourse(courseId, produitId);
                } else {
                    success = courseDAO.updateProduitQuantity(courseId, produitId, newQty);
                }

                if (!success) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Échec de la mise à jour");
                    return;
                }

                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Quantité mise à jour\"}");

            } catch (NullPointerException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données manquantes ou invalides");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur");
            }
        }else if ("addToCourse".equals(action)) {
            try {
                // 解析 JSON 请求
                Map<String, Object> requestBody = objectMapper.readValue(request.getReader(), Map.class);
                System.out.println("🔍 Request Body: " + requestBody);

                // 验证数据
                if (!requestBody.containsKey("idCourse") || !requestBody.containsKey("productId") || !requestBody.containsKey("quantity")) {
                    System.out.println("❌ Données manquantes: " + requestBody);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données manquantes");
                    return;
                }

                // **正确转换 String -> int**
                int idCourse = Integer.parseInt(requestBody.get("idCourse").toString());
                int productId = Integer.parseInt(requestBody.get("productId").toString());
                int quantity = Integer.parseInt(requestBody.get("quantity").toString());

                System.out.println("🟢 Ajout de produit au course: idCourse=" + idCourse + ", productId=" + productId + ", quantity=" + quantity);

                // 检查 `idCourse` 是否存在
                boolean courseExists = courseDAO.checkCourseExists(idCourse);
                if (!courseExists) {
                    System.out.println("❌ Course ID " + idCourse + " n'existe pas.");
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": false, \"message\": \"SVP créez ce course d'abord.\"}");
                    return;
                }

                // 添加到 `Ajouter` 表
                boolean success = courseDAO.addProduitToCourse(idCourse, productId, quantity);
                if (!success) {
                    System.out.println("❌ Échec de l'ajout au course.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Échec de l'ajout au course");
                    return;
                }

                System.out.println("✅ Produit ajouté au course avec succès !");
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true}");

            } catch (NumberFormatException e) {
                System.out.println("❌ Erreur de conversion String -> int");
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format invalide des données");
            } catch (Exception e) {
                System.out.println("❌ Erreur serveur:");
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur");
            }
        }


        else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action non reconnue");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non connecté");
            return;
        }

        String idCourseParam = request.getParameter("idCourse");
        if (idCourseParam == null || !idCourseParam.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Course invalide");
            return;
        }

        int idCourse = Integer.parseInt(idCourseParam);
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.deleteCourse(idCourse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Course supprimé avec succès\"}");
    }



}
