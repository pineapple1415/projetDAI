package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCourse;

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false)
    @JsonBackReference
    private User client;

    @Column(name = "texte", columnDefinition = "TEXT", nullable = true) // 允许 NULL
    private String texte;


    /** 多对多：Course 和 Produit 通过 Ajouter 连接 */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ajouter> ajouts = new HashSet<>();

    public Course() {}

    // 允许创建 Course 时提供 texte，但可以为空
    public Course(User client, String texte) {
        this.client = client;
        this.texte = (texte == null || texte.trim().isEmpty()) ? "Sans description" : texte; // 默认值
    }



    public Integer getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Integer idCourse) {
        this.idCourse = idCourse;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Set<Ajouter> getAjouts() {
        return ajouts;
    }

    public void setAjouts(Set<Ajouter> ajouts) {
        this.ajouts =ajouts;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
}