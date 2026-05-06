public class AuthorizationService {
    public static final String ACCESS_GRANTED = "ACESSO LIBERADO";
    public static final String ACCESS_DENIED_NO_PERMISSION = "ACESSO NEGADO - SEM PERMISSÃO";

    public boolean isAuthorized(User user) {
        if (user == null || user.getRole() == null) {
            return false;
        }

        return user.getRole() == Role.ADMIN || user.getRole() == Role.SOLDADO;
    }
}
