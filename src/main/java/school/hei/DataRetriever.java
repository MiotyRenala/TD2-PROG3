package school.hei;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataRetriever {
    private Dish dish;
    private Ingredient ingredient;

    public List<DishIngredient> getdishIngredientFromid_dish(Integer id_dish) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        String sql = " select dI.id, dI.id_dish, dI.id_ingredient, dI.quantity_required, dI.unit, d.dish_type, d.selling_price," +
                " i.name, i.price, i.category from ingredient i inner join dishIngredient dI on dI.id_ingredient = i.id " +
                " inner join dish d on dI.id_dish = d.id where id_dish = ?";
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_dish);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");

                Integer idDish = rs.getInt("id_dish");
                String nameDish = rs.getString("name");
                String DishType = rs.getString("dish_type");
                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(DishType);
                Double sellingPrice = rs.getDouble("selling_price");
                Dish dish = new Dish(idDish, nameDish, dishTypeEnum, sellingPrice);

                Integer idIngredient = rs.getInt("id_ingredient");
                String nameIngredient = rs.getString("name");
                Double priceIngredient = rs.getDouble("price");
                String category = rs.getString("category");
                CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
                Ingredient ingredient = new Ingredient(idIngredient, nameIngredient, priceIngredient, categoryEnum);

                Double quantityRequired = rs.getDouble("quantity_required");
                String unit = rs.getString("UNIT");
                UnitEnum unitEnum = UnitEnum.valueOf(unit);
                DishIngredient dishIngredient = new DishIngredient(id, dish, ingredient, quantityRequired, unitEnum);
                dishIngredientList.add(dishIngredient);
            }
            System.out.println(dishIngredientList);


            rs.close();
            stmt.close();
            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredientList;
    }


    public Dish findDishById(Integer id) throws SQLException, RuntimeException {
        List<Ingredient> dishIngredientList = new ArrayList<>();
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

                dishById = new Dish(id, nameDish, dishTypeEnum, sellingPrice, getdishIngredientFromid_dish(id));
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
        String sql = "Select id, name , price, category from ingredient order by id OFFSET ? " +
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


    //BEFORE CREATING AN INGREDIENT

    public List<String> getAllIngredientName() throws SQLException {
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
                    if (getAllIngredientName().contains(i.getName())) {
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

    // Avoir la liste de tous les plats existants
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
            return dishList;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


    }

    //Vérifier si un plat existe
    public boolean existingDish(Dish dish) throws SQLException {
        if (getAllDish().contains(dish)) {
            return true;
        }
        return false;
    }

    public boolean existingIngredient(Ingredient ingredient) throws SQLException {
        if (getAllIngredientName().contains(ingredient.getName())) {
            return true;
        }
        return false;
    }

    public List<DishIngredient> getDishIngredient(Dish dish) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        List<DishIngredient> dishIngredientsList = new ArrayList<>();
        String sql = "SELECT id, id_dish, id_ingredient, quantity_required, unit, i.name from dishIngredient dI " +
                "inner join dish d on dI.id_dish = d.id " +
                "inner join ingredient i on dI.id_ingredient = i.id where d.id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, dish.getId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer idDish = rs.getInt("id_dish");
            Dish dish1 = new Dish(idDish);

            Integer idIngredient = rs.getInt("id_ingredient");
            Ingredient ingredient = new Ingredient(idIngredient);

            Double quantityRequired = rs.getDouble("quantity_required");
            String unit = rs.getString(("unit"));
            UnitEnum unitEnum = UnitEnum.valueOf(unit);

            DishIngredient dishIngredient = new DishIngredient(id, dish1, ingredient, quantityRequired, unitEnum);
            dishIngredientsList.add(dishIngredient);
        }
        return dishIngredientsList;
    }


    public List<Ingredient> getIngredientListFromDish(Dish dish) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();
        List<Ingredient> ingredientList = new ArrayList<>();
        String sql = " select i.id, i.name from dishIngredient dI inner join ingredient i on i.id = " +
                "dI.id_ingredient inner join dish d on d.id = dI.id_dish where d.id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, dish.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Ingredient ingredient = new Ingredient(id, name);
                ingredientList.add(ingredient);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;

    }

    public void insertDishIngredient(DishIngredient dishIngredient) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        String sql = "INSERT INTO dishIngredient(id_dish, id_ingredient, quantity_required, unit) VALUES(?,?,?,?::unit_type)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            {
