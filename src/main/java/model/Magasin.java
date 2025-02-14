package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Magasin")
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMagasin;

    @Column(name = "nomMagasin")
    private String nomMagasin;

    @Column(name = "adresseMagasin")
    private String adresseMagasin;

    @Column(name = "telMagasin")
    private String telMagasin;

    @OneToMany(mappedBy = "magasin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Preparateur> preparateurs = new HashSet<>(); // 🚀 确保这里是 Set<Preparateur>，而不是 User！

    /** 一对多：Magasin 和 Stocker（库存中间表） */
    @OneToMany(mappedBy = "magasin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Stocker> stockers = new HashSet<>();

    public Magasin() {}

    public Magasin(String nomMagasin, String adresseMagasin, String telMagasin) {
        this.nomMagasin = nomMagasin;
        this.adresseMagasin = adresseMagasin;
        this.telMagasin = telMagasin;
    }

    public Integer getIdMagasin() { return idMagasin; }
    public void setIdMagasin(Integer idMagasin) { this.idMagasin = idMagasin; }

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

    public Set<Preparateur> getPreparateurs() { return preparateurs; }
    public void setPreparateurs(Set<Preparateur> preparateurs) { this.preparateurs = preparateurs; }

    public Set<Stocker> getStockers() { return stockers; }
    public void setStockers(Set<Stocker> stockers) { this.stockers = stockers; }

    public void addPreparateur(Preparateur preparateur) {
        this.preparateurs.add(preparateur);
    }

    public void removePreparateur(Preparateur preparateur) {
        this.preparateurs.remove(preparateur);
    }


}
