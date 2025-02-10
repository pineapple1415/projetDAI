package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "panier")
public class panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdPanier;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private Set<produit> items = new HashSet<>();

    public panier() {}

    public panier(User user, Set<produit> items) {
        this.user = user;
        this.items = items;
    }


    // Getters and Setters

    public Long getIdPanier() {
        return IdPanier;
    }

    public User getUser() {
        return user;
    }

    public Set<produit> getItems() {
        return items;
    }

    public void setIdPanier(Long idPanier) {
        this.IdPanier = idPanier;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItems(Set<produit> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        panier panier = (panier) o;
        return Objects.equals(IdPanier, panier.IdPanier) && Objects.equals(user, panier.user) && Objects.equals(items, panier.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IdPanier, user, items);
    }

    @Override
    public String toString() {
        return "panier{" +
                "IDpanier=" + IdPanier +
                ", user=" + user +
                ", items=" + items +
                '}';
    }
}