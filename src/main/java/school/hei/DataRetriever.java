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
                Integer id_dish = rs.getInt("id");
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


    public Dish findDishById(Integer id) throws SQLException,RuntimeException {
        List<Ingredient> ingredientList = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.dish_type, i.name from Dish d " +
                "INNER JOIN Ingredient i  on i.id_dish = d.id where d.id = ?";
        Dish dishById = null;
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException();

            }
            while (rs.next()) {
                String nameDish = rs.getString("name");
                String dish_type = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);


                dishById = new Dish(id, nameDish, dishTypeEnum, getIngredientfromId(id));

            }
            System.out.println(dishById);

            conn.close();

            return dishById;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Ingredient> findIngredient(int page, int size){
        String sql = "Select id, name , price, category from ingredient order by id OFFSET ? " +
                "LIMIT ?  ";
        List<Ingredient> ingredient = new ArrayList<Ingredient>();
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,(page-1)*size);
            stmt.setInt(2,size);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);

                Ingredient ingredientFinal = new Ingredient(id, name, price, categoryEnum);
                ingredient.add(ingredientFinal);
            }
            System.out.println(ingredient);

            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredient;

    }

    public List<Ingredient> getAllIngredient() throws SQLException{
        DBConnection db = new DBConnection();
        String sql = "Select id, name, price, category, id_dish from ingredient";
        List<Ingredient> ingredientList = new ArrayList<>();
        try{
            Connection conn = db.getDBConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);

                Ingredient ingredient = new Ingredient(id, name, price, categoryEnum);
                ingredientList.add(ingredient);

            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;
    }


    public List<Ingredient> createIngredient(List<Ingredient> newIngredient) throws SQLException{
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO Ingredient(id, name, price, category, id_dish) VALUES(?,?,?,?::ingredient_category,?)";
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(Ingredient i : newIngredient ){
                stmt.setInt(1, i.getId());
                stmt.setString(2, i.getName());
                stmt.setDouble(3, i.getPrice());
                stmt.setString(4, i.getCategory().name());
                stmt.setInt(5, i.getDish().getId());
                stmt.addBatch();
                ingredients.add(i);
                stmt.executeBatch();

                for(Ingredient ingredient : getAllIngredient()){
                    if(i == ingredient){
                        conn.rollback();
                    }else {
                        conn.commit();
                    }
                }
            }





            System.out.println("Test d'insertion réussie");


        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return ingredients;
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
