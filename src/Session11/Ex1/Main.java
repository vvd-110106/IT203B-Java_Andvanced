package Session11.Ex1;

import Session11.dbConnect;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * - Cạn kiệt tài nguyên: Mỗi kết nối mở chiếm dung lượng bộ nhớ trên Server. Không đóng sẽ dẫn đến lỗi cạn kiệt pool.
 * - Nguy cơ y tế 24/7: Hệ thống bệnh viện bị "treo" khiến bác sĩ không thể tra cứu bệnh án, gây rủi ro tính mạng.
 */
public class Main {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = dbConnect.getConnection(); // Sử dụng hàm từ dbConnect
            if (conn != null) {
                System.out.println("PHẦN 2 - THỰC THI: Kết nối bệnh viện thành công.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối để giải phóng tài nguyên
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Đã đóng kết nối an toàn.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}