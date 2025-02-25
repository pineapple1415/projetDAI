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

    /** 多对多：Course 和 Produit 通过 Ajouter 连接 */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ajouter> ajouts = new HashSet<>();

    public Course() {}

    public Course(User client) {
        this.client = client;
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
}