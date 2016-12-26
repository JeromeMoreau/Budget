package jm.rest;

import jm.entity.Budget;
import jm.entity.Categorie;
import jm.entity.Transaction;
import jm.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * Created by jejemoreau on 05/12/2016.
 */
@Component
@Path("/ws")
public class BudgetRest extends Application {
    @Autowired
    private BudgetService service;

    /**
     * setter injectino spring
     * @param service
     */
    public void setService(BudgetService service) {
        this.service = service;
    }

    /*------ACCES AU CATEGORIES-------*/

    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Categorie> getCategories(){
        Collection<Categorie> categories = service.getListCategories();
        System.out.println("Liste des categories: "+ categories);
        return categories;
    }

    @GET
    @Path("/categories/depenses")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Categorie> getCategoriesDepenses(){
        Collection<Categorie> categories = service.getListCategoriesDepenses();
        return categories;
    }

    @GET
    @Path("/categories/revenus")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Categorie> getCategoriesRevenus(){
        Collection<Categorie> categories = service.getListCategoriesRevenus();
        return categories;
    }

    /*------ACCES AU TRANSACTIONS-------*/

    @POST
    @Path("/depenses")
    @Consumes(MediaType.APPLICATION_JSON)
    public void ajouterDepense(Transaction transaction){
        System.out.println("AJOUT DEPENSE: " + transaction.getCategorie());
        service.ajouterDepense(transaction);
    }

    @GET
    @Path("/depenses/{numMois}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getDepensesDuMois(@PathParam("numMois") int numMois){
        return service.getDepensesDuMois(numMois);
    }

    @GET
    @Path("/revenus/{numMois}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getRevenusDuMois(@PathParam("numMois") int numMois){
        return service.getRevenusDuMois(numMois);
    }


    @DELETE
    @Path("/depenses/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void supprimerDepense(@PathParam("id") int id){
        System.out.println("DELETE DEPENSE: "+id);
        service.supprimerTransaction(id);
    }

    /*------ACCES AU BUDGETS-------*/

    @GET
    @Path("/budgets/{numMois}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Budget> getBudgetsDuMois(@PathParam("numMois") int numMois){
        return service.budgetsParMois(numMois);
    }

    @POST
    @Path("/budgets")
    @Consumes(MediaType.APPLICATION_JSON)
    public void ajouterBudget(Budget pBudget){
        service.creerBudget(pBudget);
    }

    @POST
    @Path("/budgets/{numMois}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean ajouterBudget(@PathParam("numMois") int numMois){
        boolean verif = service.ajouterBudgetDefault(numMois);
        return verif;
    }

    @PUT
    @Path("/budgets/{numMois}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean modifierBudgets(@PathParam("numMois") int numMois, List<Budget> budgets){
        System.out.println("REST: ModifierBudgets()");
        boolean verif = service.majListeBudgets(budgets,numMois);
        return verif;
    }
}
