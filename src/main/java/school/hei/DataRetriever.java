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

    public List<String> getAllIngredient() throws SQLException{
        DBConnection db = new DBConnection();
        String sql = "Select id, name, price, category, id_dish from ingredient";
        List<String> ingredientListName = new ArrayList<>();
        try{
            Connection conn = db.getDBConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String name = rs.getString("name");

                ingredientListName.add(name);

            }

            conn.close();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientListName;
    }


    public List<Ingredient> createIngredient(List<Ingredient> newIngredient) throws SQLException{
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        String sql = "INSERT INTO Ingredient(id, name, price, category, id_dish) VALUES(?,?,?,?::ingredient_category,?)";
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);{
                conn.setAutoCommit(false);

                for(Ingredient i : newIngredient ){
                    if(getAllIngredient().contains(i.getName())){
                        conn.rollback();
                        System.out.println("Cet Ingredient existe déjà");
                        throw new RuntimeException("Cet Ingredient existe déjà");
                    }else {
                        stmt.setInt(1, i.getId());
                        stmt.setString(2, i.getName());
                        stmt.setDouble(3, i.getPrice());
                        stmt.setString(4, i.getCategory().name());
                        stmt.setInt(5,i.getDish().getId());
                        stmt.addBatch();
                        ingredients.add(i);

                    }


                }
                stmt.executeBatch();
                conn.commit();
                System.out.println("Insertion réussie");

                conn.close();

            }




        }
        catch (Exception e){
            conn.rollback();
            throw new RuntimeException(e);

        }
        return ingredients;
    }

    public List<Ingredient>createIngredient2(List<Ingredient> newIngredient) throws SQLException {
        DBConnection db = new DBConnection();
        String sql = "INSERT INTO ingredient(id, name, price, category, id_dish) VALUES(?, ?, ?, ?::ingredient_category , ?)";
        Connection conn = db.getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        List<Ingredient> ingredientList = new ArrayList<>();
        conn.setAutoCommit(false);
        try {
            for (Ingredient i : newIngredient) {
                if (getAllIngredient().contains(i.getName())) {
                    conn.rollback();
                    System.out.println("Cet Ingredient existe déjà");
                    throw new RuntimeException("Cet Ingredient existe déjà");
                } else {
                    stmt.setInt(1, i.getId());
                    stmt.setString(2, i.getName());
                    stmt.setDouble(3, i.getPrice());
                    stmt.setString(4, i.getCategory().name());
                    stmt.setInt(5, i.getDish().getId());
                    stmt.addBatch();
                    ingredientList.add(i);
                }
                stmt.executeUpdate();
                conn.commit();
                System.out.println("Insertion réussie");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;
    }


    //FUNCTION TO GET DISH BY PROVIDING ITS NAME
    public Dish getDishByName(String name) throws SQLException {
        DBConnection db = new DBConnection();
        String sql = "SELECT dish.id, dish.name, dish.dish_type from dish where name = ?";
        Connection conn = db.getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            Integer id = rs.getInt("id");
            String nameinDish = rs.getString("name");
            String dishType = rs.getString("dishType");
            DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dishType);

            Dish dish = new Dish(id, nameinDish, dishTypeEnum);

        }
        return dish;


    }
    //INSERTION
    public Dish tryINSERT(Dish dish){
        DBConnection db = new DBConnection();
        String sql = "INSERT INTO Dish(id, name, dish_type ) VALUES (?,?,?::type_of_dishes)";

        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, dish.getId());;
            stmt.setString(2, dish.getName());
            stmt.setString(3, String.valueOf(dish.getDishType()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dish;
    }





    public Dish saveDish(Dish dishToSave) throws SQLException {
        DBConnection db = new DBConnection();
        String sql = "INSERT INTO dish(id, name, dish_type) VALUES (?,?,?)";
        Dish dish = null;
        try{
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, dishToSave.getId());
            stmt.setString(2, dishToSave.getName());
            stmt.setString(3, dishToSave.getDishType().name());

            PreparedStatement stmt2 = conn.prepareStatement("SELECT dish.id, dish.name, dish.dish_type" +
                    "from dish where id = ?");
            ResultSet rs = stmt2.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt(dishToSave.getId());
                String name = rs.getString(dishToSave.getName());
                String dishType = rs.getString(dishToSave.getDishType().name());
                if()
            }
        }


    }

    public List<Dish> findDishsByIngredientName(String IngredientName){
        DBConnection db = new DBConnection();
        String sql = "SELECT dish.id, dish.name, dish.dish_type from dish INNER JOIN ingredient on dish.id =" +
                "ingredient.id_dish where ingredient.name ilike ?";
        List<Dish> dishInHere = new ArrayList<Dish>();

        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%"+IngredientName+"%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String dish_type = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);
                Dish dishofIngredient = new Dish(id, name, dishTypeEnum);
                dishInHere.add(dishofIngredient);
            }

            System.out.println(dishInHere);

            
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishInHere;
    }

    public List<Ingredient> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page,
                                                     int size){
        DBConnection db = new DBConnection();
        String sql = "SELECT ingredient.id, ingredient.name, ingredient.price, ingredient.category from ingredient inner join dish" +
                " on ingredient.id_dish = dish.id where ingredient.name = ? and ingredient.category = ? :: ingredient_category and" +
                " dish.name = ? LIMIT ? OFFSET ?";
        List<Ingredient> ingredientList = new ArrayList<>();
        System.out.println(String.valueOf(category));
        try{
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ingredientName);
            stmt.setString(2, category.name());
            stmt.setString(3, dishName);
            stmt.setInt(4, size);
            stmt.setInt(5, (page-1)*size);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(name);
                Double price = rs.getDouble("price");
                String categoryString = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(categoryString);

                Ingredient toIngredientList = new Ingredient(id, name, price, categoryEnum);

                ingredientList.add(toIngredientList);

            }
            System.out.println(ingredientList.size());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;

    }

    //DELETE INGREDIENT
    public void deleteIngredientbyName(String name){
        DBConnection db = new DBConnection();
        String sql = "DELETE from ingredient where name = ?";
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Suppresion réussie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    //DELETE

    public void tryDELETEbyid(String name){
        DBConnection db = new DBConnection();
        String sql = "DELETE from dish where name = ?";
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Suppression réussie");




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //UPDATE
    public void tryUPDATE(String newName, int id){
        DBConnection db = new DBConnection();
        String sql = "UPDATE dish set name = ? where id = ?";

        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt= conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setInt(2,id);

            stmt.executeUpdate();
            System.out.println("UPDATE Succeed");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
