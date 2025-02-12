package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClient;

    private String nomClient;
    private String prenomClient;
    private String adresseClient;
    private String codePostal;
    private String emailClient;
    private String telClient;
    private String loginClient;
    private String passwordClient;

    /** 一对多：Client 和 Course（购物车） */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    /** 一对多：Client 和 Commande（订单） */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes = new HashSet<>();

    public Client() {}

    public Client(String nomClient, String prenomClient, String adresseClient, String codePostal,
                  String emailClient, String telClient, String loginClient, String passwordClient) {
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.adresseClient = adresseClient;
        this.codePostal = codePostal;
        this.emailClient = emailClient;
        this.telClient = telClient;
        this.loginClient = loginClient;
        this.passwordClient = passwordClient;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getTelClient() {
        return telClient;
    }

    public void setTelClient(String telClient) {
        this.telClient = telClient;
    }

    public String getLoginClient() {
        return loginClient;
    }

    public void setLoginClient(String loginClient) {
        this.loginClient = loginClient;
    }

    public String getPasswordClient() {
        return passwordClient;
    }

    public void setPasswordClient(String passwordClient) {
        this.passwordClient = passwordClient;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
}
