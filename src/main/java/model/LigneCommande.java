package model;

import javax.persistence.*;

@Entity
@Table(name = "LigneCommande")
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLigneCommande")
    private Integer idLigneCommande;

    @Column(name = "quantite")
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "idProduit")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "idCommande")
    private Commande commande;

    public LigneCommande() {
    }

    public LigneCommande(Integer idLigneCommande, Integer quantite, Produit produit, Commande commande) {
        this.idLigneCommande = idLigneCommande;
        this.quantite = quantite;
        this.produit = produit;
        this.commande = commande;
    }

    public Integer getIdLigneCommande() {
        return idLigneCommande;
    }

    public void setIdLigneCommande(Integer idLigneCommande) {
        this.idLigneCommande = idLigneCommande;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}
