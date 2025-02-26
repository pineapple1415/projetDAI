package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonBackReference
    private User client;

    @ManyToOne
    @JoinColumn(name = "idPreparateur")
    @JsonBackReference
    private User preparateur;

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    @JsonBackReference  // Empêche la récursion infinie avec `Magasin`
    private Magasin magasin;


    @Column(name = "FinirPrepa")
    private Date finirPrepa;

    @Column(name = "dateAjoutPanier")
    private Date dateAjoutPanier;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Permet la sérialisation JSON
    private Set<Composer> composers = new HashSet<>();



    public Commande() {}

    public Commande(Double prixTotal, Date dateCommande, Statut statut, User client, User preparateur, Magasin magasin, Date dateAjoutPanier) {
        this.prixTotal = prixTotal;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.client = client;
        this.preparateur = preparateur;
        this.magasin = magasin;
        this.dateAjoutPanier = dateAjoutPanier;
        this.finirPrepa = null;
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

    public Magasin getMagasin() {
        return magasin;
    }
    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }


    public Date getFinirPrepa() { return finirPrepa; }
    public void setFinirPrepa(Date finirPrepa) {
        this.finirPrepa = finirPrepa;
    }
    public Date getDateAjoutPanier() { return dateAjoutPanier; }

    public Set<Composer> getComposers() {
        return composers;
    }

    public void setComposers(Set<Composer> composers) {
        this.composers = composers;
    }

    public void setDateAjoutPanier(Date dateAjoutPanier) {
            this.dateAjoutPanier = dateAjoutPanier;


}
}
