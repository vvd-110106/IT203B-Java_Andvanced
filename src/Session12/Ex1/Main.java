package Session12.Ex1;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * 1. PreparedStatement là "tấm khiên": Khác với Statement (gửi cả câu lệnh và dữ liệu
 * dưới dạng một chuỗi văn bản thuần túy), PreparedStatement tách biệt hoàn toàn phần
 * cấu trúc câu lệnh SQL và phần dữ liệu đầu vào.
 * 2. Cơ chế Pre-compiled: Câu lệnh SQL với các dấu hỏi chấm (?) được gửi đến Database
 * để biên dịch và lập kế hoạch thực thi trước. Khi dữ liệu được truyền vào sau đó,
 * Database chỉ coi đó là các giá trị literal (văn bản thuần túy) chứ không bao giờ
 * coi đó là một phần của lệnh thực thi. Vì vậy, chuỗi "' OR '1'='1" sẽ bị coi là một
 * mật khẩu sai thay vì một đoạn mã tấn công.
 */
public class Main {
    public static void main(String[] args) {
        // Giả lập dữ liệu đăng nhập từ hacker
        String doctorCode = "DOC001";
        String inputPass = "' OR '1'='1";

        // PHẦN 2 - THỰC THI: SỬ DỤNG PREPAREDSTATEMENT
        String sql = "SELECT * FROM Doctors WHERE code = ? AND pass = ?";

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Truyền tham số an toàn qua các dấu hỏi chấm
            pstmt.setString(1, doctorCode);
            pstmt.setString(2, inputPass);

            ResultSet rs = pstmt.executeQuery();

            System.out.println("--- KẾT QUẢ ĐĂNG NHẬP ---");
            if (rs.next()) {
                System.out.println("Đăng nhập thành công! Chào bác sĩ: " + rs.getString("full_name"));
            } else {
                System.out.println("Lỗi: Mã số bác sĩ hoặc mật khẩu không chính xác.");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi hệ thống: " + e.getMessage());
        }
    }
}