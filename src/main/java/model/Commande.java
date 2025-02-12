package model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCommande;

    private Double prixTotal;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommande;

    @Enumerated(EnumType.STRING)
    private Statut statut;  // 使用 Statut 作为订单状态

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    /** 一对多：Commande 和 Composer（订单详情） */
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Composer> composers = new HashSet<>();

    public Commande() {}

    public Commande(Double prixTotal, Date dateCommande, Statut statut, Client client, Magasin magasin) {
        this.prixTotal = prixTotal;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.client = client;
        this.magasin = magasin;
    }

    public Integer getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Integer idCommande) {
        this.idCommande = idCommande;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Set<Composer> getComposers() {
        return composers;
    }

    public void setComposers(Set<Composer> composers) {
        this.composers = composers;
    }
}
