package model;

import javax.persistence.*;

@Entity
@DiscriminatorValue("GERANT") // userType = "GERANT"
public class Gerant extends User {
    public Gerant() {}
    public Gerant(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password);
    }
}
