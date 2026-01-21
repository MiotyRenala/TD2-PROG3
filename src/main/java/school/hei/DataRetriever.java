package school.hei;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private Dish dish;
    private Ingredient ingredient;

    public List<Ingredient> getIngredientfromId(Integer id) {
        List<Ingredient> ingredientList = new ArrayList<>();
        String sql = "SELECT ingredient.id,ingredient.name, ingredient.price," +
                "ingredient.category, dishIngredient.quantity_required from dish INNER JOIN dishIngredient on dishIngredient.id_dish =" +
                "dish.id INNER JOIN ingredient on ingredient.id = dishIngredient.id_ingredient where dish.id = ?";
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id_ingredient = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
                Double quantityRequired = rs.getDouble("quantity_required");
                Ingredient ingredients = new Ingredient(id_ingredient, name, price, categoryEnum, quantityRequired);
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


    public Dish findDishById(Integer id) throws SQLException, RuntimeException {
        List<Ingredient> ingredientList = new ArrayList<>();
        Ingredient ingredient = null;
        String sql = "SELECT d.id, d.name, d.dish_type,d.selling_price, i.name from dish d  " +
                "INNER JOIN dishIngredient dI on dI.id_dish = d.id INNER JOIN ingredient i on " +
                "dI.id_ingredient = i.id where d.id = ?";
        Dish dishById = null;
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String nameDish = rs.getString("name");
                String dish_type = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);
                Double sellingPrice = rs.getDouble("selling_price");
                System.out.println(sellingPrice);

                dishById = new Dish(id, nameDish, dishTypeEnum, sellingPrice, getIngredientfromId(id));
                dishById.setTotalCost(dishById.getDishCost());


            }
            System.out.println(dishById);

            conn.close();

            return dishById;
        } catch (SQLException e) {
            throw e;
        }
    }


    public List<Ingredient> findIngredient(int page, int size) {
        String sql = "Select id, name , price, category, required_quantity from ingredient order by id OFFSET ? " +
                "LIMIT ?  ";
        List<Ingredient> ingredient = new ArrayList<Ingredient>();
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (page - 1) * size);
            stmt.setInt(2, size);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
                Double required_quantity = rs.getDouble("required_quantity");
                Ingredient ingredientFinal = new Ingredient(id, name, price, categoryEnum, required_quantity);
                ingredient.add(ingredientFinal);
            }
            System.out.println(ingredient);

            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredient;

    }


    //BEFORE CREATING AN INGREDIENT

    public List<String> getAllIngredient() throws SQLException {
        DBConnection db = new DBConnection();
        String sql = "Select id, name, price, category from ingredient";
        List<String> ingredientListName = new ArrayList<>();
        try {
            Connection conn = db.getDBConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("name");

                ingredientListName.add(name);

            }

            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientListName;
    }


    public List<Ingredient> createIngredient(List<Ingredient> newIngredient) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        String sql = "INSERT INTO Ingredient(id, name, price, category) VALUES(?,?,?,?::ingredient_category)";
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            {
                conn.setAutoCommit(false);

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
                        stmt.addBatch();
                        ingredients.add(i);

                    }


                }
                stmt.executeBatch();
                conn.commit();
                System.out.println("Insertion réussie");

                conn.close();

            }

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);

        }
        return ingredients;
    }


    public List<Dish> getAllDish() throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        String sql = "SELECT id, name, dish_type, selling_price from dish ";
        List<Dish> dishList = new ArrayList<>();
        Dish dish = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nameofDish = rs.getString("name");
                String TypeOfDish = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(TypeOfDish);
                Double sellingPrice = rs.getDouble("selling_price");

                dish = new Dish(id, nameofDish, dishTypeEnum, sellingPrice);
                dishList.add(dish);


            }
            System.out.println(dishList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return dishList;

    }
}


     //Pour sauvegarder un plat
    /**
     * Liste des apperçus:
     * => Parcourir la liste de Dish déjà existant :
     *        Si le dish existant = nouveau Dish (nom et id) : ALORS BREAK
     *                 Regarder la liste des ingrédients (parcourir) :
     *                      Si la liste est vide : dissocier l'ingrédient au plat
     *                      Si la liste existe et que les ingrédients existent : mettre à jour la liste
     *                      Si la liste existe et que les igrédients n'existent pas encore : le créer
     *        Si le dish n'existe pas encore : le créer
     *                  Regarder la liste des ingrédients (parcourir) :
     *      *                      Si la liste est vide : dissocier l'ingrédient au plat
     *      *                      Si la liste existe et que les ingrédients existent : mettre à jour la liste
     *      *                      Si la liste existe et que les igrédients n'existent pas encore : le créer*/

   /** public Dish SaveDish ( Dish dishToSave) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        List<Dish> ExistListOfDish = getAllDish();
        List<String> ExistingIngredientName = getAllIngredient();
        String UpdateDish = "UPDATE dish set name = ? , dish_type = ? where name = ? ";
        String UpdateIngredient = "UPDATE ingredient set dish_type = null where dish_type = ? ";

        try{
            for(Dish d : ExistListOfDish){
                if((d.getName() == dishToSave.getName()) && ((d.getDishType() == dishToSave.getDishType()))){
                    PreparedStatement stmtUpdateDish = conn.prepareStatement(UpdateDish);
                    stmtUpdateDish.setString(1, dishToSave.getName());
                    stmtUpdateDish.setString(2, dishToSave.getDishType().name());
                    stmtUpdateDish.setString(3, d.getName());
                    stmtUpdateDish.executeUpdate();
                    if (d.getIngredient().isEmpty()) {
                        PreparedStatement stmtUpdateIngredient = conn.prepareStatement(UpdateIngredient);
                        stmtUpdateIngredient.setInt(1,d.getId());
                        stmtUpdateDish.executeUpdate();


                    }
                }
            }

        }

    }
*/



 /**   public List<Dish> findDishsByIngredientName(String IngredientName){
        DBConnection db = new DBConnection();
        String sql = "SELECT dish.id, dish.name, dish.dish_type, dish.selling_price from dish INNER JOIN dishIngredient on dish.id =" +
                "dishIngredient.id_dish INNER JOIN ingredient on ingredient.id = dishIngredient.id_ingredient where ingredient.name ilike ?";
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
                Double sellingPrice = rs.getDouble("selling_price");
                Dish dishofIngredient = new Dish(id, name, dishTypeEnum, sellingPrice);
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
*/