import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserRepository userRepository = new UserRepository("users.txt");
        AuthService authService = new AuthService(userRepository);
        AuthorizationService authorizationService = new AuthorizationService();

        User authenticatedUser = authService.authenticate(username, password);

        if (authenticatedUser == null) {
            System.out.println("ACESSO NEGADO");
        } else {
            if (authorizationService.isAuthorized(authenticatedUser)) {
                System.out.println(AuthorizationService.ACCESS_GRANTED);
            } else {
                System.out.println(AuthorizationService.ACCESS_DENIED_NO_PERMISSION);
            }
        }

        scanner.close();
    }
}
