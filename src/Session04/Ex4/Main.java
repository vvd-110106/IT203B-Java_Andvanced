package Session04.Ex4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordValidator {
    public String evaluatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "Yếu";
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        if (hasUpper && hasLower && hasDigit && hasSpecial) {
            return "Mạnh";
        }
        if ((hasLower && hasDigit && hasSpecial) || (hasUpper && hasLower && hasDigit) || (hasUpper && hasLower && hasSpecial)) {
            return "Trung bình";
        }
        return "Yếu";
    }
}

public class Main {
    private final PasswordValidator validator = new PasswordValidator();
    @Test
    void testPasswordStrength_Comprehensive() {
        Assertions.assertAll("Kiểm tra tổng thể các mức độ mật khẩu",
                () -> Assertions.assertEquals("Mạnh", validator.evaluatePasswordStrength("Abc123!@")),
                () -> Assertions.assertEquals("Trung bình", validator.evaluatePasswordStrength("abc123!@")),
                () -> Assertions.assertEquals("Trung bình", validator.evaluatePasswordStrength("ABC123!@")),
                () -> Assertions.assertEquals("Trung bình", validator.evaluatePasswordStrength("Abcdef!@")),
                () -> Assertions.assertEquals("Trung bình", validator.evaluatePasswordStrength("Abc12345")),
                () -> Assertions.assertEquals("Yếu", validator.evaluatePasswordStrength("Ab1!")),
                () -> Assertions.assertEquals("Yếu", validator.evaluatePasswordStrength("password")),
                () -> Assertions.assertEquals("Yếu", validator.evaluatePasswordStrength("ABC12345"))
        );
    }
    public static void main(String[] args) {
        PasswordValidator validator = new PasswordValidator();
        System.out.println("TC01: " + validator.evaluatePasswordStrength("Abc123!@"));
        System.out.println("TC06: " + validator.evaluatePasswordStrength("Ab1!"));
    }
}