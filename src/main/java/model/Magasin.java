package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Magasin")
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMagasin;

    private String nomMagasin;
    private String adresseMagasin;
    private String telMagasin;

    @OneToMany(mappedBy = "magasin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Preparateur> preparateurs = new HashSet<>(); // 🚀 确保这里是 Set<Preparateur>，而不是 User！

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

    public void addPreparateur(Preparateur preparateur) {
        this.preparateurs.add(preparateur);
        preparateur.setMagasin(this);
    }

    public void removePreparateur(Preparateur preparateur) {
        this.preparateurs.remove(preparateur);
        preparateur.setMagasin(null);
    }
}
