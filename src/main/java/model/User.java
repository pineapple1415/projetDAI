package model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUser")
    private Integer idUser;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adress")
    private String adress;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();


    //Constructeur avec paramettre et sans paramettre
    public User() {
    }

    public User( String nom, String prenom, String adress, String email, String telephone,  String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.adress = adress;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }

    public User(Integer idUser, String nom, String prenom, String adress, String email, String telephone, String password, Set<Commande> commandes, Set<Course> courses) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.adress = adress;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.commandes = commandes;
        this.courses = courses;
    }

    //Getter et setter
    public Integer getIdUser() {
        return idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adress='" + adress + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
