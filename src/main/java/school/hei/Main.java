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

            List<Ingredient> ingredients = new ArrayList<>();
            Dish patateDish = new Dish(1);
            Dish fromageDish = new Dish(1);
            Ingredient patate = new Ingredient(6, "patate", 700, CategoryEnum.VEGETABLE, patateDish);
            Ingredient Fromage = new Ingredient( 7,"Fromage", 3900, CategoryEnum.DAIRY, fromageDish);
            ingredients.add(patate);
            ingredients.add(Fromage);

            dr.createIngredient(ingredients);


        } catch (Exception e) {
            e.printStackTrace();// sout pour les exceptions
            throw new RuntimeException(e);
        }




    }
}