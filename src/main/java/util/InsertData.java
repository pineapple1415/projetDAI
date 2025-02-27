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

            /** 3️⃣ 插入 10 个货架 */
            List<Rayon> rayons = Arrays.asList(
                    new Rayon("Électronique"),      // 0
                    new Rayon("Mode et Vêtements"), // 1
                    new Rayon("Alimentation"),      // 2
                    new Rayon("Meubles"),           // 3
                    new Rayon("Cosmétiques"),       // 4
                    new Rayon("Sports et Loisirs"), // 5
                    new Rayon("Automobile"),        // 6
                    new Rayon("Livres"),            // 7
                    new Rayon("Jouets"),            // 8
                    new Rayon("Bricolage")          // 9
            );
            rayons.forEach(session::save);

            /** 2️⃣ 插入 10 个类别 */
            List<Categorie> categories = Arrays.asList(
                    new Categorie("Smartphones", rayons.get(0)), // Électronique
                    new Categorie("Ordinateurs", rayons.get(0)), // Électronique
                    new Categorie("Vêtements Homme", rayons.get(1)), // Mode
                    new Categorie("Vêtements Femme", rayons.get(1)), // Mode
                    new Categorie("Produits Bio", rayons.get(2)), // Alimentation
                    new Categorie("Meubles de Salon", rayons.get(3)), // Meubles
                    new Categorie("Maquillage", rayons.get(4)), // Cosmétiques
                    new Categorie("Équipements de Sport", rayons.get(5)), // Sport
                    new Categorie("Voitures de Luxe", rayons.get(6)), // Automobile
                    new Categorie("Romans Policier", rayons.get(7)) // Livres
            );
            categories.forEach(session::save);






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
