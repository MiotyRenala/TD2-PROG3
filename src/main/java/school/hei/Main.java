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
            dr.findDishById(1);
            //dr.findDishById(999);
            dr.findIngredient(2,2);
            dr.findIngredient(3,5);



//            List<Ingredient> ingredients = new ArrayList<>();
//            Dish bleDish = new Dish(2);
//            Dish haricotDish = new Dish(1);
//            Ingredient ble = new Ingredient(10, "blé", 2000, CategoryEnum.VEGETABLE, bleDish);
//            Ingredient  haricot = new Ingredient( 9,"Haricot", 4500, CategoryEnum.VEGETABLE, haricotDish);
//            ingredients.add(ble);
//            ingredients.add(haricot);
//
//            dr.createIngredient(ingredients);

            /**List<Ingredient> ingredients = new ArrayList<>();
            Dish soupeDeLegume = new Dish(7);
            Ingredient Oignon = new Ingredient(9, "Oignon", 4000, CategoryEnum.VEGETABLE, soupeDeLegume);

            Dish pouletFarci = new Dish(6, "Poulet farci", DishTypeEnum.MAIN);
            dr.saveDish(pouletFarci);*/

            dr.findDishsByIngredientName("%EUR%");


        } catch (Exception e) {
            e.printStackTrace();// sout pour les exceptions
            throw new RuntimeException(e);
        }




    }
}