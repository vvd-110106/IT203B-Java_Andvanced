package Session04.Ex1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserValidator {
    public boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        int length = username.length();
        return length >= 6 && length <= 20 && !username.contains(" ");
    }
}
public class Main {
    private final UserValidator validator = new UserValidator();
    @Test
    void testValidUsername_TC01() {
        String username = "user123";
        boolean result = validator.isValidUsername(username);
        Assertions.assertTrue(result);
    }
    @Test
    void testUsernameTooShort_TC02() {
        String username = "abc";
        boolean result = validator.isValidUsername(username);
        Assertions.assertFalse(result);
    }
    @Test
    void testUsernameWithSpace_TC03() {
        String username = "user name";
        boolean result = validator.isValidUsername(username);
        Assertions.assertFalse(result);
    }
    public static void main(String[] args) {
        UserValidator validator = new UserValidator();
        System.out.println(validator.isValidUsername("user123"));
        System.out.println(validator.isValidUsername("abc"));
        System.out.println(validator.isValidUsername("user name"));
    }
}