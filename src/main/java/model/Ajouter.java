package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Ajouter")
public class Ajouter {

    @EmbeddedId
    private AjouterId id;

    @ManyToOne
    @MapsId("idCourse")
    @JoinColumn(name = "idCourse")
    @JsonBackReference
    private Course course;

    @ManyToOne
    @MapsId("idProduit")
    @JoinColumn(name = "idProduit")
    @JsonBackReference
    private Produit produit;

    private Integer nombre;

    public Ajouter() {}

    public Ajouter(Course course, Produit produit, Integer nombre) {
        this.id = new AjouterId(course.getIdCourse(), produit.getIdProduit());
        this.course = course;
        this.produit = produit;
        this.nombre = nombre;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    @JsonBackReference
    public Course getCourse() { return course; }

    public void setCourse(Course course) {
        this.course = course;
        if (this.id == null) {
            this.id = new AjouterId();
        }
        this.id.setIdCourse(course.getIdCourse());
    }

    public Produit getProduit() { return produit; }

    public void setProduit(Produit produit) {
        this.produit = produit;
        if (this.id == null) {
            this.id = new AjouterId();
        }
        this.id.setIdProduit(produit.getIdProduit());
    }

    public AjouterId getId() {
        return id;
    }

    public void setId(AjouterId id) {
        this.id = id;
    }
}
