package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Panier {
    private Set<Produit> items = new HashSet<>();
    private User user;
    public Panier() {}

    public Panier(Set<Produit> items) {
        this.items = items;
    }

    // 获取购物车中的商品
    public Set<Produit> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) { // 👈 添加这个方法
        this.user = user;
    }

    // 添加商品
    public void addProduit(Produit produit) {
        items.add(produit);
    }

    // 删除商品
    public void removeProduit(Produit produit) {
        items.remove(produit);
    }

    // 清空购物车
    public void clearPanier() {
        items.clear();
    }

    // 计算购物车中的商品数量
    public int getItemCount() {
        return items.size();
    }

    // 设置商品列表（用于恢复会话中的购物车）
    public void setItems(Set<Produit> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Panier panier = (Panier) o;
        return Objects.equals(items, panier.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return "Panier{" +
                "items=" + items +
                '}';
    }
}
