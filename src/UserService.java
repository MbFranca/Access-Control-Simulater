import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(String username, String password, String roleInput) {
        String validationError = validate(username, password, roleInput);
        if (validationError != null) {
            return validationError;
        }

        if (userRepository.findByUsername(username.trim()) != null) {
            return "Username já existe.";
        }

        Role role = Role.valueOf(roleInput.trim().toUpperCase());
        boolean saved = userRepository.save(new User(username.trim(), password.trim(), role));
        return saved ? "Usuário cadastrado com sucesso." : "Falha ao cadastrar usuário.";
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public String updateUser(int id, String username, String password, String roleInput) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return "Usuário não encontrado.";
        }

        String validationError = validate(username, password, roleInput);
        if (validationError != null) {
            return validationError;
        }

        User userWithSameUsername = userRepository.findByUsername(username.trim());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
            return "Username já existe para outro usuário.";
        }

        Role role = Role.valueOf(roleInput.trim().toUpperCase());
        User updatedUser = new User(id, username.trim(), password.trim(), role);
        boolean updated = userRepository.update(updatedUser);
        return updated ? "Usuário atualizado com sucesso." : "Falha ao atualizar usuário.";
    }

    public String deleteUser(int id) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return "Usuário não encontrado.";
        }

        boolean deleted = userRepository.deleteById(id);
        return deleted ? "Usuário deletado com sucesso." : "Falha ao deletar usuário.";
    }

    private String validate(String username, String password, String roleInput) {
        if (username == null || username.trim().isEmpty()) {
            return "Username não pode ser vazio.";
        }

        if (password == null || password.trim().length() < 4) {
            return "Password deve ter no mínimo 4 caracteres.";
        }

        if (parseRole(roleInput) == null) {
            return "Role inválida. Use ADMIN, SOLDADO ou VISITANTE.";
        }

        return null;
    }

    private Role parseRole(String roleInput) {
        if (roleInput == null) {
            return null;
        }

        try {
            return Role.valueOf(roleInput.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
