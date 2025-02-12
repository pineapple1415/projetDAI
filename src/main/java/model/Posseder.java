package model;

import javax.persistence.*;

@Entity
@Table(name = "Posseder")
public class Posseder {

    @EmbeddedId
    private PossederId id;

    @ManyToOne
    @MapsId("idRayon")
    @JoinColumn(name = "idRayon")
    private Rayon rayon;

    @ManyToOne
    @MapsId("idMagasin")
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    public Posseder() {}

    public Posseder(Rayon rayon, Magasin magasin) {
        this.id = new PossederId(rayon.getIdRayon(), magasin.getIdMagasin());
        this.rayon = rayon;
        this.magasin = magasin;
    }

    public PossederId getId() {
        return id;
    }

    public void setId(PossederId id) {
        this.id = id;
    }

    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(Rayon rayon) {
        this.rayon = rayon;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }
}
