package Session03;

import java.util.List;

public class Ex2 {
    public record User(String username, String email, String status) {}
    public static void main(String[] args) {
        List<User> users = List.of(
                new User("alice", "alice@gmail.com", "ACTIVE"),
                new User("bob", "bob@yahoo.com", "INACTIVE"),
                new User("charlie", "charlie@gmail.com", "ACTIVE"),
                new User("david", "david@outlook.com", "ACTIVE")
        );
        users.stream().filter(u -> u.email().endsWith("@gmail.com")).map(User::username).forEach(System.out::println);
    }
}