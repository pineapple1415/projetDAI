package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

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

    // 修改后的构造函数
    public Composer(Commande commande, Produit produit, Integer quantite) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Composer composer = (Composer) o;
        return Objects.equals(id, composer.id) && Objects.equals(commande, composer.commande) && Objects.equals(produit, composer.produit) && Objects.equals(quantite, composer.quantite);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, commande, produit, quantite);
    }
}
