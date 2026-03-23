package Session12.Ex3;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * 1. Tại sao phải gọi registerOutParameter(): JDBC cần biết kiểu dữ liệu của tham số
 * đầu ra trước khi thực thi để cấp phát bộ nhớ và ánh xạ (mapping) đúng kiểu dữ liệu
 * giữa SQL và Java. Nếu không đăng ký, Driver sẽ không biết vị trí đó là tham số trả về
 * và dẫn đến lỗi "index out of range" khi bạn cố gắng lấy dữ liệu sau đó.
 * * 2. Kiểu DECIMAL trong SQL: Khi đăng ký trong Java, ta phải sử dụng hằng số
 * java.sql.Types.DECIMAL hoặc java.sql.Types.DOUBLE tùy vào độ chính xác yêu cầu.
 */
public class Main {
    public static void main(String[] args) {
        int surgeryId = 505;

        // Cú pháp gọi Stored Procedure: {call ProcedureName(?, ?)}
        String sql = "{call GET_SURGERY_FEE(?, ?)}";

        try (Connection conn = dbConnect.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Bước 1: Thiết lập tham số đầu vào (IN parameter)
            cstmt.setInt(1, surgeryId);

            // Bước 2: QUAN TRỌNG - Đăng ký tham số đầu ra (OUT parameter)
            // Giả sử tham số thứ 2 là total_cost kiểu DECIMAL
            cstmt.registerOutParameter(2, Types.DECIMAL);

            // Bước 3: Thực thi thủ tục
            cstmt.execute();

            // Bước 4: Lấy giá trị từ tham số đầu ra sau khi thực thi
            double totalCost = cstmt.getDouble(2);

            System.out.println("--- CHI PHÍ PHẪU THUẬT ---");
            if (totalCost > 0) {
                System.out.println("Mã ca phẫu thuật: " + surgeryId);
                System.out.printf("Tổng chi phí (đã tính bảo hiểm): %,.2f VNĐ\n", totalCost);
            } else {
                System.out.println("Không tìm thấy dữ liệu chi phí cho mã này.");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi gọi Stored Procedure: " + e.getMessage());
        }
    }
}