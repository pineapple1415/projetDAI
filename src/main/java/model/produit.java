package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduit")
    private Integer idProduit;

    @Column(name = "nomProduit")
    private String nomProduit;

    @Column(name = "prixUnit")
    private Double prixUnit;

    @Column(name = "origineProduit")
    private String origineProduit;

    @Column(name = "tailleProduit")
    private String tailleProduit;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "idCategorie")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "idRayon")
    private Rayon rayon;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ligneCommande> ligneCommande = new HashSet<>();

    public Produit() {
    }

    public Produit(Integer idProduit, String nomProduit, Double prixUnit, String origineProduit,
                   String tailleProduit, String description, Fournisseur fournisseur, Categorie categorie) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.origineProduit = origineProduit;
        this.tailleProduit = tailleProduit;
        this.description = description;
        this.fournisseur = fournisseur;
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

    public String getOrigineProduit() {
        return origineProduit;
    }

    public void setOrigineProduit(String origineProduit) {
        this.origineProduit = origineProduit;
    }

    public String getTailleProduit() {
        return tailleProduit;
    }

    public void setTailleProduit(String tailleProduit) {
        this.tailleProduit = tailleProduit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(fournisseur fournisseur) {
        this.Fournisseur = fournisseur;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(Rayon rayon) {
        this.rayon = rayon;
    }

    public Set<ligneCommande> getDemandes() {
        return ligneCommande;
    }

    public void setDemandes(Set<ligneCommande> ligneCommande) {
        this.ligneCommande = ligneCommande;
    }
}
