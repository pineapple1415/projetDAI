package model;


public class ProduitDansPanier {

    private Long id;

    private Panier panier;

    private Produit produit;

    private Integer quantity = 1;

    private boolean available = true;

    public boolean isAvailable() {
        return available;
    }

    // Getters/Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}