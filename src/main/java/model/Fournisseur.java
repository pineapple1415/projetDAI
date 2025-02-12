package model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFournisseur;

    private String nomFournisseur;
    private String adresseFournisseur;
    private String contactFournisseur;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Produit> produits;

    public Fournisseur() {}

    public Fournisseur(String nomFournisseur, String adresseFournisseur, String contactFournisseur) {
        this.nomFournisseur = nomFournisseur;
        this.adresseFournisseur = adresseFournisseur;
        this.contactFournisseur = contactFournisseur;
    }

    public Integer getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Integer idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getAdresseFournisseur() {
        return adresseFournisseur;
    }

    public void setAdresseFournisseur(String adresseFournisseur) {
        this.adresseFournisseur = adresseFournisseur;
    }

    public String getContactFournisseur() {
        return contactFournisseur;
    }

    public void setContactFournisseur(String contactFournisseur) {
        this.contactFournisseur = contactFournisseur;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
}
