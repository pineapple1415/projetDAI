package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProduit;

    private String nomProduit;
    private Double prixUnit;

    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "idRayon")
    private Rayon rayon;

    @ManyToOne
    @JoinColumn(name = "idCategorie")
    private Categorie categorie;

    /** 一对多：Produit 和 Composer（中间表，用于订单） */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Composer> composers = new HashSet<>();

    /** 多对多：Produit 和 Course（购物车） 通过 Ajouter 连接 */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ajouter> ajouts = new HashSet<>();

    /** 多对多：Produit 和 Magasin 通过 Stocker 连接 */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Stocker> stockers = new HashSet<>();

    public Produit() {}

    public Produit(String nomProduit, Double prixUnit, Fournisseur fournisseur, Rayon rayon, Categorie categorie) {
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.fournisseur = fournisseur;
        this.rayon = rayon;
        this.categorie = categorie;
    }

    public Integer getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Double getPrixUnit() {
        return prixUnit;
    }

    public void setPrixUnit(Double prixUnit) {
        this.prixUnit = prixUnit;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(Rayon rayon) {
        this.rayon = rayon;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<Ajouter> getAjouts() {
        return ajouts;
    }

    public void setAjouts(Set<Ajouter> ajouts) {
        this.ajouts = ajouts;
    }

    public Set<Stocker> getStockers() {
        return stockers;
    }

    public void setStockers(Set<Stocker> stockers) {
        this.stockers = stockers;
    }
}
