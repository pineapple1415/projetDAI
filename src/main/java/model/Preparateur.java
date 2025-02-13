package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("PREPARATEUR") // userType = "PREPARATEUR"
public class Preparateur extends User {

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    @OneToMany(mappedBy = "preparateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes = new HashSet<>();

    public Preparateur() {}

    public Preparateur(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password);
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
}
