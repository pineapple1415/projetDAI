package model;

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
    @JoinColumn(name = "idClient")
    private Client client;

    /** 多对多：Course 和 Produit 通过 Ajouter 连接 */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ajouter> ajouts = new HashSet<>();

    public Course() {}

    public Course(Client client) {
        this.client = client;
    }

    public Integer getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Integer idCourse) {
        this.idCourse = idCourse;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Ajouter> getAjouts() {
        return ajouts;
    }

    public void setAjouts(Set<Ajouter> ajouts) {
        this.ajouts = ajouts;
    }
}
