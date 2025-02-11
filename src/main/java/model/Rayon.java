package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Rayon")
public class Rayon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRayon;

    private String nomRayon;

    /** 多对多：Rayon 和 Magasin */
    @ManyToMany
    @JoinTable(name = "Posseder",
            joinColumns = @JoinColumn(name = "idRayon"),
            inverseJoinColumns = @JoinColumn(name = "idMagasin"))
    private Set<Magasin> magasins = new HashSet<>();

    /** 一对多：Rayon 和 Produit */
    @OneToMany(mappedBy = "rayon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Produit> produits = new HashSet<>();

    public Rayon() {}

    public Rayon(String nomRayon) {
        this.nomRayon = nomRayon;
    }

    public Integer getIdRayon() {
        return idRayon;
    }

    public void setIdRayon(Integer idRayon) {
        this.idRayon = idRayon;
    }

    public String getNomRayon() {
        return nomRayon;
    }

    public void setNomRayon(String nomRayon) {
        this.nomRayon = nomRayon;
    }

    public Set<Magasin> getMagasins() {
        return magasins;
    }

    public void setMagasins(Set<Magasin> magasins) {
        this.magasins = magasins;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
}
