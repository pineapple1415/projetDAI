package DAO;

import model.produit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<produit> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from produit ", produit.class).list();
        }
    }

    public void addProduit(produit product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(product);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<produit> getAllProducts() {
        List<produit> products = new ArrayList<>();
        String sql = "SELECT idProduit, nomProduit, origineProduit, prixUnit, tailleProduit, descriptionProduit FROM produits";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getString("origineProduit"),
                        rs.getDouble("prixUnit"),
                        rs.getString("tailleProduit"),
                        rs.getString("descriptionProduit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }




}