package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Composer")
public class Composer {

    @EmbeddedId
    private ComposerId id;  // 使用复合主键

    @ManyToOne
    @MapsId("idCommande")  // 复合主键的一部分
    @JoinColumn(name = "idCommande")
    @JsonBackReference
    private Commande commande;

    @ManyToOne
    @MapsId("idProduit")  // 复合主键的一部分
    @JoinColumn(name = "idProduit")
    @JsonBackReference
    private Produit produit;

    private Integer quantite;

    public Composer() {}

    public Composer(Commande commande, Produit produit, Integer quantite) {
        this.id = new ComposerId(commande.getIdCommande(), produit.getIdProduit());
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
    }

    public ComposerId getId() {
        return id;
    }

    public void setId(ComposerId id) {
        this.id = id;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
