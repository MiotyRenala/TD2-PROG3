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
                "ingredient.category, ingredient.required_quantity from ingredient INNER JOIN dish on ingredient.id_dish =" +
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
                Double requiredQuantity = rs.getDouble("required_quantity");
                Integer id_dish = rs.getInt("id");
                Ingredient ingredients = new Ingredient(id_ingredient, name, price, categoryEnum, requiredQuantity);
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
        Ingredient ingredient = null;
        String sql = "SELECT d.id, d.name, d.dish_type, i.name, i.required_quantity from Dish d  " +
                "INNER JOIN Ingredient i  on i.id_dish = d.id where d.id = ?";
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
                String nameIngredient = rs.getString("name");


                dishById = new Dish(id, nameDish, dishTypeEnum, getIngredientfromId(id));
                dishById.setTotalCost(dishById.getDishCost());


            }
            System.out.println(dishById);

            conn.close();

            return dishById;
        } catch (SQLException e) {
            throw e;
        }
    }



    public List<Ingredient> findIngredient(int page, int size){
        String sql = "Select id, name , price, category, required_quantity from ingredient order by id OFFSET ? " +
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

    public List<Dish> getAllDish() throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        String sql = "SELECT id, name, dish_type from dish ";
        List<Dish> dishList = new ArrayList<>();
        Dish dish = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String nameofDish = rs.getString("name");
                String TypeOfDish = rs.getString("tyeOfDish");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(TypeOfDish);

                dish = new Dish(id, nameofDish, dishTypeEnum);
                dishList.add(dish);

            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return dishList;

    }

    public List<Ingredient> createIngredient2(List<Ingredient> newIngredient) throws SQLException{
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        String sql = "INSERT INTO Ingredient(id, name, price, category, id_dish) VALUES(?,?,?,?::ingredient_category,?)";
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);{
                conn.setAutoCommit(false);

                for(Ingredient i : newIngredient ){
                    if(getAllIngredient().contains(i.getName())){

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

//    public Dish saveDish (Dish dishToSave) throws SQLException {
//        DBConnection db = new DBConnection();
//        Connection conn = db.getDBConnection();
//        Dish existingDish = getDishByCriteria(dishToSave.getName(), dishToSave.getDishType());
//
//        if(existingDish == null){
//            PreparedStatement stmt = conn.prepareStatement("INSERT INTO dish (id, name, " +
//                    "dish_type) VALUES (?,?,?::type_of_dishes)");
//            stmt.setInt(1,dishToSave.getId());
//            stmt.setString(2, dishToSave.getName());
//            stmt.setString(3, dishToSave.getDishType().name());
//            stmt.executeUpdate();
//        } else if (existingDish != null) {
//            PreparedStatement stmt = conn.prepareStatement("UPDATE dish set name = ?, dish_type" +
//                    " = ? where id = ?");
//            stmt.setString(1, dishToSave.getName());
//            stmt.setString(2, dishToSave.getDishType().name());
//            stmt.setInt(3, existingDish.getId());
//            stmt.executeUpdate();
//
//        }
//        PreparedStatement stmt = conn.prepareStatement("UPDATE ingredient set dish_id = NULL where dish_id = ?");
//        stmt.setInt(1,dishToSave.getId());
//        stmt.executeUpdate();
//
//        for(Ingredient i : dishToSave.getIngredient()){
//            if(i.getDish().getId() == null){
//                PreparedStatement stmtToInsert = conn.prepareStatement("INSERT INTO ingredient (id, name, price, " +
//                        "category, id_dish) VALUES (?,?,?,?::ingredient_category,?)");
//                stmtToInsert.setInt(1, i.getId());
//                stmtToInsert.setString(2, i.getName());
//                stmtToInsert.setDouble(3, i.getPrice());
//                stmtToInsert.setString(4, i.getCategory().name());
//                stmtToInsert.setInt(5,dishToSave.getId());
//                stmtToInsert.executeUpdate();
//            }
//            if(i.getDish().getId()!= null){
//                PreparedStatement stmtToUpdate = conn.prepareStatement("UPDATE ingredient set dish_id = ? where " +
//                        "dish_id = ?");
//                stmtToUpdate.setInt(1, dishToSave.getId());
//                stmtToUpdate.setInt(2, i.getId());
//                stmtToUpdate.executeUpdate();
//            }
//        }
//        return  dish;
//
//
//    }
//
//    public Dish saveDish2(Dish dishToSave) throws SQLException {
//        DBConnection db = new DBConnection();
//        Connection conn = db.getDBConnection();
//        Dish existingDish = null;
//
//        String sqlUpdateExistingDish = "UPDATE dish set name = ? , dish_type = ?  where id = ?";
//        String sqlUpdateSetDishToNull = "UPDATE ingredient set dish_id = null  where dish_id = ? ";
//        String sqlInsertIngredient = "INSERT INTO ingredient (id, name, price, category, dish_id) " +
//                        "VALUES (?, ?, ?, ? :: ingredient_category, ?)";
//
//        String sqlUpdateIngredient = "UPDATE ingredient SET name = ?, price = ?, category = CAST(? AS ingredient_category), dish_id = ? " +
//                        "WHERE id = ?";
//
//        String sqlInsertDish = "INSERT dish (id, name, dish_type) VALUES (?, ? , ?::type_of_dishes";
//
//
//        List<Dish> listOfAllDish = getAllDish();
//
//        for(Dish d : listOfAllDish){
//            if ((d.getId() == dishToSave.getId()) || (d.getName() == dishToSave.getName() && d.getDishType() == dishToSave.getDishType()) ) {
//                existingDish = d;
//                break;
//            }
//        }
//        if(existingDish == null){
//
//        }
//        if(existingDish != null){
//            dishToSave.setId(existingDish.getId());
//            PreparedStatement stmtUpdateExistingDish = conn.prepareStatement(sqlUpdateExistingDish);
//            stmtUpdateExistingDish.setString(1, dishToSave.getName());
//            stmtUpdateExistingDish.setString(2, dishToSave.getDishType().name());
//            stmtUpdateExistingDish.setInt(3,dishToSave.getId());
//            stmtUpdateExistingDish.executeUpdate();
//
//            if(dishToSave.getIngredient().isEmpty()){
//                PreparedStatement stmtSetIngredientToNull = conn.prepareStatement(sqlUpdateSetDishToNull);
//                stmtSetIngredientToNull.setInt(1, dishToSave.getId());
//                stmtSetIngredientToNull.executeUpdate();
//
//            }
//            for (Ingredient ingredient : dishToSave.getIngredient()) {
//
//                if (ingredient.getId() != null) {
//                    PreparedStatement stmtUpdateIngredient = conn.prepareStatement(sqlUpdateIngredient);
//                    stmtUpdateIngredient.setString(1, ingredient.getName());
//                    stmtUpdateIngredient.setDouble(2, ingredient.getPrice());
//                    stmtUpdateIngredient.setString(3, ingredient.getCategory().name());
//                    stmtUpdateIngredient.setInt(4, dishToSave.getId());
//                    stmtUpdateIngredient.setInt(5, ingredient.getId());
//                    stmtUpdateIngredient.executeUpdate();
//
//                } else {
//
//                    PreparedStatement stmtInsertIngredient = conn.prepareStatement(sqlInsertIngredient);
//                    stmtInsertIngredient.setInt(1, ingredient.getId());
//                    stmtInsertIngredient.setString(2, ingredient.getName());
//                    stmtInsertIngredient.setDouble(3, ingredient.getPrice());
//                    stmtInsertIngredient.setString(4, ingredient.getCategory().name());
//                    stmtInsertIngredient.setInt(5, dishToSave.getId());
//                    stmtInsertIngredient.executeUpdate();
//                }
//
//            }
//        }
//
//    }
//
//    public Dish saveDishFinal(Dish dishToSave) throws SQLException {
//        DBConnection db = new DBConnection();
//        Connection connection = db.getDBConnection();
//        try {
//            String isAlreadyInDBQuery = "SELECT name FROM dish WHERE name = ?";
//            PreparedStatement isAlreadyInDBPreparedStmt = connection.prepareStatement(isAlreadyInDBQuery);
//            isAlreadyInDBPreparedStmt.setString(1, dishToSave.getName());
//            ResultSet resultSet = isAlreadyInDBPreparedStmt.executeQuery();
//            if (resultSet.next()) {
//                String updateDishQuery = "UPDATE dish SET id = ?, name = ?, dish_type = ?";
//                PreparedStatement updateDishPreparedStmt = connection.prepareStatement(updateDishQuery);
//                updateDishPreparedStmt.setInt(1, dishToSave.getId());
//                updateDishPreparedStmt.setString(2, dishToSave.getName());
//                updateDishPreparedStmt.setObject(3, dishToSave.getDishType(), Types.OTHER);
//
//                String updateIngredientQuery = "UPDATE ingredient SET id = ?, name = ?, price = ?, category = ? WHERE id = ?";
//                PreparedStatement updateIngredientPreparedStmt = connection.prepareStatement(updateIngredientQuery);
//                for (Ingredient ingredient: dishToSave.getIngredient()) {
//                    updateIngredientPreparedStmt.setInt(1, ingredient.getId());
//                    updateDishPreparedStmt.setString(2, ingredient.getName());
//                    updateIngredientPreparedStmt.setDouble(3, ingredient.getPrice());
//                    updateDishPreparedStmt.setObject(4, ingredient.getCategory(), Types.OTHER);
//                    updateIngredientPreparedStmt.setInt(5, dishToSave.getId());
//                }
//                int affectedRows = updateDishPreparedStmt.executeUpdate();
//                return affectedRows > 0 ? "Dish avec les ingrédients mis à jour " + dishToSave.getIngredients().getFirst().getName() : "Aucun dish mis à jour";
//            }
//            String insertDishQuery = "INSERT INTO dish(id, name, dish_type, ingredients) VALUES (?, ?, ?, ?)";
//            PreparedStatement insertDishPreparedStmt = connection.prepareStatement(insertDishQuery);
//            insertDishPreparedStmt.setInt(1, dishToSave.getId());
//            insertDishPreparedStmt.setString(2, dishToSave.getName());
//            insertDishPreparedStmt.setString(3, String.valueOf(dishToSave.getDishType()));
//            insertDishPreparedStmt.setObject(4, dishToSave.getIngredient());
//            int affectedRows = insertDishPreparedStmt.executeUpdate();
//            return affectedRows > 0 ? "Dish (" + dishToSave.getName() + ") créé avec succès contenant l’ingrédient Oignon" : "Aucun dish mis a jour";
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            connection.close();
//        }
//
//    }
//
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
