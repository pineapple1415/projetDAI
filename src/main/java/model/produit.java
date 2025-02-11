package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produit")
public class produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdProduit;

    @Column(nullable = false)
    private String nomProduit;

    @Column(nullable = false)
    private Double prixProduit;

    private String Origin;

    private String Fourisseur;

//    @ManyToOne(fetch = FetchType.LAZY)

    //un produit, plusieur de categorie
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<categorie> categorieSet = new HashSet<>(0);
    // Getters and Setters

    public produit(){}

    public produit(String nomProduit, Double prixProduit, String origin, String fourisseur, Set<categorie> categorieSet) {
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        Origin = origin;
        Fourisseur = fourisseur;
        this.categorieSet = categorieSet;
    }




    public Long getIdProduit() {
        return IdProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public Double getPrixProduit() {
        return prixProduit;
    }

    public String getOrigin() {
        return Origin;
    }

    public String getFourisseur() {
        return Fourisseur;
    }

    public Set<categorie> getCategorieSet() {
        return categorieSet;
    }

    public void setIdProduit(Long idProduit) {
        IdProduit = idProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setPrixProduit(Double prixProduit) {
        this.prixProduit = prixProduit;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public void setFourisseur(String fourisseur) {
        Fourisseur = fourisseur;
    }

    public void setCategorieSet(Set<categorie> categorieSet) {
        this.categorieSet = categorieSet;
    }


    public void addToPanier(panier panier) {

    }
}
