package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Stocker")
public class Stocker {

    @EmbeddedId
    private StockerId id;

    @ManyToOne
    @MapsId("idProduit")
    @JoinColumn(name = "idProduit")
    @JsonBackReference
    private Produit produit;

    @ManyToOne
    @MapsId("idMagasin")
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    private Integer nbStock;

    public Stocker() {}

    public Stocker(Produit produit, Magasin magasin, Integer nbStock) {
        this.id = new StockerId(produit.getIdProduit(), magasin.getIdMagasin());
        this.produit = produit;
        this.magasin = magasin;
        this.nbStock = nbStock;
    }

    public Integer getNbStock() {
        return nbStock;
    }

    public void setNbStock(Integer nbStock) {
        this.nbStock = nbStock;
    }
}
