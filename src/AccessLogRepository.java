import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccessLogRepository {
    private final DatabaseConnection databaseConnection;

    public AccessLogRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        createTableIfNotExists();
    }

    public void save(String username, String status) {
        String sql = "INSERT INTO access_logs(username, access_time, status) VALUES (?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, LocalDateTime.now().toString());
            statement.setString(3, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar log de acesso: " + e.getMessage());
        }
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS access_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                access_time TEXT NOT NULL,
                status TEXT NOT NULL CHECK(status IN ('LIBERADO', 'NEGADO'))
            )
            """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela access_logs: " + e.getMessage());
        }
    }
}
