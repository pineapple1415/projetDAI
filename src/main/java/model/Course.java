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
    @JoinColumn(name = "IdUser")
    private User user;

    /** 多对多：Course 和 Produit 通过 Ajouter 连接 */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ajouter> ajouts = new HashSet<>();

    public Course() {}

    public Course(User user) {
        this.user = user;
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

    public Set<Ajouter> getAjouts() {
        return ajouts;
    }

    public void setAjouts(Set<Ajouter> ajouts) {
        this.ajouts =ajouts;
    }
}