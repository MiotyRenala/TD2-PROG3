package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String JDBC_URL = System.getenv("JDBC_URL");
    private String USERNAME = System.getenv("DB_USERNAME");
    private String PASSWORD = System.getenv("DB_PASSWORD");

    public DBConnection() {
    }

    public Connection getDBConnection () throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

}
