public class User {
    private final Integer id;
    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this(null, username, password, role);
    }

    public User(Integer id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
