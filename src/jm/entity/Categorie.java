package jm.entity;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by jejemoreau on 05/12/2016.
 */
@Entity(name = "categorie")
@Table(name = "categories")
@XmlRootElement
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categorie")
    private int id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "is_revenue")
    private boolean revenue;

    @OneToMany(mappedBy = "categorie",fetch=FetchType.EAGER)
    private List<Transaction> transactions;



    /**
     * ctor
     */
    public Categorie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @JsonIgnore
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean isRevenue() {
        return revenue;
    }

    public void setRevenue(boolean revenue) {
        this.revenue = revenue;
    }
}
