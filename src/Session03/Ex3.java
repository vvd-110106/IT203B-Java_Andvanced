package Session03;

import java.util.List;
import java.util.Optional;

public class Ex3 {
    public record User(String username, String email, String status) {}
    public static class UserRepository {
        private final List<User> users = List.of(
                new User("alice", "alice@gmail.com", "ACTIVE"),
                new User("bob", "bob@yahoo.com", "INACTIVE")
        );
        public Optional<User> findUserByUsername(String username) {
            return users.stream()
                    .filter(u -> u.username().equals(username))
                    .findFirst();
        }
    }
    public static void main(String[] args) {
        UserRepository repo = new UserRepository();
        repo.findUserByUsername("alice").ifPresentOrElse(u -> System.out.println("Welcome " + u.username()),
                () -> System.out.println("Guest login"));
    }
}