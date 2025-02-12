package model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AjouterId implements Serializable {

    private Integer idCourse;
    private Integer idProduit;

    public AjouterId() {}

    public AjouterId(Integer idCourse, Integer idProduit) {
        this.idCourse = idCourse;
        this.idProduit = idProduit;
    }

    public Integer getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Integer idCourse) {
        this.idCourse = idCourse;
    }

    public Integer getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AjouterId that = (AjouterId) o;
        return Objects.equals(idCourse, that.idCourse) &&
                Objects.equals(idProduit, that.idProduit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCourse, idProduit);
    }
}
