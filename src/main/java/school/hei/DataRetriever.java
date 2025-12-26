package school.hei;

import java.sql.*;

public class DataRetriever {
    private Dish dish;
    private Ingredient ingredient;

    public Dish findDishById(Integer id){
        String sql = "SELECT id, name, dish_type from Dish where id = ?";
        Dish dishById = null;
        DBConnection db = new DBConnection();
        try{
            Connection conn = db.getDBConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String name =rs.getString("name");
                String dish_type = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);


                dishById = new Dish(id, name, dishTypeEnum);
                System.out.println(dishById);
            }





            conn.close();

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
