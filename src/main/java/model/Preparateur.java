package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("PREPARATEUR") // userType = "PREPARATEUR"
public class Preparateur extends User {

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    @JsonBackReference
    private Magasin magasin;


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

}
