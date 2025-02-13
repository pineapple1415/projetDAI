package util;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class InsertData {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            /** 1️⃣ 插入 10 个供应商 */
            List<Fournisseur> fournisseurs = Arrays.asList(
                    new Fournisseur("Samsung", "Seoul, South Korea", "contact@samsung.com"),
                    new Fournisseur("Nike", "USA", "support@nike.com"),
                    new Fournisseur("Nestlé", "Switzerland", "info@nestle.com"),
                    new Fournisseur("Sony", "Japan", "customer@sony.com"),
                    new Fournisseur("L’Oréal", "France", "contact@loreal.com"),
                    new Fournisseur("IKEA", "Sweden", "support@ikea.com"),
                    new Fournisseur("Toyota", "Japan", "service@toyota.com"),
                    new Fournisseur("Apple", "USA", "support@apple.com"),
                    new Fournisseur("Zara", "Spain", "info@zara.com"),
                    new Fournisseur("Adidas", "Germany", "support@adidas.com")
            );
            fournisseurs.forEach(session::save);

            /** 2️⃣ 插入 10 个类别 */
            List<Categorie> categories = Arrays.asList(
                    new Categorie("Électronique"), new Categorie("Vêtements"),
                    new Categorie("Alimentation"), new Categorie("Meubles"),
                    new Categorie("Cosmétiques"), new Categorie("Sport"),
                    new Categorie("Automobile"), new Categorie("Livres"),
                    new Categorie("Jouets"), new Categorie("Bricolage")
            );
            categories.forEach(session::save);

            /** 3️⃣ 插入 10 个货架 */
            List<Rayon> rayons = Arrays.asList(
                    new Rayon("Téléviseurs"), new Rayon("Chaussures"),
                    new Rayon("Produits laitiers"), new Rayon("Salles de bain"),
                    new Rayon("Maquillage"), new Rayon("Vélos"),
                    new Rayon("Voitures"), new Rayon("Romans"),
                    new Rayon("Jeux pour enfants"), new Rayon("Outils de bricolage")
            );
            rayons.forEach(session::save);

            /** 4️⃣ 插入 5 个商店 */
            List<Magasin> magasins = Arrays.asList(
                    new Magasin("Carrefour", "Paris", "0145678901"),
                    new Magasin("Auchan", "Lille", "0320456789"),
                    new Magasin("Leclerc", "Lyon", "0478654321"),
                    new Magasin("Intermarché", "Toulouse", "0567890123"),
                    new Magasin("Super U", "Nantes", "0245678901")
            );
            magasins.forEach(session::save);

            /** 5️⃣ 插入 5 个客户 */
            List<Client> clients = Arrays.asList(
                    new Client("Jean", "Dupont", "jean@email.com", "password1", "Paris", "75001", "0601020304"),
                    new Client("Marie", "Curie", "marie@email.com", "password2", "Lyon", "69002", "0611223344"),
                    new Client("Paul", "Durand", "paul@email.com", "password3", "Marseille", "13003", "0622334455"),
                    new Client("Sophie", "Bernard", "sophie@email.com", "password4", "Toulouse", "31004", "0633445566"),
                    new Client("Luc", "Morel", "luc@email.com", "password5", "Nantes", "44005", "0644556677")
            );
            clients.forEach(session::save);

            /** 6️⃣ 插入 2 个 Gerant（经理） */
            List<Gerant> gerants = Arrays.asList(
                    new Gerant("Michel", "Martin", "michel@email.com", "password6"),
                    new Gerant("Elise", "Dubois", "elise@email.com", "password7")
            );
            gerants.forEach(session::save);

            /** 7️⃣ 插入 5 个 préparateurs（准备员） */
            List<Preparateur> preparateurs = Arrays.asList(
                    new Preparateur("Thomas", "Lemoine", "thomas@email.com", "password8"),
                    new Preparateur("Camille", "Girard", "camille@email.com", "password9"),
                    new Preparateur("Alexandre", "Moreau", "alexandre@email.com", "password10"),
                    new Preparateur("Emma", "Roux", "emma@email.com", "password11"),
                    new Preparateur("Nathan", "Fournier", "nathan@email.com", "password12")
            );
            preparateurs.forEach(session::save);

            /** 8️⃣ 插入 10 个产品 */
            List<Produit> produits = Arrays.asList(
                    new Produit("iPhone 15", 999.99, "USA", "6.7 pouces", "Smartphone Apple",
                            "https://res.cloudinary.com/mozillion/image/upload/f_auto,q_auto/v1694595603/hmcmbp5eyh41hrrcw1qt.png",
                            fournisseurs.get(7), rayons.get(0), categories.get(0)),

                    new Produit("MacBook Pro", 2499.99, "USA", "16 pouces", "Ordinateur portable Apple",
                            "https://th.bing.com/th/id/R.71c92a552a4481a5294785b0ec187a3f?rik=YrX3E7PfsRj8cQ&pid=ImgRaw&r=0",
                            fournisseurs.get(7), rayons.get(0), categories.get(0)),

                    new Produit("Nike Air Max", 129.99, "Chine", "42", "Chaussures de sport",
                            "https://th.bing.com/th/id/R.e92c524b033c17d52b1ce6821e890d41?rik=73pAa9GqBI2h%2bg&riu=http%3a%2f%2fwww.authentkicks.com%2fwp-content%2fuploads%2f2015%2f06%2fIMG_6168.jpg&ehk=V1yN0NNnsA1MhVZqoEZ8AbZeGQTYJIIsmG%2fuhVtImYw%3d&risl=1&pid=ImgRaw&r=0",
                            fournisseurs.get(1), rayons.get(1), categories.get(1))
            );
            produits.forEach(session::save);

            /** 9️⃣ 插入 5 个订单 */
            List<Commande> commandes = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                commandes.add(new Commande((i + 1) * 100.0, new Date(), Statut.PAYEE, clients.get(i), preparateurs.get(i % preparateurs.size()), magasins.get(i % magasins.size())));
            }
            commandes.forEach(session::save);

            /** 🔟 插入 5 个购物车 */
            List<Course> courses = new ArrayList<>();
            for (Client client : clients) {
                courses.add(new Course(client));
            }
            courses.forEach(session::save);

            /** 🔟 插入 5 个购物车详情（Ajouter） */
            for (Course course : courses) {
                for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
                    session.save(new Ajouter(course, produits.get(i % produits.size()), new Random().nextInt(5) + 1));
                }
            }

            transaction.commit();
            System.out.println("✅ 所有表格已成功填充数据！");

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            System.out.println("❌ 数据插入失败！");
        } finally {
            session.close();
        }
    }
}
