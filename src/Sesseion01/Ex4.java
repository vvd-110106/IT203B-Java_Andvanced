package Sesseion01;

import java.io.IOException;

public class Ex4 {
    public static void main(String[] args) {
        try {
            processUserData();
        } catch (IOException e) {
            System.out.println("Main (A) đã xử lý lỗi: " + e.getMessage());
        } finally {
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally");
        }
        System.out.println("Chương trình kết thúc an toàn");
    }
    public static void processUserData() throws IOException {
        saveToFile();
    }
    public static void saveToFile() throws IOException {
        throw new IOException("Lỗi");
    }
}
