package model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PossederId implements Serializable {

    private Integer idRayon;
    private Integer idMagasin;

    public PossederId() {}

    public PossederId(Integer idRayon, Integer idMagasin) {
        this.idRayon = idRayon;
        this.idMagasin = idMagasin;
    }

    public Integer getIdRayon() {
        return idRayon;
    }

    public void setIdRayon(Integer idRayon) {
        this.idRayon = idRayon;
    }

    public Integer getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(Integer idMagasin) {
        this.idMagasin = idMagasin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PossederId that = (PossederId) o;
        return Objects.equals(idRayon, that.idRayon) &&
                Objects.equals(idMagasin, that.idMagasin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRayon, idMagasin);
    }
}
