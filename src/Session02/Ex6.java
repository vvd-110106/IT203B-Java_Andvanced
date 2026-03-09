package Session02;

class User2 {
    private String username;

    public User2(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
@FunctionalInterface
interface UserProcessor {
    String process(User u);
}
class UserUtils {
    public static String convertToUpperCase(User u) {
        return u.getUsername().toUpperCase();
    }
}
public class Ex6 {
    public static void main(String[] args) {
        UserProcessor processor = UserUtils::convertToUpperCase;

        User user = new User("vũ việt hoàng");
        String result = processor.process(user);

        System.out.println(result);
    }
}