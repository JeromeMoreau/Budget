package jm.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jejemoreau on 08/12/2016.
 */
@Entity(name = "budget")
@Table(name = "budgets")
@XmlRootElement
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_budget")
    private int id;

    @Column(name = "mois")
    private int numMois;

    @ManyToOne
    @JoinColumn(name = "categorie_id",referencedColumnName = "id_categorie")
    private Categorie categorie;

    @Column(name = "percent")
    private int pourcentageDuRevenus;

    public Budget() {
    }

    public Budget(int numMois, Categorie categorie, int pourcentageDuRevenus) {
        this.numMois = numMois;
        this.categorie = categorie;
        this.pourcentageDuRevenus = pourcentageDuRevenus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumMois() {
        return numMois;
    }

    public void setNumMois(int numMois) {
        this.numMois = numMois;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getPourcentageDuRevenus() {
        return pourcentageDuRevenus;
    }

    public void setPourcentageDuRevenus(int pourcentageDuRevenus) {
        this.pourcentageDuRevenus = pourcentageDuRevenus;
    }
}
