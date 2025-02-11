package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private Integer idCourse;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private User user;

    @ManyToMany
    @JoinTable(name = "Ajouter",
            joinColumns = @JoinColumn(name = "idCourse"),
            inverseJoinColumns = @JoinColumn(name = "idProduit"))
    private Set<Produit> produits = new HashSet<>();

    public Course() {
    }

    public Course(Integer idCourse, User user, Set<Produit> produits) {
        this.idCourse = idCourse;
        this.user = user;
        this.produits = produits;
    }

    public Integer getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Integer idCourse) {
        this.idCourse = idCourse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
}
