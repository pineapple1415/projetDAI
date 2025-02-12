package DAO;

import model.panier;
import model.produit;
import model.User;
import model.produitDansPanier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class PanierDAO {
    public void addItemToPanier(User user, Long productId, int quantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // 获取或创建购物车
            panier panier = session.createQuery("FROM panier WHERE user = :user", panier.class)
                    .setParameter("user", user)
                    .uniqueResultOptional()
                    .orElseGet(() -> {
                        panier newPanier = new panier();
                        newPanier.setUser(user);
                        session.persist(newPanier);
                        return newPanier;
                    });

            // 查找现有商品项
            produitDansPanier existingItem = session.createQuery(
                            "FROM produitDansPanier WHERE panier = :panier AND produit.id = :productId",
                            produitDansPanier.class)
                    .setParameter("panier", panier)
                    .setParameter("productId", productId)
                    .uniqueResultOptional()
                    .orElse(null);

            if (existingItem != null) {
                // 更新数量
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                // 新增商品项
                produit product = session.get(produit.class, productId);
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