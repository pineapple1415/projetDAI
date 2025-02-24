package model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

// ComposerId.java
@Embeddable
public class ComposerId implements Serializable { // 必须实现Serializable
    private Integer idCommande;
    private Integer idProduit;

    // 必须有无参构造
    public ComposerId() {}

    public ComposerId(Integer idCommande, Integer idProduit) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
    }

    // Getter & Setter
    public Integer getIdCommande() { return idCommande; }
    public void setIdCommande(Integer idCommande) { this.idCommande = idCommande; }

    public Integer getIdProduit() { return idProduit; }
    public void setIdProduit(Integer idProduit) { this.idProduit = idProduit; }

    // 必须正确实现equals和hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComposerId that = (ComposerId) o;
        return Objects.equals(idCommande, that.idCommande) &&
                Objects.equals(idProduit, that.idProduit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommande, idProduit);
    }
}
