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
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "idPreparateur")
    private User preparateur;

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    public Commande() {}

    public Commande(Double prixTotal, Date dateCommande, Statut statut, User client, User preparateur, Magasin magasin) {
        this.prixTotal = prixTotal;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.client = client;
        this.preparateur = preparateur;
        this.magasin = magasin;
    }

    public Integer getIdCommande() { return idCommande; }
    public void setIdCommande(Integer idCommande) { this.idCommande = idCommande; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    public Date getDateCommande() { return dateCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getPreparateur() {
        return preparateur;
    }

    public void setPreparateur(User preparateur) {
        this.preparateur = preparateur;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }
}
