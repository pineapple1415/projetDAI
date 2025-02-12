package util;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.*;

public class InsertData {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            /** ⿡ 插入 10 个供应商 */
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

            /** ⿢ 插入 10 个类别 */
            List<Categorie> categories = Arrays.asList(
                    new Categorie("Électronique"), new Categorie("Vêtements"),
                    new Categorie("Alimentation"), new Categorie("Meubles"),
                    new Categorie("Cosmétiques"), new Categorie("Sport"),
                    new Categorie("Automobile"), new Categorie("Livres"),
                    new Categorie("Jouets"), new Categorie("Bricolage")
            );
            categories.forEach(session::save);

            /** ⿣ 插入 10 个货架 */
            List<Rayon> rayons = Arrays.asList(
                    new Rayon("Téléviseurs"), new Rayon("Chaussures"),
                    new Rayon("Produits laitiers"), new Rayon("Salles de bain"),
                    new Rayon("Maquillage"), new Rayon("Vélos"),
                    new Rayon("Voitures"), new Rayon("Romans"),
                    new Rayon("Jeux pour enfants"), new Rayon("Outils de bricolage")
            );
            rayons.forEach(session::save);

            /** ⿤ 插入 10 个商店 */
            List<Magasin> magasins = Arrays.asList(
                    new Magasin("Carrefour", "Paris", "0145678901"),
                    new Magasin("Auchan", "Lille", "0320456789"),
                    new Magasin("Leclerc", "Lyon", "0478654321"),
                    new Magasin("Intermarché", "Toulouse", "0567890123"),
                    new Magasin("Super U", "Nantes", "0245678901"),
                    new Magasin("Amazon France", "Online", "N/A"),
                    new Magasin("Fnac", "Marseille", "0491012345"),
                    new Magasin("Lidl", "Bordeaux", "0556789012"),
                    new Magasin("Decathlon", "Lille", "0321234567"),
                    new Magasin("Castorama", "Nice", "0493012345")
            );
            magasins.forEach(session::save);

            /** ⿥ 插入 10 个客户 */
            List<User> users = Arrays.asList(
                    new User("Jean", "Dupont", "Paris", "75001", "jean@email.com", "0601020304", "jeanDup", "password1","Client"),
                    new User("Marie", "Curie", "Lyon", "75002", "marie@email.com", "0611223344", "marieCurie", "password2","Client"),
                    new User("Paul", "Durand", "Marseille", "75003", "paul@email.com", "0622334455", "paulDurand", "password3","Client"),
                    new User("Sophie", "Bernard", "Toulouse", "75004", "sophie@email.com", "0633445566", "sophieB", "password4","Client"),
                    new User("Luc", "Morel", "Nantes", "75005", "luc@email.com", "0644556677", "lucMorel", "password5","Client")
            );
            users.forEach(session::save);

            /** ⿦ 插入 10 个产品 */
            List<Produit> produits = Arrays.asList(
                    new Produit("iPhone 15", 999.99, "USA", "6.7 pouces", "Smartphone Apple",
                            "https://www.apple.com/v/iphone-15/a/images/overview/design/iphone_15_color.jpg",
                            fournisseurs.get(7), rayons.get(0), categories.get(0)),

                    new Produit("MacBook Pro", 2499.99, "USA", "16 pouces", "Ordinateur portable Apple",
                            "https://www.apple.com/v/macbook-pro/a/images/overview/hero/macbook_pro_16.jpg",
                            fournisseurs.get(7), rayons.get(0), categories.get(0)),

                    new Produit("Nike Air Max", 129.99, "Chine", "42", "Chaussures de sport",
                            "https://www.nike.com/images/air-max.jpg",
                            fournisseurs.get(1), rayons.get(1), categories.get(1))
            );


            /** ⿧ 插入 10 个订单 */
            List<Commande> commandes = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                commandes.add(new Commande((i + 1) * 100.0, new Date(), Statut.PAYEE, users.get(i % users.size()), magasins.get(i % magasins.size())));
            }
            commandes.forEach(session::save);

            /** ⿨ 插入 10 个订单详情（Composer），确保 1 个订单包含 1-3 个产品 */
            for (Commande commande : commandes) {
                for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
                    session.save(new Composer(commande, produits.get(i % produits.size()), new Random().nextInt(5) + 1));
                }
            }

            /** ⿩ 插入 10 个购物车 */
            List<Course> courses = new ArrayList<>();
            for (User user : users) {
                courses.add(new Course(user));
            }
            courses.forEach(session::save);

            /** 🔟 插入 10 个购物车详情（Ajouter），确保 1 个购物车包含 1-3 个产品 */
            for (Course course : courses) {
                for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
                    session.save(new Ajouter(course, produits.get(i % produits.size()), new Random().nextInt(5) + 1));
                }
            }

            /** 1⿡ 插入 10 个库存（Stocker），确保 1 个商店包含多个产品 */
            for (Magasin magasin : magasins) {
                for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
                    session.save(new Stocker(produits.get(i % produits.size()), magasin, new Random().nextInt(100) + 1));
                }
            }

            /** 1⿢ 插入 10 个商店-货架关系（Posseder），确保 1 个商店有 2-4 个货架 */
            for (Magasin magasin : magasins) {
                for (int i = 0; i < new Random().nextInt(3) + 2; i++) {
                    session.save(new Posseder(rayons.get(i % rayons.size()), magasin));
                }
            }

            transaction.commit();
            System.out.println("✅ 所有表格已成功填充 10 条数据！");

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            System.out.println("❌ 数据插入失败！");
        } finally {
            session.close();
        }
    }
}