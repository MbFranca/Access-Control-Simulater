import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Path filePath;

    public UserRepository(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length != 3) {
                    continue;
                }

                String username = parts[0].trim();
                String password = parts[1].trim();
                String roleRaw = parts[2].trim();

                Role role;
                try {
                    // Permite escrita em qualquer caixa: "admin", "ADMIN", etc.
                    role = Role.valueOf(roleRaw.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    // Linha inválida (role desconhecida) -> ignora
                    continue;
                }

                users.add(new User(username, password, role));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler users.txt: " + e.getMessage());
        }

        return users;
    }
}
