package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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



    private String origineProduit;
    private String tailleProduit;
    private String descriptionProduit;

    @Column(name = "image")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    @JsonBackReference
    private Fournisseur fournisseur;


    @ManyToOne
    @JoinColumn(name = "idCategorie")
    @JsonBackReference
    private Categorie categorie;

    /** 一对多：Produit 和 Composer（订单详情） */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Composer> composers = new HashSet<>();

    /** 一对多：Produit 和 Ajouter（购物车中间表） */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ajouter> ajouts = new HashSet<>();

    /** 一对多：Produit 和 Stocker（库存中间表） */
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Stocker> stockers = new HashSet<>();

    /** ✅ **默认构造函数**（Hibernate 需要） */
    public Produit() {}

    /** ✅ **参数化构造函数** */
    public Produit(String nomProduit, Double prixUnit, String origineProduit, String tailleProduit, String descriptionProduit, String imageUrl,
                   Fournisseur fournisseur, Categorie categorie) {
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.origineProduit = origineProduit;
        this.tailleProduit = tailleProduit;
        this.descriptionProduit = descriptionProduit;
        this.imageUrl = imageUrl;
        this.fournisseur = fournisseur;
        this.categorie = categorie;
    }

    /** ✅ **Getter 和 Setter** */
    public Integer getIdProduit() { return idProduit; }
    public void setIdProduit(Integer idProduit) { this.idProduit = idProduit; }

    public String getNomProduit() { return nomProduit; }
    public void setNomProduit(String nomProduit) { this.nomProduit = nomProduit; }

    public Double getPrixUnit() { return prixUnit; }
    public void setPrixUnit(Double prixUnit) { this.prixUnit = prixUnit; }

    public String getOrigineProduit() { return origineProduit; }
    public void setOrigineProduit(String origineProduit) { this.origineProduit = origineProduit; }

    public String getTailleProduit() { return tailleProduit; }
    public void setTailleProduit(String tailleProduit) { this.tailleProduit = tailleProduit; }

    public String getDescriptionProduit() { return descriptionProduit; }
    public void setDescriptionProduit(String descriptionProduit) { this.descriptionProduit = descriptionProduit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Set<Composer> getComposers() { return composers; }
    public void setComposers(Set<Composer> composers) { this.composers = composers; }

    public Set<Ajouter> getAjouts() { return ajouts; }
    public void setAjouts(Set<Ajouter> ajouts) { this.ajouts = ajouts; }

    public Set<Stocker> getStockers() { return stockers; }
    public void setStockers(Set<Stocker> stockers) { this.stockers = stockers; }
}
