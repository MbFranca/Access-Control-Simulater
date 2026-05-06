import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url;

    public DatabaseConnection(String databaseFileName) {
        this.url = "jdbc:sqlite:" + databaseFileName;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
