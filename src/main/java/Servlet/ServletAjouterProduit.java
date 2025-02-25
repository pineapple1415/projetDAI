package Servlet;

import DAO.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.net.URLDecoder;

@WebServlet("/ajouterProduit")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class ServletAjouterProduit extends HttpServlet {

    private final ProductDAO produitDAO = new ProductDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private final RayonDAO rayonDAO = new RayonDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 获取基本信息
        String nomProduit = request.getParameter("nomProduit");
        double prix = Double.parseDouble(request.getParameter("prix"));
        String origine = request.getParameter("origine");
        String taille = request.getParameter("taille");
        String description = request.getParameter("description");
        String promoStr = request.getParameter("promotion");
        double promotion = promoStr == null || promoStr.isEmpty() ? 0.0 : Double.parseDouble(promoStr) / 100;

        // 图片上传 (修改为相对路径到 static/img 文件夹)
        Part filePart = request.getPart("imageProduit");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String projectRoot = System.getProperty("project.root");
        if (projectRoot == null) {
            projectRoot = new File("").getAbsolutePath();  // 如果没设置，则回退
        }
        String uploadPath = projectRoot + "/src/main/webapp/static/img";

        System.out.println("图片存储路径: " + uploadPath);


        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        filePart.write(uploadPath + File.separator + fileName);

        String imageUrl = "static/img/" + fileName;


        // Fournisseur处理逻辑
        Fournisseur fournisseur;
        if ("nouveau".equals(request.getParameter("fournisseur"))) {
            fournisseur = new Fournisseur(
                    request.getParameter("nouveauNomFournisseur"),
                    request.getParameter("adresseFournisseur"),
                    request.getParameter("contactFournisseur")
            );
            fournisseurDAO.saveFournisseur(fournisseur);
        } else {
            fournisseur = fournisseurDAO.getFournisseurById(
                    Integer.parseInt(request.getParameter("fournisseur"))
            );
        }

        // Categorie处理逻辑
        Categorie categorie;
        if ("nouveau".equals(request.getParameter("categorie"))) {
            String nomNouvelleCategorie = request.getParameter("nouvelleCategorie");

            // 处理Rayon
            Rayon rayon;
            if ("nouveau".equals(request.getParameter("rayon"))) {
                String nomNouveauRayon = request.getParameter("nouveauRayon");
                rayon = new Rayon(nomNouveauRayon);
                rayonDAO.saveRayon(rayon);
            } else {
                rayon = rayonDAO.getRayonById(Integer.parseInt(request.getParameter("rayon")));
            }

            categorie = new Categorie(nomNouvelleCategorie, rayon);
            categorieDAO.saveCategorie(categorie);
        } else {
            categorie = categorieDAO.getCategorieById(Integer.parseInt(request.getParameter("categorie")));
        }

        // 创建Produit对象
        Produit produit = new Produit(nomProduit, prix, origine, taille, description, imageUrl,
                fournisseur, categorie, promotion);

        produitDAO.addProduit(produit);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("Produit ajouté avec succès : " + produit.getNomProduit());
    }
}
