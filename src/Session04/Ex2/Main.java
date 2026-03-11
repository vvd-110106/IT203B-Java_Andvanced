package Session04.Ex2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserService {
    public boolean checkRegistrationAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Tuổi không được là số âm");
        }
        return age >= 18;
    }
}

public class Main {
    private final UserService userService = new UserService();
    @Test
    void testValidAgeBoundary_18() {
        boolean result = userService.checkRegistrationAge(18);
        Assertions.assertEquals(true, result);
    }
    @Test
    void testInvalidAgeUnderRequirement_17() {
        boolean result = userService.checkRegistrationAge(17);
        Assertions.assertEquals(false, result);
    }
    @Test
    void testInvalidAgeNegative_Minus1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.checkRegistrationAge(-1);
        });
    }
    public static void main(String[] args) {
        UserService service = new UserService();
        try {
            System.out.println("Age 18: " + service.checkRegistrationAge(18));
            System.out.println("Age 17: " + service.checkRegistrationAge(17));
            System.out.println("Age -1: " + service.checkRegistrationAge(-1));
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}