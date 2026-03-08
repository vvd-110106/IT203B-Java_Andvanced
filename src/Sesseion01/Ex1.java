package Sesseion01;

import java.util.Scanner;

public class Ex1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Nhập năm sinh: ");
            String input = scanner.nextLine();

            int namSinh = Integer.parseInt(input);
            int tuoi = 2026 - namSinh;

            System.out.println("Tuổi của bạn là: " + tuoi);

        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Vui lòng nhập số, không nhập chữ");

        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally");
        }
    }
}
