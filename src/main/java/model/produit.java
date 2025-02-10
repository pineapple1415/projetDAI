package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produit")
public class produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdProduit;

    @Column(nullable = false)
    private String nomProduit;

    @Column(nullable = false)
    private Double prixProduit;

    private String Origin;

    private String Fourisseur;

//    @ManyToOne(fetch = FetchType.LAZY)

    //un produit, plusieur de categorie
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<categorie> categorieSet = new HashSet<>(0);
    // Getters and Setters

}
