package Sesseion01;

import java.util.Scanner;

public class Ex2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Nhập tổng số người dùng: ");
            int tongSoNguoi = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập số lượng nhóm muốn chia: ");
            int soNhom = Integer.parseInt(scanner.nextLine());

            int moiNhom = tongSoNguoi / soNhom;
            System.out.println("Mỗi nhóm có: " + moiNhom + " người");

        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Vui lòng nhập số nguyên hợp lệ");

        } catch (ArithmeticException e) {
            System.out.println("Không thể chia cho 0");

        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally");
        }
        System.out.println("Chương trình vẫn tiếp tục chạy các luồng lệnh phía sau");
    }
}
