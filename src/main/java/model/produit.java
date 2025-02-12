package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produit")
public class produit {

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
    private fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "idCategorie")
    private categorie categorie;

    @ManyToOne
    @JoinColumn(name = "idRayon")
    private rayon rayon;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ligneCommande> ligneCommande = new HashSet<>();

    public produit(){}

    public produit(Integer idProduit, String nomProduit, Double prixUnit, String origineProduit,
                   String tailleProduit, String description, fournisseur fournisseur, categorie categorie) {
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

    public fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(categorie categorie) {
        this.categorie = categorie;
    }

    public rayon getRayon() {
        return rayon;
    }

    public void setRayon(rayon rayon) {
        this.rayon = rayon;
    }

    public Set<ligneCommande> getDemandes() {
        return ligneCommande;
    }

    public void setDemandes(Set<ligneCommande> ligneCommande) {
        this.ligneCommande = ligneCommande;
    }
}
