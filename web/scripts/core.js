/**
 * Created by jejemoreau on 07/12/2016.
 */
/**
 * Definition du module application
 * @type {angular.Module}
 */
var app = angular.module("budgetApp", ["ngRoute",'angular-datepicker','chart.js' ]);


/**
 * Definition des routes
 */
app.config(function($routeProvider) {
    $routeProvider.when("/ajout", {templateUrl: "views/ajoutDepense.html",controller: "ajoutController"})
        .when("/mois", {templateUrl: "views/mois.html",controller: "moisController"})
        .when("/annee", {templateUrl: "views/annee.html",controller: "anneeController"})
        .when("/budget", {templateUrl: "views/ajoutBudget.html",controller: "budgetController"})
        .otherwise({redirectTo: '/mois'})
});


/**
 * definition des controllers de l'application
 */
app.controller("ajoutController", function($scope,$rootScope, $http, $location) {
    $rootScope.pageTitle = "Ajout Depense";
    $scope.transaction = {'categorie':'','date':'','description':'','montant':'','obligatoire':true, 'revenue':''};

    var isRevenus = $location.search().revenus;
    console.log("is revenus ? " +isRevenus)
    if (isRevenus){
        //get categories revenus
        $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/categories/revenus').then(function (response) {
            $scope.listeCategories = response.data;
            $scope.transaction.revenue = true;
        }, function (reason) {
            console.log(reason);
        })
    } else {
        //get categories depenses
        $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/categories/depenses').then(function (response) {
            $scope.listeCategories = response.data;
            $scope.transaction.revenue = false;
        }, function (reason) {
            console.log(reason);
        })
    }





    /*------DatePicker-------*/
    $scope.options = {
        format: 'yyyy-mm-dd', // ISO formatted date
        formatSubmit: 'yyyy-mm-dd',
        selectMonths: true, // Creates a dropdown to control month
        closeOnSelect: true,
        monthsFull: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
        weekdaysShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
        today: 'aujourd\'hui',
        clear: 'effacer',
        firstDay: 1,
        hiddenName: true,
    }



    $scope.ajoutDepense = function () {
        console.log($scope.transaction)
        $http({method:'POST',url:'http://localhost:8080/Budget2_war_exploded/rest/ws/depenses/',data: angular.toJson($scope.transaction)})
            .success(function(response) {
                console.log(response.data);
                $location.path("#/mois");
        }).error(function(reason) {
            console.log('error: '+reason);
        });

    }


}).controller('moisController',function ($scope, $rootScope, $http) {
    $rootScope.pageTitle = "Mois";
    $rootScope.activeMois = new Date().getMonth();
    $scope.budgets = [];

    var setTotal = function (transactionList) {
        var sum = 0;
        for( t=0; t < transactionList.length; t++) { sum += transactionList[t].montant }
        return sum.toFixed(2);
    };


    var setTransactions = function(numMois){
        //obtenir les depenses
        $scope.depenseDuMois = [];
        $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/depenses/'+(numMois+1)).then(function (response) {
            $scope.depenseDuMois = response.data;
            $scope.totalDepenses = setTotal($scope.depenseDuMois);
        }, function (reason) {
            console.log(reason);
        });

        //obtenir les revenus
        $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/revenus/'+(numMois+1)).then(function (response) {
            $scope.revenusDuMois = response.data;
            $scope.totalRevenus = setTotal($scope.revenusDuMois);
            $scope.revenusDisponibles = $scope.totalRevenus *0.9;
        }, function (reason) {
            console.log(reason);
        })

    };

    var setBudgets = function (numMois) {
        //obtenir les budgets
        $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/budgets/'+(numMois+1)).then(function (response) {
            $scope.budgets = response.data;

        }, function (reason) {
            console.log(reason);
        })
    };

    $scope.creerDefaultBudgets = function () {
        $http.post('http://localhost:8080/Budget2_war_exploded/rest/ws/budgets/'+($rootScope.activeMois+1),[]).then(function (response) {
            var success = response.data;
            console.log("Adding default budgets: "+success);
            setBudgets($scope.activeMois);
        }, function (reason) {
            console.log(reason);
        })
    };


    //init the current month
    setTransactions($rootScope.activeMois);
    setBudgets($rootScope.activeMois);

    var totalCategorie = function (catId) {
        var sum = 0;
        for( t=0; t < $scope.depenseDuMois.length; t++) {
            if ($scope.depenseDuMois[t].categorie.id == catId){
                sum += $scope.depenseDuMois[t].montant;
            }
        }
        return sum.toFixed(2);
    };

    $scope.pieData = function (catIndex) {
        if (catIndex == undefined || $scope.budgets.length == 0) return;
        var alloue = ($scope.budgets[catIndex].pourcentageDuRevenus * $scope.revenusDisponibles)/100;
        console.log('CALCULATING FOR BUDGET: '+$scope.budgets[catIndex].categorie.nom);

        var utilise = totalCategorie($scope.budgets[catIndex].categorie.id);
        $scope.budgets[catIndex].utilise = utilise;
        $scope.budgets[catIndex].alloue = alloue;
        return ((utilise/alloue)*100).toFixed(2);
    };



    //pour changer de mois
    $scope.changerMois = function (numMois) {
        $rootScope.activeMois = numMois;
        setTransactions($rootScope.activeMois)
    };

    $scope.supprimerTransaction = function (idDepense) {
        $http({method:'DELETE',url:'http://localhost:8080/Budget2_war_exploded/rest/ws/depenses/'+idDepense})
            .success(function(response) {
                console.log(response.data);
                setTransactions($rootScope.activeMois)
            }).error(function(reason) {
            console.log('error: '+reason);
        });
    }


}).controller('budgetController',function ($scope, $rootScope, $http, $location){
    $rootScope.pageTitle = "Budgets";

    var setTotal = function (transactionList) {
        var sum = 0;
        for( t=0; t < transactionList.length; t++) { sum += transactionList[t].montant }
        return sum.toFixed(2);
    }

    //obtenir les revenus
    $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/revenus/'+($rootScope.activeMois+1)).then(function (response) {
        $scope.revenusDuMois = response.data;
        $scope.totalRevenus = setTotal($scope.revenusDuMois);
        $scope.revenusDisponibles = $scope.totalRevenus *0.9;
    }, function (reason) {
        console.log(reason);
    })

    //get categories depenses
    $http.get('http://localhost:8080/Budget2_war_exploded/rest/ws/budgets/'+($rootScope.activeMois+1)).then(function (response) {
        $scope.budgets = response.data;
        $scope.getTotalDisponible();
    }, function (reason) {
        console.log(reason);
    })

    $scope.majBudgets = function () {
        console.log('Executing maj Budgets')
        if ($scope.getTotalDisponible() >= 0){
            //execute the webService
            $http({method:'PUT',url:'http://localhost:8080/Budget2_war_exploded/rest/ws/budgets/'+($rootScope.activeMois+1),data: angular.toJson($scope.budgets)})
                .success(function(response) {
                    console.log(response);
                    $location.path("#/mois");
                }).error(function(reason) {
                console.log('error: '+reason);
            });
        } else {
            return 'Sum of ranges is over 100';
        }
    };
    
    $scope.getTotalDisponible = function () {
        var total = 0;
        total=0;
        for(var i = 0; i < $scope.budgets.length; i++){
            //var product = $scope.cart.products[i];
            total += $scope.budgets[i].pourcentageDuRevenus/1;
        }
        return 100-total;
    }

});