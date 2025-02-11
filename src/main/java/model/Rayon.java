package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Rayon")
public class Rayon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRayon")
    private Integer idRayon;

    @Column(name = "nomRayon")
    private String nomRayon;

    @OneToMany(mappedBy = "rayon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Produit> produits = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "Posseder",
            joinColumns = @JoinColumn(name = "idRayon"),
            inverseJoinColumns = @JoinColumn(name = "idMagasin"))
    private Set<Magasin> magasins = new HashSet<>();

    public Rayon() {
    }

    public Rayon(Integer idRayon, String nomRayon) {
        this.idRayon = idRayon;
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

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Set<Magasin> getMagasins() {
        return magasins;
    }

    public void setMagasins(Set<Magasin> magasins) {
        this.magasins = magasins;
    }
}
