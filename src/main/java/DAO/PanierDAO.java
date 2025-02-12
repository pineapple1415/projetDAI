package DAO;

import model.Panier;
import model.Produit;
import model.produitDansPanier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

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
            produitDansPanier existingItem = session.createQuery(
                            "FROM produitDansPanier WHERE Panier = :Panier AND Produit.id = :productId",
                            produitDansPanier.class)
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
                produitDansPanier newItem = new produitDansPanier();
                newItem.setPanier(panier);
                newItem.setProduit(product);
                newItem.setQuantity(quantity);
                session.persist(newItem);
            }

            tx.commit();
        }
    }
}