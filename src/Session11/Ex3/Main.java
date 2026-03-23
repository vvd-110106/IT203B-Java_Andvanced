package Session11.Ex3;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * - executeUpdate() trả về số nguyên (int) là số dòng bị thay đổi trong Database.
 * - Nếu trả về 0: Mã giường không tồn tại. Ta dùng giá trị này để báo lỗi chính xác cho y tá.
 */
public class Main {
    public static void main(String[] args) {
        String inputId = "Bed_999"; // Mã test không tồn tại
        String sql = "UPDATE Beds SET bed_status = 'Occupied' WHERE bed_id = '" + inputId + "'";

        try (Connection conn = dbConnect.getConnection();
             Statement stmt = conn.createStatement()) {

            int rows = stmt.executeUpdate(sql);

            System.out.print("PHẦN 2 - THỰC THI: ");
            if (rows > 0) {
                System.out.println("Cập nhật thành công giường " + inputId);
            } else {
                System.out.println("Lỗi: Mã giường " + inputId + " không tồn tại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}