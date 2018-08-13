package pl.coderslab;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection implements AutoCloseable {

    private Connection connection;

    public DBConnection(String url, String user, String password) {
        try {
            this.connection = (Connection) DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }
}
