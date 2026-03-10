package Session03;

import java.util.List;
import java.util.stream.Collectors;

public class Ex4 {
    public record User(String username, String email, String status) {}
    public static void main(String[] args) {
        List<User> users = List.of(
                new User("alice", "alice@gmail.com", "ACTIVE"),
                new User("bob", "bob@yahoo.com", "INACTIVE"),
                new User("alice", "alice_duplicate@gmail.com", "ACTIVE"),
                new User("charlie", "charlie@gmail.com", "ACTIVE")
        );
        List<User> uniqueUsers = users.stream()
                .collect(Collectors.toMap(User::username, user -> user,
                        (existing, replacement) -> existing)).values().stream().toList();
        uniqueUsers.forEach(System.out::println);
    }
}