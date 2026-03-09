package Session02;

import java.util.List;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.Consumer;

class User {
    private String username;
    public User() {
        this.username = "New User";
    }
    public User(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}

public class Ex4 {
    public static void main(String[] args) {
        Supplier<User> userConstructor = User::new;
        User user1 = userConstructor.get();

        List<User> users = Arrays.asList(user1, new User("Alice"), new User("Bob"));

        Function<User, String> nameExtractor = User::getUsername;
        Consumer<String> printer = System.out::println;
        users.stream().map(nameExtractor).forEach(printer);
    }
}