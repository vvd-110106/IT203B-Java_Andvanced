package Session03;

import java.util.Comparator;
import java.util.List;

public class Ex5 {
    public record User(String username, String email, String status) {}
    public static void main(String[] args) {
        List<User> users = List.of(
                new User("alexander", "a@test.com", "ACTIVE"),
                new User("bob", "b@test.com", "INACTIVE"),
                new User("charlotte", "c@test.com", "ACTIVE"),
                new User("benjamin", "d@test.com", "ACTIVE"),
                new User("dan", "e@test.com", "ACTIVE")
        );
        users.stream().sorted(Comparator.comparingInt(
                (User u) -> u.username().length()).reversed()).limit(3).map(User::username).forEach(System.out::println);
    }
}