package Session12.Ex2;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * 1. Vấn đề nối chuỗi: Khi dùng Statement, dữ liệu số (double, int) bị ép kiểu sang String.
 * Nếu hệ thống dùng Locale Việt Nam/Pháp, số 37.5 có thể bị biến thành "37,5". SQL không hiểu
 * dấu phẩy này, dẫn đến lỗi cú pháp (Syntax Error).
 * 2. Ưu điểm của PreparedStatement: Các phương thức setDouble(), setInt() truyền dữ liệu
 * dưới dạng nhị phân (binary) trực tiếp đến database theo đúng kiểu dữ liệu của cột.
 * Nó hoàn toàn bỏ qua bước chuyển đổi sang chuỗi ký tự, do đó lập trình viên không cần
 * quan tâm đến dấu chấm hay dấu phẩy của hệ điều hành.
 */
public class Main {
    public static void main(String[] args) {
        String patientId = "BN001";
        double temp = 37.5;
        int heartRate = 85;

        // PHẦN 2 - THỰC THI: SỬ DỤNG PREPAREDSTATEMENT CHO DỮ LIỆU SỐ
        String sql = "UPDATE Vitals SET temperature = ?, heart_rate = ? WHERE p_id = ?";

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Truyền dữ liệu theo đúng kiểu, không lo lỗi định dạng dấu phẩy/chấm
            pstmt.setDouble(1, temp);
            pstmt.setInt(2, heartRate);
            pstmt.setString(3, patientId);

            int rows = pstmt.executeUpdate();

            System.out.println("--- CẬP NHẬT CHỈ SỐ SINH TỒN ---");
            if (rows > 0) {
                System.out.println("Thành công: Đã cập nhật chỉ số cho bệnh nhân " + patientId);
                System.out.printf("Nhiệt độ: %.1f | Nhịp tim: %d bpm\n", temp, heartRate);
            } else {
                System.err.println("Lỗi: Không tìm thấy bệnh nhân mã " + patientId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn SQL: " + e.getMessage());
        }
    }
}