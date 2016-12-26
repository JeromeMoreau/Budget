package jm.dao;

import jm.entity.Budget;
import jm.entity.Categorie;
import jm.entity.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jejemoreau on 05/12/2016.
 */
@Repository
@Transactional
public class BudgetDAO {

    @Autowired
    private SessionFactory factory;

    /**
     * setter for spring injection
     * @param factory
     */
    public void setFactory(SessionFactory factory) {
        this.factory = factory;
    }

    public Session getSession(){
        return factory.getCurrentSession();
    }

    /**
     * @return list of categories
     */
    public List<Categorie> getAllCategories(){
        String req= "FROM categorie c";

        Session session = factory.getCurrentSession();
        Query query = session.createQuery(req);

        return query.list();
    }

    public List<Categorie> getDepenseCategories(){
        String req= "FROM categorie c WHERE revenue = false";

        Session session = factory.getCurrentSession();
        Query query = session.createQuery(req);

        return query.list();
    }

    public List<Categorie> getRevenuCategories(){
        String req= "FROM categorie c WHERE revenue = true";

        Session session = factory.getCurrentSession();
        Query query = session.createQuery(req);

        return query.list();
    }

    /**
     *
     * @param transaction
     */
    public void ajouterDepense(Transaction transaction){
        getSession().saveOrUpdate(transaction);
    }

    /**
     *
     * @param id
     */
    public void supprimerDepense(int id){
        Transaction transaction = (Transaction) getSession().get(Transaction.class,id);
        getSession().delete(transaction);
    }

    /**
     *
     * @param numMois
     * @return
     */

    public List<Transaction> getTransactionParMois(int numMois){
        String reqhql = "FROM transaction t WHERE month(t.date) = :pNumMois";
        Query query = getSession().createQuery(reqhql);
        query.setParameter("pNumMois",numMois);
        return query.list();
    }

    public List<Budget> getBudgetsParMois(int numMois){
        String reqhql = "FROM budget b WHERE b.numMois = :pNumMois";
        Query query = getSession().createQuery(reqhql);
        query.setParameter("pNumMois",numMois);
        return query.list();
    }

    public void ajouterBudget(Budget pBudget){
        getSession().saveOrUpdate(pBudget);
    }
}
