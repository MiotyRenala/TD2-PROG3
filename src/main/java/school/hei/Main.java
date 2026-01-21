package school.hei;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutt
public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            System.out.println("Welcome Mioty");

            DataRetriever dr = new DataRetriever();
            System.out.println("Coût total : "+ dr.findDishById(2).getTotalCost());
            //dr.findDishById(999);
            dr.findIngredient(2,2);
            dr.findIngredient(3,5);
            dr.getAllDish();



//            List<Ingredient> ingredients = new ArrayList<>();
//            Dish bleDish = new Dish(2);
//            Dish haricotDish = new Dish(1);
//            Ingredient ble = new Ingredient(10, "blé", 2000, CategoryEnum.VEGETABLE, bleDish);
//            Ingredient  haricot = new Ingredient( 9,"Haricot", 4500, CategoryEnum.VEGETABLE, haricotDish);
//            ingredients.add(ble);
//            ingredients.add(haricot);
//
//            dr.createIngredient(ingredients);

            //List<Ingredient> ingredients = new ArrayList<>();
            /*Dish soupeDeLegume = new Dish(7);
            Ingredient Oignon = new Ingredient(9, "Oignon", 4000, CategoryEnum.VEGETABLE, soupeDeLegume);*/

            /*Dish pouletFarci = new Dish(6, "Poulet farci", DishTypeEnum.MAIN);
            dr.saveDish(pouletFarci);*/

            //dr.findDishsByIngredientName("%EUR%");
            //dr.findIngredientByCriteria("Poulet", CategoryEnum.ANIMAL, "Poulet Grillé", 1,10);
            /*Dish PorcFarci = new Dish(8, "Porc Farci", DishTypeEnum.MAIN);
            dr.tryINSERT(PorcFarci);*/
            //dr.deleteIngredientbyName("Salad");
            //dr.tryUPDATE("Porc au beurre", 8);
            /*Dish dishCharcuterie = new Dish(1);
            Dish Laitue2 = new Dish(1);
            Ingredient Laitue = new Ingredient(12, "Salad", 2500, CategoryEnum.VEGETABLE, Laitue2);
            Ingredient Charcuterie = new Ingredient(11, "Charcuterie", 3900, CategoryEnum.ANIMAL, dishCharcuterie);
            List<Ingredient> ingredientList = new ArrayList<>();
            ingredientList.add(Laitue);
            ingredientList.add(Charcuterie);
            dr.createIngredient2(ingredientList);*/
            /*List<Ingredient> ingredientPorcAuMiel = new ArrayList<>();
            Dish ViandedePorc = new Dish(9);
            Ingredient Viande = new Ingredient(12, "Viande", 5000, CategoryEnum.ANIMAL, ViandedePorc);
            ingredientPorcAuMiel.add(Viande);
            ingredientPorcAuMiel.add(Oignon);

            Dish PorcAuMiel = new Dish(9, "Porc au Miel", DishTypeEnum.MAIN, ingredientPorcAuMiel);
            dr.saveDish(PorcAuMiel);*/



        } catch (Exception e) {
            e.printStackTrace();// sout pour les exceptions
            throw new RuntimeException(e);
        }




    }
}