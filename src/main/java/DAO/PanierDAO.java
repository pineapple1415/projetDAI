package DAO;

import model.panier;
import model.produit;
import model.User;
import model.produitDansPanier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class PanierDAO {
    public void addItemToUserCart(User user, Long productId, String productName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // 获取或创建购物车
            panier cart = session.createQuery("FROM panier WHERE user = :user", panier.class)
                    .setParameter("user", user)
                    .uniqueResultOptional()
                    .orElseGet(() -> {
                        panier newCart = new panier();
                        newCart.setUser(user);
                        session.persist(newCart);
                        return newCart;
                    });

            // 添加购物车项
            produitDansPanier item = new produitDansPanier();
            item.setPanier(cart);
            item.setIdProduit(productId);
            item.setNomProduit(productName);
            session.persist(item);

            tx.commit();
        }
    }
}