package Session11.Ex2;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * - Lệnh 'if' chỉ kiểm tra và lấy ra dòng đầu tiên trong ResultSet rồi dừng lại.
 * - ResultSet hoạt động theo con trỏ: Cần dùng 'while(rs.next())' để đẩy con trỏ qua tất cả các dòng thuốc.
 */
public class Main {
    public static void main(String[] args) {
        String sql = "SELECT medicine_name, stock FROM Pharmacy";

        try (Connection conn = dbConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("PHẦN 2 - THỰC THI: DANH MỤC THUỐC");
            while (rs.next()) {
                System.out.println("Thuốc: " + rs.getString("medicine_name") + " | Số lượng: " + rs.getInt("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}