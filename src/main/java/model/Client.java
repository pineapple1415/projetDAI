package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes = new HashSet<>();

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "codePostal")
    private String codePostal;

    @Column(name = "telephone")
    private String telephone;

    public Client() {}

    public Client(String nom, String prenom, String email, String password, String adresse, String codePostal, String telephone) {
        super(nom, prenom, email, password);
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.telephone = telephone;
    }

    // Getters & Setters
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }

    public Set<Commande> getCommandes() { return commandes; }
    public void setCommandes(Set<Commande> commandes) { this.commandes = commandes; }
}
