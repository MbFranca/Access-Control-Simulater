import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection("access_control.db");
        UserRepository userRepository = new UserRepository(databaseConnection);
        AccessLogRepository accessLogRepository = new AccessLogRepository(databaseConnection);
        UserService userService = new UserService(userRepository);
        AuthService authService = new AuthService(userRepository);
        AuthorizationService authorizationService = new AuthorizationService();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Escolha uma opção: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> handleLogin(scanner, authService, authorizationService, accessLogRepository);
                case "2" -> handleCreateUser(scanner, userService);
                case "3" -> handleListUsers(userService);
                case "4" -> handleUpdateUser(scanner, userService);
                case "5" -> handleDeleteUser(scanner, userService);
                case "0" -> {
                    System.out.println("Saindo...");
                    running = false;
                }
                default -> System.out.println("Opção inválida.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== CONTROLE DE ACESSO ===");
        System.out.println("1 - Login");
        System.out.println("2 - Cadastrar usuário");
        System.out.println("3 - Listar usuários");
        System.out.println("4 - Atualizar usuário");
        System.out.println("5 - Deletar usuário");
        System.out.println("0 - Sair");
    }

    private static void handleLogin(
        Scanner scanner,
        AuthService authService,
        AuthorizationService authorizationService,
        AccessLogRepository accessLogRepository
    ) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User authenticatedUser = authService.authenticate(username, password);
        if (authenticatedUser == null) {
            accessLogRepository.save(username, "NEGADO");
            System.out.println("ACESSO NEGADO");
            return;
        }

        if (authorizationService.isAuthorized(authenticatedUser)) {
            accessLogRepository.save(authenticatedUser.getUsername(), "LIBERADO");
            System.out.println(AuthorizationService.ACCESS_GRANTED);
        } else {
            accessLogRepository.save(authenticatedUser.getUsername(), "NEGADO");
            System.out.println(AuthorizationService.ACCESS_DENIED_NO_PERMISSION);
        }
    }

    private static void handleCreateUser(Scanner scanner, UserService userService) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role (ADMIN, SOLDADO, VISITANTE): ");
        String role = scanner.nextLine();

        String result = userService.createUser(username, password, role);
        System.out.println(result);
    }

    private static void handleListUsers(UserService userService) {
        System.out.println("=== USUÁRIOS ===");
        for (User user : userService.listUsers()) {
            System.out.printf("ID: %d | Username: %s | Role: %s%n",
                user.getId(), user.getUsername(), user.getRole());
        }
    }

    private static void handleUpdateUser(Scanner scanner, UserService userService) {
        System.out.print("ID do usuário: ");
        Integer id = tryParseInt(scanner.nextLine());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        System.out.print("Novo username: ");
        String username = scanner.nextLine();
        System.out.print("Nova password: ");
        String password = scanner.nextLine();
        System.out.print("Novo role (ADMIN, SOLDADO, VISITANTE): ");
        String role = scanner.nextLine();

        String result = userService.updateUser(id, username, password, role);
        System.out.println(result);
    }

    private static void handleDeleteUser(Scanner scanner, UserService userService) {
        System.out.print("ID do usuário: ");
        Integer id = tryParseInt(scanner.nextLine());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        System.out.print("Confirma exclusão? (s/n): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("s")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        String result = userService.deleteUser(id);
        System.out.println(result);
    }

    private static Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
