package jm.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jejemoreau on 05/12/2016.
 */
@Entity(name = "transaction")
@Table(name = "transactions")
@XmlRootElement
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_transaction")
    private int id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "montant")
    private double montant;

    @Column(name = "obligatoire")
    private boolean obligatoire;

    @Column(name = "description")
    private String description;

    @Column(name = "isRevenue")
    private boolean isRevenue;

    @ManyToOne
    @JoinColumn(name = "categorie_id",referencedColumnName = "id_categorie")
    private Categorie categorie;



    /**
     * ctor
     */
    public Transaction() {
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRevenue() {
        return isRevenue;
    }

    public void setRevenue(boolean revenue) {
        isRevenue = revenue;
    }
}
