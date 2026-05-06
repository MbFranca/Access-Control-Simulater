import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final DatabaseConnection databaseConnection;

    public UserRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        createTableIfNotExists();
        seedDefaultUsersIfNeeded();
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public User findById(int id) {
        String sql = "SELECT id, username, password, role FROM users WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por id: " + e.getMessage());
        }

        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role FROM users ORDER BY id";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }

        return users;
    }

    public boolean save(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
            return false;
        }
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            )
            """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela users: " + e.getMessage());
        }
    }

    private void seedDefaultUsersIfNeeded() {
        String countSql = "SELECT COUNT(*) FROM users";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement countStatement = connection.prepareStatement(countSql);
             ResultSet resultSet = countStatement.executeQuery()) {
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                save(new User("admin", "123456", Role.ADMIN));
                save(new User("joao", "senha123", Role.SOLDADO));
                save(new User("maria", "abc123", Role.VISITANTE));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao popular tabela users: " + e.getMessage());
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        Role role = Role.valueOf(resultSet.getString("role").toUpperCase());
        return new User(id, username, password, role);
    }
}
