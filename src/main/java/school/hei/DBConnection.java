package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String JDBC_URL = "jdbc:postgresql://localhost:5432/mini_dish_db";
    private String USERNAME = "mini_dish_db_manager";
    private String PASSWORD = "123456";

    public DBConnection() {
    }

    public Connection getDBConnection () throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

}
