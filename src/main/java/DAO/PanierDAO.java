package DAO;

import model.Panier;
import model.Produit;
import model.User;
import model.ProduitDansPanier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PanierDAO {
    public void addItemToPanier(User user, Long productId, int quantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // 获取或创建购物车
            Panier panier = session.createQuery("FROM Panier WHERE user = :user", Panier.class)
                    .setParameter("user", user)
                    .uniqueResultOptional()
                    .orElseGet(() -> {
                        Panier newPanier = new Panier();
                        newPanier.setUser(user);
                        session.persist(newPanier);
                        return newPanier;
                    });

            // 查找现有商品项
            ProduitDansPanier existingItem = session.createQuery(
                            "FROM ProduitDansPanier WHERE Panier = :Panier AND Produit.id = :productId",
                            ProduitDansPanier.class)
                    .setParameter("Panier", panier)
                    .setParameter("productId", productId)
                    .uniqueResultOptional()
                    .orElse(null);

            if (existingItem != null) {
                // 更新数量
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                // 新增商品项
                Produit product = session.get(Produit.class, productId);
                ProduitDansPanier newItem = new ProduitDansPanier();
                newItem.setPanier(panier);
                newItem.setProduit(product);
                newItem.setQuantity(quantity);
                session.persist(newItem);
            }

            tx.commit();
        }
    }

    public void updateCartItem(User user, Long productId, int newQuantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // 获取购物车项
            ProduitDansPanier item = session.createQuery(
                            "FROM ProduitDansPanier WHERE panier.user = :user AND produit.id = :productId",
                            ProduitDansPanier.class)
                    .setParameter("user", user)
                    .setParameter("productId", productId)
                    .uniqueResult();

            if (item != null) {
                item.setQuantity(newQuantity);
                session.merge(item);
            }

            tx.commit();
        }
    }

    public void viderPanier(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            int userId = user.getIdUser();
            // 获取用户所有购物车项
            List<ProduitDansPanier> items = session.createQuery(
                            "FROM ProduitDansPanier WHERE panier.utilisateur.idUtilisateur = :userId",
                            ProduitDansPanier.class)
                    .setParameter("userId", userId)
                    .list();

            // 批量删除操作
            for (ProduitDansPanier item : items) {
                session.remove(item); // 使用remove替代delete
            }

            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("清空购物车失败", e);
        }
    }

}