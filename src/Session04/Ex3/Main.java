package Session04.Ex3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProcessor {
    public String processEmail(String email) {
        if (email == null || !email.contains("@") || email.endsWith("@")) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }
        return email.toLowerCase();
    }
}

public class Main {
    private UserProcessor userProcessor;
    @BeforeEach
    void setUp() {
        userProcessor = new UserProcessor();
    }
    @Test
    void testProcessEmail_ValidFormat() {
        String input = "user@gmail.com";
        String result = userProcessor.processEmail(input);
        Assertions.assertEquals("user@gmail.com", result);
    }
    @Test
    void testProcessEmail_MissingAtSign() {
        String input = "usergmail.com";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userProcessor.processEmail(input);
        });
    }
    @Test
    void testProcessEmail_MissingDomain() {
        String input = "user@";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userProcessor.processEmail(input);
        });
    }
    @Test
    void testProcessEmail_NormalizationToLowerCase() {
        String input = "Example@Gmail.com";
        String result = userProcessor.processEmail(input);
        Assertions.assertEquals("example@gmail.com", result);
    }
    public static void main(String[] args) {
        UserProcessor processor = new UserProcessor();
        try {
            System.out.println("Valid: " + processor.processEmail("user@gmail.com"));
            System.out.println("Normalize: " + processor.processEmail("Example@Gmail.com"));
            System.out.println("Invalid: " + processor.processEmail("user@"));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }
}