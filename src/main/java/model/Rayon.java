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

}
