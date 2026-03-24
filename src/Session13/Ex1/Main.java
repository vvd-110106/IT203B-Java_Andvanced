package Session13.Ex1;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH LOGIC:
 * 1. Chế độ Auto-Commit: Mặc định mỗi lệnh executeUpdate() sẽ tự động COMMIT xuống DB ngay lập tức.
 * 2. Tại sao lỗi vẫn trừ kho: Khi xảy ra lỗi ở giữa (như chia cho 0), thao tác 1 đã được Auto-Commit nên thuốc bị trừ vĩnh viễn.
 * Thao tác 2 chưa chạy dẫn đến dữ liệu không đồng nhất (mất thuốc nhưng không có lịch sử).
 * 3. Giải pháp Transaction: Tắt Auto-Commit giúp gom các lệnh vào một khối. Chỉ khi tất cả thành công mới COMMIT,
 * nếu có lỗi sẽ ROLLBACK để đưa dữ liệu về trạng thái ban đầu.
 */
public class Main {
    public void capPhatThuoc(int medicineId, int patientId) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            conn = dbConnect.getConnection();

            // Tắt Auto-Commit để quản lý giao dịch thủ công
            conn.setAutoCommit(false);

            String sqlUpdateInventory = "UPDATE Medicine_Inventory SET quantity = quantity - 1 WHERE medicine_id = ?";
            ps1 = conn.prepareStatement(sqlUpdateInventory);
            ps1.setInt(1, medicineId);
            ps1.executeUpdate();

            // Giả lập lỗi hệ thống tại đây (nếu cần test Rollback)
            // int x = 10 / 0;

            String sqlInsertHistory = "INSERT INTO Prescription_History (patient_id, medicine_id, date) VALUES (?, ?, CURDATE())";
            ps2 = conn.prepareStatement(sqlInsertHistory);
            ps2.setInt(1, patientId);
            ps2.setInt(2, medicineId);
            ps2.executeUpdate();

            // Xác nhận hoàn tất toàn bộ giao dịch
            conn.commit();
            System.out.println("Cấp phát thuốc thành công!");

        } catch (Exception e) {
            if (conn != null) {
                try {
                    // Hủy bỏ mọi thay đổi nếu có lỗi xảy ra
                    conn.rollback();
                    System.err.println("Đã Rollback dữ liệu do có lỗi xảy ra.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Main().capPhatThuoc(101, 2026);
    }
}