//                stmt.setInt(1, dishIngredient.getId());
                stmt.setInt(1, dishIngredient.getDish().getId());
                stmt.setInt(2, dishIngredient.getIngredient().getId());
                stmt.setDouble(3, dishIngredient.getQuantity_required());
                stmt.setString(4, dishIngredient.getUnit().name());


                stmt.executeUpdate();

                conn.close();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    public Ingredient insertIngredient(Ingredient ingredient) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        String sql = "INSERT INTO dishIngredient(name, price, category) VALUES(?,?,?,? :: ingredient_category)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            {
                stmt.setString(1, ingredient.getName());
                stmt.setDouble(2, ingredient.getPrice());
                stmt.setString(3, ingredient.getCategory().name());


                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int GeneratedKey = rs.getInt(1);
                    ingredient.setId(GeneratedKey);
                }

                conn.close();

            }

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);

        }
        return ingredient;

    }


    public Dish saveDish(Dish dishToSave) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        if (existingDish(dishToSave)) {
            PreparedStatement updateDish = conn.prepareStatement("UPDATE dish set name = ?, dish_type = ?, selling_price = ?" +
                    " where id = ? ");
            updateDish.setString(1, dishToSave.getName());
            updateDish.setString(2, dishToSave.getDishType().name());
            updateDish.setDouble(3, dishToSave.getSellingPrice());
            updateDish.setInt(4, dishToSave.getId());
            updateDish.executeUpdate();
            for (int i = 0; i < dishToSave.getDishIngredients().size(); i++) {
                if (!existingIngredient(dishToSave.getDishIngredients().get(i).getIngredient())) {
                    insertIngredient(dishToSave.getDishIngredients().get(i).getIngredient());
                }

            }
            if (dishToSave.getDishIngredients().isEmpty()) {
                PreparedStatement deleteRelation = conn.prepareStatement("DELETE from dishIngredient where id_dish = ?");
                deleteRelation.setInt(1, dishToSave.getId());
                deleteRelation.executeUpdate();
            } else {
                PreparedStatement deleteRelation = conn.prepareStatement("DELETE from dishIngredient where id_dish = ?");
                deleteRelation.setInt(1, dishToSave.getId());
                deleteRelation.executeUpdate();
                List<DishIngredient> dishIngredients = dishToSave.getDishIngredients();
                for (int i = 0; i < dishIngredients.size(); i++) {
                    dishIngredients.get(i).setDish(dishToSave);
                    insertDishIngredient(dishIngredients.get(i));

                }

            }
        } else {
            PreparedStatement createDish = conn.prepareStatement("INSERT INTO dish (id, name, dish_type, selling_price) VALUES " +
                    "(?, ?, ? :: type_of_dishes, ?)");
            createDish.setInt(1, dishToSave.getId());
            createDish.setString(2, dishToSave.getName());
            createDish.setString(3, dishToSave.getDishType().name());
            createDish.setDouble(4, dishToSave.getSellingPrice());
            createDish.executeUpdate();

            for (int i = 0; i < dishToSave.getDishIngredients().size(); i++) {
                if (!existingIngredient(dishToSave.getDishIngredients().get(i).getIngredient())) {
                    insertIngredient(dishToSave.getDishIngredients().get(i).getIngredient());
                }
                dishToSave.getDishIngredients().get(i).setDish(dishToSave);
                insertDishIngredient(dishToSave.getDishIngredients().get(i));


            }

        }
        return dishToSave;


    }


    Ingredient saveIngredient(Ingredient toSave) {
        String upsertIngredientSql = """
                    INSERT INTO ingredient (id, name, price, category)
                    VALUES (?, ?, ?, ?::dish_type)
                    ON CONFLICT (id) DO UPDATE
                    SET name = EXCLUDED.name,
                        category = EXCLUDED.category,
                        price = EXCLUDED.price
                    RETURNING id
                """;

        try (Connection conn = new DBConnection().getDBConnection()) {
            conn.setAutoCommit(false);
            Integer ingredientId;
            try (PreparedStatement ps = conn.prepareStatement(upsertIngredientSql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                }
                if (toSave.getPrice() != null) {
                    ps.setDouble(2, toSave.getPrice());
                } else {
                    ps.setNull(2, Types.DOUBLE);
                }
                ps.setString(3, toSave.getName());
                ps.setString(4, toSave.getCategory().name());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    ingredientId = rs.getInt(1);
                }
            }

            insertIngredientStockMovements(conn, toSave);

            conn.commit();
            return findIngredientById(ingredientId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    Ingredient findIngredientById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getDBConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name, price, category from ingredient where id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idIngredient = resultSet.getInt("id");
                String name = resultSet.getString("name");
                CategoryEnum category = CategoryEnum.valueOf(resultSet.getString("category"));
                Double price = resultSet.getDouble("price");
                return new Ingredient(idIngredient, name, price, category,findStockMovementsByIngredientId(idIngredient));
            }
            throw new RuntimeException("Ingredient not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<StockMovement> findStockMovementsByIngredientId(Integer id) throws SQLException {

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        List<StockMovement> stockMovementList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, quantity, unit, type, creation_datetime
                            from stock_movement
                            where stock_movement.id_ingredient = ?;
                            """);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StockMovement stockMovement = new StockMovement();
                stockMovement.setId(resultSet.getInt("id"));
                stockMovement.setType(MovementTypeEnum.valueOf(resultSet.getString("type")));
                stockMovement.setCreationDateTime(resultSet.getTimestamp("creation_datetime").toInstant());

                StockValue stockValue = new StockValue();
                stockValue.setQuantity(resultSet.getDouble("quantity"));
                stockValue.setUnit(UnitEnum.valueOf(resultSet.getString("unit")));
                stockMovement.setValue(stockValue);

                stockMovementList.add(stockMovement);
            }
            connection.close();
            return stockMovementList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void insertIngredientStockMovements(Connection conn, Ingredient ingredient) {
        List<StockMovement> stockMovementList = ingredient.getStockMovementList();
        String sql = """
                insert into stock_movement(id, id_ingredient, quantity, type, unit, creation_datetime)
                values (?, ?, ?, ?::movement_type, ?::unit, ?)
                on conflict (id) do nothing
                """;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            for (StockMovement stockMovement : stockMovementList) {
                if (ingredient.getId() != null) {
                    preparedStatement.setInt(1, ingredient.getId());
                } else {
                    preparedStatement.setInt(1, getNextSerialValue(conn, "stock_movement", "id"));
                }
                preparedStatement.setInt(2, ingredient.getId());
                preparedStatement.setDouble(3, stockMovement.getValue().getQuantity());
                preparedStatement.setObject(4, stockMovement.getType());
                preparedStatement.setObject(5, stockMovement.getValue().getUnit());
                preparedStatement.setTimestamp(6, Timestamp.from(stockMovement.getCreationDateTime()));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sql = "SELECT pg_get_serial_sequence(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }
    private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
        if (sequenceName == null) {
            throw new IllegalArgumentException(
                    "Any sequence found for " + tableName + "." + columnName
            );
        }
        updateSequenceNextValue(conn, tableName, columnName, sequenceName);

        String nextValSql = "SELECT nextval(?)";

        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
        String setValSql = String.format(
                "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                sequenceName, columnName, tableName
        );

        try (PreparedStatement ps = conn.prepareStatement(setValSql)) {
            ps.executeQuery();
        }
    }

    private void updateDishOrders(Connection conn, Integer orderId, List<DishOrder> dishOrders)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dishOrder WHERE id_order = ?")) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }

        if (dishOrders != null && !dishOrders.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO dishOrder (id, id_order, id_dish, quantity) VALUES (?, ?, ?, ?)")) {
                for (DishOrder dishOrder : dishOrders) {
                    Integer dishOrderId = dishOrder.getId() != null
                            ? dishOrder.getId()
                            : getNextSerialValue(conn, "dish_order", "id");

                    ps.setInt(1, dishOrderId);
                    ps.setInt(2, orderId);
                    ps.setInt(3, dishOrder.getDish().getId());
                    ps.setInt(4, dishOrder.getQuantity());

                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }


    public Order findOrderById(Integer orderId) {
        DBConnection dbConnection = new DBConnection();
        String query = "SELECT id, reference, creation_datetime FROM orders WHERE id = ?";
        try (Connection conn = dbConnection.getDBConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            ResultSet resultSet = ps.executeQuery();
            Order order = null;
            Timestamp ts = resultSet.getTimestamp("creation_datetime");
            Instant creationDatetime = (ts != null) ? ts.toInstant() : null;
            if (resultSet.next()) {
                List<DishOrder> dishOrders = findDishOrderByIdOrder(orderId);
                order = new Order(resultSet.getInt("id"),resultSet.getString("reference"),creationDatetime, dishOrders);
            }
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        public Order saveOrder2(Order orderToSave) {
            List<DishOrder> dishOrderList = orderToSave.getDishOrders();
            for (DishOrder dishOrder : dishOrderList) {
                Dish dish = dishOrder.getDish();
                List<DishIngredient> dishIngredients = dish.getDishIngredients();
                for (DishIngredient dishIngredient : dishIngredients) {
                    Ingredient ingredient = dishIngredient.getIngredient();
                    StockValue actualStock = ingredient.getStockValueAt(Instant.now());
                    double requiredQuantityForOrder = dishOrder.getQuantity() * dishIngredient.getQuantity_required();
                    if (actualStock.getQuantity() < requiredQuantityForOrder) {
                        throw new RuntimeException("Insufficient Stock for Order");
                    }
                }
            }
        }

        public Order saveOrder(Order orderToSave) {
            DBConnection dbConnection = new DBConnection();
            try (Connection conn = dbConnection.getDBConnection()) {
                String upsertOrderQuery = """
                INSERT INTO orders (id, reference) VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE
                SET id = EXCLUDED.id, reference = EXCLUDED.reference
                RETURNING id
                """;
                try (PreparedStatement ps = conn.prepareStatement(upsertOrderQuery)) {
                    if (orderToSave.getId() != null) {
                        ps.setInt(1, orderToSave.getId());
                    } else {
                        ps.setInt(1, getNextSerialValue(conn, "orders", "id"));
                    }
                    ps.setString(2, orderToSave.getReference());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    updateDishOrders(conn, rs.getInt(1), orderToSave.getDishOrders());
                    return findOrderById(orderToSave.getId());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    private List<DishOrder> findDishOrderByIdOrder(Integer idOrder) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        List<DishOrder> dishOrders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, id_dish, quantity from dish_order where dish_order.id_order = ?
                            """);
            preparedStatement.setInt(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Dish dish = findDishById(resultSet.getInt("id_dish"));
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(resultSet.getInt("id"));
                dishOrder.setQuantity(resultSet.getInt("quantity"));
                dishOrder.setDish(dish);
                dishOrders.add(dishOrder);
            }
            connection.close();
            return dishOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Order findOrderByReference(String reference) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getDBConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select id, reference, creation_datetime from "order" where reference like ?""");
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                Integer idOrder = resultSet.getInt("id");
                order.setId(idOrder);
                order.setReference(resultSet.getString("reference"));
                order.setCreationDateTime(resultSet.getTimestamp("creation_datetime").toInstant());
                order.setDishOrders(findDishOrderByIdOrder(idOrder));
                return order;
            }
            throw new RuntimeException("Order not found with reference " + reference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}




//
//   public List<Dish> findDishsByIngredientName(String IngredientName){
//        DBConnection db = new DBConnection();
//        String sql = "SELECT dish.id, dish.name, dish.dish_type, dish.selling_price from dish INNER JOIN dishIngredient on dish.id =" +
//                "dishIngredient.id_dish INNER JOIN ingredient on ingredient.id = dishIngredient.id_ingredient where ingredient.name ilike ?";
//        List<Dish> dishInHere = new ArrayList<Dish>();
//
//        try {
//            Connection conn = db.getDBConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, "%"+IngredientName+"%");
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()){
//                Integer id = rs.getInt("id");
//                String name = rs.getString("name");
//                String dish_type = rs.getString("dish_type");
//                DishTypeEnum dishTypeEnum = DishTypeEnum.valueOf(dish_type);
//                Double sellingPrice = rs.getDouble("selling_price");
//                Dish dishofIngredient = new Dish(id, name, dishTypeEnum, sellingPrice);
//                dishInHere.add(dishofIngredient);
//            }
//
//            System.out.println(dishInHere);
//
//
//            conn.close();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return dishInHere;
//    }
//}
///**
//    public List<Ingredient> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page,
//                                                     int size){
//        DBConnection db = new DBConnection();
//        String sql = "SELECT ingredient.id, ingredient.name, ingredient.price, ingredient.category from ingredient inner join dish" +
//                " on ingredient.id_dish = dish.id where ingredient.name = ? and ingredient.category = ? :: ingredient_category and" +
//                " dish.name = ? LIMIT ? OFFSET ?";
//        List<Ingredient> ingredientList = new ArrayList<>();
//        System.out.println(String.valueOf(category));
//        try{
//            Connection conn = db.getDBConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, ingredientName);
//            stmt.setString(2, category.name());
//            stmt.setString(3, dishName);
//            stmt.setInt(4, size);
//            stmt.setInt(5, (page-1)*size);
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()){
//                Integer id = rs.getInt("id");
//                String name = rs.getString("name");
//                System.out.println(name);
//                Double price = rs.getDouble("price");
//                String categoryString = rs.getString("category");
//                CategoryEnum categoryEnum = CategoryEnum.valueOf(categoryString);
//
//                Ingredient toIngredientList = new Ingredient(id, name, price, categoryEnum);
//
//                ingredientList.add(toIngredientList);
//
//            }
//            System.out.println(ingredientList.size());
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ingredientList;
//
//    }
//
//    //DELETE INGREDIENT
//    public void deleteIngredientbyName(String name){
//        DBConnection db = new DBConnection();
//        String sql = "DELETE from ingredient where name = ?";
//        try {
//            Connection conn = db.getDBConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, name);
//            stmt.executeUpdate();
//            System.out.println("Suppresion réussie");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    //DELETE
//
//    public void tryDELETEbyid(String name){
//        DBConnection db = new DBConnection();
//        String sql = "DELETE from dish where name = ?";
//        try {
//            Connection conn = db.getDBConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, name);
//            stmt.executeUpdate();
//            System.out.println("Suppression réussie");
//
//
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    //UPDATE
//    public void tryUPDATE(String newName, int id){
//        DBConnection db = new DBConnection();
//        String sql = "UPDATE dish set name = ? where id = ?";
//
//        try {
//            Connection conn = db.getDBConnection();
//            PreparedStatement stmt= conn.prepareStatement(sql);
//            stmt.setString(1, newName);
//            stmt.setInt(2,id);
//
//            stmt.executeUpdate();
//            System.out.println("UPDATE Succeed");
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//
//    public DataRetriever() {
//
//    }
//
//    public Dish getDish() {
//        return dish;
//    }
//
//    public void setDish(Dish dish) {
//        this.dish = dish;
//    }
//
//    public Ingredient getIngredient() {
//        return ingredient;
//    }
//
//    public void setIngredient(Ingredient ingredient) {
//        this.ingredient = ingredient;
//    }
//}
//*/