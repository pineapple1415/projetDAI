package model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StockerId implements Serializable {

    private Integer idProduit;
    private Integer idMagasin;

    public StockerId() {}

    public StockerId(Integer idProduit, Integer idMagasin) {
        this.idProduit = idProduit;
        this.idMagasin = idMagasin;
    }

    public Integer getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
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
        StockerId that = (StockerId) o;
        return Objects.equals(idProduit, that.idProduit) &&
                Objects.equals(idMagasin, that.idMagasin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduit, idMagasin);
    }
}
