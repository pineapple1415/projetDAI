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
    private Integer IdProduit;

    @Column(name = "nomProduit")
    private String nomProduit;

    @Column(name = "prixUnit")
    private Double prixUnit;

    @Column(name = "origineProduit")
    private String Origin;

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
    private Set<Demande> demandes = new HashSet<>(0);

<<<<<<< Updated upstream
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

=======
    /**
     * Constructeurs.
     */
    public produit() {
    }

    public produit(Integer idProduit, String nomProduit, Double prixUnit, String origin, String tailleProduit, String description, model.fournisseur fournisseur, model.categorie categorie) {
        this.IdProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.Origin = origin;
        this.tailleProduit = tailleProduit;
        this.description = description;
        this.fournisseur = fournisseur;
    }

    /**
     * Setter / Getter.
     */
    public Integer getIdProduit() {
        return IdProduit;
    }

    public void setIdProduit(Integer idProduit) {
        IdProduit = idProduit;
    }

>>>>>>> Stashed changes
    public String getNomProduit() {
        return nomProduit;
    }

<<<<<<< Updated upstream
    public Double getPrixProduit() {
        return prixProduit;
=======
    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Double getPrixUnit() {
        return prixUnit;
    }

    public void setPrixUnit(Double prixUnit) {
        this.prixUnit = prixUnit;
>>>>>>> Stashed changes
    }

    public String getOrigin() {
        return Origin;
    }

<<<<<<< Updated upstream
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

=======
>>>>>>> Stashed changes
    public void setOrigin(String origin) {
        Origin = origin;
    }

<<<<<<< Updated upstream
    public void setFourisseur(String fourisseur) {
        Fourisseur = fourisseur;
    }

    public void setCategorieSet(Set<categorie> categorieSet) {
        this.categorieSet = categorieSet;
=======
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
>>>>>>> Stashed changes
    }
}
