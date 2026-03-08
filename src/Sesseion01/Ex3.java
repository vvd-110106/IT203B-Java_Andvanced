package Sesseion01;

import java.util.Scanner;

class User {
    private int age;
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Tuổi không thể âm!");
        }
        this.age = age;
    }
    public int getAge() {
        return age;
    }
}

public class Ex3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        try {
            System.out.print("Nhập tuổi người dùng: ");
            int inputAge = Integer.parseInt(scanner.nextLine());

            user.setAge(inputAge);
            System.out.println("Cập nhật tuổi thành công: " + user.getAge());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Vui lòng nhập số nguyên.");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi nghiệp vụ: " + e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally...");
        }
    }
}
