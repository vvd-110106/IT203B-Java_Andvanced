package Sesseion01;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Ex6 {
    static class InvalidAgeException extends Exception {
        public InvalidAgeException(String msg) {
            super(msg);
        }
    }
    static class User {
        private String name;
        private int age;
        public void setName(String name) {
            if (name != null && !name.trim().isEmpty()) {
                this.name = name;
            } else {
                System.out.println("Cảnh báo: Tên không hợp lệ, không gán giá trị");
            }
        }
        public void setAge(int age) throws InvalidAgeException {
            if (age < 0) {
                throw new InvalidAgeException("Tuổi " + age + " không thể âm");
            }
            this.age = age;
        }
        public void displayInfo() {
            if (name != null) {
                System.out.println("Người dùng: " + name + ", Tuổi: " + age);
            } else {
                System.out.println("Thông tin người dùng chưa đầy đủ");
            }
        }
    }
    private static void logError(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.err.println("[ERROR] " + timestamp + " - " + message);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        try {
            System.out.print("Nhập tên: ");
            user.setName(scanner.nextLine());

            System.out.print("Nhập tuổi: ");
            String inputAge = scanner.nextLine();
            user.setAge(Integer.parseInt(inputAge));

            user.displayInfo();

        } catch (NumberFormatException e) {
            logError("Dữ liệu tuổi không phải là số hợp lệ.");
        } catch (InvalidAgeException e) {
            logError("Vi phạm quy tắc nghiệp vụ: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally");
        }
    }
}
