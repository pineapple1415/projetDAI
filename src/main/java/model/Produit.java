package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "0.0", message = "Promotion不能为负数")
    @DecimalMax(value = "1.0", message = "Promotion不能超过1.0")
    @Column(name = "promotion", nullable = false, columnDefinition = "double default 0.0")
    private double promotion = 0.0;

    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    @JsonBackReference
    @JsonIgnore // ✅ 避免 Jackson 访问 `Fournisseur`
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
                   Fournisseur fournisseur,Categorie categorie, double promotion) {
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.origineProduit = origineProduit;
        this.tailleProduit = tailleProduit;
        this.descriptionProduit = descriptionProduit;
        this.imageUrl = imageUrl;
        this.fournisseur = fournisseur;
        this.categorie = categorie;
        this.promotion = promotion;
    }

    public Produit(String nomProduit, Double prixUnit, String origineProduit, String tailleProduit, String descriptionProduit, String imageUrl,
                   Fournisseur fournisseur,Categorie categorie) {
        this.nomProduit = nomProduit;
        this.prixUnit = prixUnit;
        this.origineProduit = origineProduit;
        this.tailleProduit = tailleProduit;
        this.descriptionProduit = descriptionProduit;
        this.imageUrl = imageUrl;
        this.fournisseur = fournisseur;
        this.categorie = categorie;
        this.promotion = 0.0;
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

    public double getPromotion() {
        return promotion;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    // 方便显示促销文本的方法
    public String getPromotionText() {
        return promotion > 0.0 ? ((int)(promotion * 100) + "%") : "None";
    }

    // 快捷获得折扣后的价格
    public double getPrixApresPromotion() {
        return prixUnit * (1 - promotion);
    }

}
