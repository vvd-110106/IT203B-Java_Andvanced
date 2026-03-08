package Sesseion01;

import java.util.Scanner;

public class Ex5 {
    static class InvalidAgeException extends Exception {
        public InvalidAgeException(String msg) {
            super(msg);
        }
    }

    static class User {
        private int age;

        public void setAge(int age) throws InvalidAgeException {
            if (age < 0) {
                throw new InvalidAgeException("Tuổi không thể âm!");
            }
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        try {
            System.out.print("Nhập tuổi: ");
            int inputAge = Integer.parseInt(scanner.nextLine());

            user.setAge(inputAge);
            System.out.println("Tuổi hợp lệ: " + user.getAge());

        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Nhập số không hợp lệ.");
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally");
        }
    }
}
