package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String url = "jdbc:postgresql://localhost:5432/mini_dish_db";
    private String user = "mini_dish_db_manager";
    private String password = "123456";

    public DBConnection() {
    }

    public Connection getDBConnection () throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
