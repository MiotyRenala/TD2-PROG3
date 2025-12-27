package school.hei;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private Dish dish;
    private Ingredient ingredient;

    public List<Ingredient> getIngredientfromId(Integer id){
        List<Ingredient> ingredientList = new ArrayList<>();
        String sql = "SELECT ingredient.id,ingredient.name, ingredient.price," +
                "ingredient.category from ingredient INNER JOIN dish on ingredient.id_dish =" +
                "dish.id where dish.id = ?";
        DBConnection db = new DBConnection();
        try{
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Integer id_ingredient = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
                Ingredient ingredients = new Ingredient(id_ingredient, name, price, categoryEnum);
                ingredientList.add(ingredients);

            }

            rs.close();
            stmt.close();
            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;
    }


    public Dish findDishById(Integer id){
        List<Ingredient> ingredientList = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.dish_type, i.name from Dish d " +
                "INNER JOIN Ingredient i  on i.id_dish = d.id where d.id = ?";
        Dish dishById = null;
        DBConnection db = new DBConnection();
        try{
            Connection conn = db.getDBConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String nameDish =rs.getString("name");
                String dish_type = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);



                dishById = new Dish(id, nameDish, dishTypeEnum, getIngredientfromId(id));

            }
            System.out.println(dishById);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishById;
    }

    public DataRetriever() {

    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
