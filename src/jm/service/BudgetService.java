package jm.service;

import jm.dao.BudgetDAO;
import jm.entity.Budget;
import jm.entity.Categorie;
import jm.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jejemoreau on 05/12/2016.
 */
@Service
public class BudgetService {
    @Autowired
    private BudgetDAO dao;

    /**
     * setter spring injection
     *
     * @param dao
     */
    public void setDao(BudgetDAO dao) {
        this.dao = dao;
    }

    public List<Categorie> getListCategories() {
        return dao.getAllCategories();
    }

    public List<Categorie> getListCategoriesDepenses(){
        List<Categorie> categoriesDepense = new ArrayList<>();
        for (Categorie cat:dao.getAllCategories()) {
            if (cat.isRevenue() == false) categoriesDepense.add(cat);
        }
        return categoriesDepense;
    }

    public List<Categorie> getListCategoriesRevenus(){
        List<Categorie> categoriesRevenus = new ArrayList<>();
        for (Categorie cat:dao.getAllCategories()) {
            if (cat.isRevenue()) categoriesRevenus.add(cat);
        }
        return categoriesRevenus;
    }

    public void ajouterDepense(Transaction transaction) {
        dao.ajouterDepense(transaction);
    }

    @Transactional
    public List<Transaction> getDepensesDuMois(int numMois) {
        List<Transaction> transactions = dao.getTransactionParMois(numMois);
        List<Transaction> depenses = new ArrayList<>();
        for (Transaction tr:transactions) {
            if (tr.isRevenue() == false) depenses.add(tr);
        }


        return depenses;
    }

    public List<Transaction> getRevenusDuMois(int numMois){
        List<Transaction> transactions = dao.getTransactionParMois(numMois);
        List<Transaction> revenus = new ArrayList<>();
        for (Transaction tr:transactions) {
            if (tr.isRevenue()) revenus.add(tr);
        }
        return revenus;
    }

    

    public void supprimerTransaction(int id){
        dao.supprimerDepense(id);
    }

    /**
     *
     * @param numMois
     * @return
     */
    public List<Budget> budgetsParMois(int numMois){
        List<Budget> budgets = dao.getBudgetsParMois(numMois);

        return budgets;
    }

    public boolean ajouterBudgetDefault(int numMois){
        //verif si budget existe deja
        List<Budget> budgetList = dao.getBudgetsParMois(numMois);
        if (!(budgetList.size() > 0)){
            // Cree un budget pour chaque categorie
            List<Categorie> categories = dao.getDepenseCategories();
            for (Categorie cat: categories) {
                int percent = (int)Math.floor(100/categories.size());
                Budget budget = new Budget(numMois,cat,percent);
                dao.ajouterBudget(budget);
            }
            System.out.println("Budgets créés par défauts");
            return true;
        }
        return false;
    }

    public boolean majListeBudgets(List<Budget> budgets, int numMois){
        List<Budget> budgetsInDB = dao.getBudgetsParMois(numMois);
        for (Budget budget:budgets) {
            for (Budget inDb:budgetsInDB){
                if (inDb.getCategorie().getId() == budget.getCategorie().getId()){
                    budget.setId(inDb.getId());
                    break;
                }
            }
            dao.ajouterBudget(budget);
        }
        return true;
    }

    public void creerBudget(Budget pBudget){
        dao.ajouterBudget(pBudget);
    }
}
