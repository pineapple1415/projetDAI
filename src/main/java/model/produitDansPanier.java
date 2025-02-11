package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "produitDansPanier")
public class produitDansPanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdProduit;

    @ManyToOne
    @JoinColumn(name = "Panier")
    private panier panier;

    private String nomProduit;

    public produitDansPanier(){}

    public produitDansPanier(String nomProduit) {
        this.nomProduit = nomProduit;
    }





    // Getters and Setters
    public Long getIdProduit() {
        return IdProduit;
    }

    public model.panier getPanier() {
        return panier;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setIdProduit(Long IdProduit) {
        this.IdProduit = IdProduit;
    }

    public void setPanier(model.panier panier) {
        this.panier = panier;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }
}