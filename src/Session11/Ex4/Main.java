package Session11.Ex4;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * 1. Luồng thực thi: Khi nối chuỗi trực tiếp, câu lệnh SQL gửi đến Database sẽ trở thành:
 * SELECT * FROM Patients WHERE full_name = '' OR '1'='1'
 * 2. Tại sao WHERE luôn đúng: Phép toán logic OR được thực thi. Trong đó, biểu thức '1'='1'
 * luôn luôn trả về TRUE. Hệ quả là điều kiện WHERE trở nên vô hiệu, ép buộc Database
 * trả về tất cả các bản ghi có trong bảng Patients thay vì chỉ 1 người.
 * 3. Hậu quả: Hacker có thể lấy toàn bộ thông tin bệnh án của hàng ngàn người chỉ bằng
 * cách nhập một chuỗi ký tự đặc biệt vào ô tìm kiếm.
 */
public class Main {
    public static void main(String[] args) {
        // Giả lập dữ liệu từ ô tìm kiếm bị hacker chèn mã độc
        String patientName = "' OR '1'='1";

        // PHẦN 2 - THỰC THI: GIẢI PHÁP VỚI STATEMENT (LOẠI BỎ KÝ TỰ ĐẶC BIỆT)
        // Cách tiếp cận: Làm sạch dữ liệu (Sanitize input)
        String sanitizedName = patientName.replace("'", "''") // Thay dấu nháy đơn bằng nháy kép để SQL hiểu là text
                .replace("--", "")  // Loại bỏ ký tự cmt SQL
                .replace(";", "");  // Loại bỏ ký tự kết thúc lệnh

        String sql = "SELECT * FROM Patients WHERE full_name = '" + sanitizedName + "'";

        try (Connection conn = dbConnect.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Câu lệnh SQL sau khi làm sạch: " + sql);
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--- KẾT QUẢ TRA CỨU ---");
            if (!rs.isBeforeFirst()) {
                System.out.println("Không tìm thấy bệnh nhân phù hợp.");
            } else {
                while (rs.next()) {
                    System.out.println("Bệnh nhân: " + rs.getString("full_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}