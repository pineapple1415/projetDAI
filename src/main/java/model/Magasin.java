package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Magasin")
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMagasin;

    private String nomMagasin;
    private String adresseMagasin;
    private String telMagasin;

    /** 多对多：Magasin 和 Rayon */
    @ManyToMany
    @JoinTable(name = "Posseder",
            joinColumns = @JoinColumn(name = "idMagasin"),
            inverseJoinColumns = @JoinColumn(name = "idRayon"))
    private Set<Rayon> rayons = new HashSet<>();

    /** 一对多：Magasin 和 Commande */
    @OneToMany(mappedBy = "magasin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes = new HashSet<>();

    /** 多对多：Magasin 和 Produit 通过 Stocker 连接 */
    @ManyToMany
    @JoinTable(name = "Stocker",
            joinColumns = @JoinColumn(name = "idMagasin"),
            inverseJoinColumns = @JoinColumn(name = "idProduit"))
    private Set<Produit> produits = new HashSet<>();

    public Magasin() {}

    public Magasin(String nomMagasin, String adresseMagasin, String telMagasin) {
        this.nomMagasin = nomMagasin;
        this.adresseMagasin = adresseMagasin;
        this.telMagasin = telMagasin;
    }

    public Integer getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(Integer idMagasin) {
        this.idMagasin = idMagasin;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getAdresseMagasin() {
        return adresseMagasin;
    }

    public void setAdresseMagasin(String adresseMagasin) {
        this.adresseMagasin = adresseMagasin;
    }

    public String getTelMagasin() {
        return telMagasin;
    }

    public void setTelMagasin(String telMagasin) {
        this.telMagasin = telMagasin;
    }

    public Set<Rayon> getRayons() {
        return rayons;
    }

    public void setRayons(Set<Rayon> rayons) {
        this.rayons = rayons;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
}
