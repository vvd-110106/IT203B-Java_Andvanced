package Session13.Ex2;

import Session11.dbConnect;
import java.sql.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * 1. Vi phạm nguyên tắc Transaction: Việc chỉ dùng System.out.println() trong khối catch
 * chỉ có tác dụng thông báo cho lập trình viên, nhưng Database thì không hề biết có lỗi.
 * Kết nối sẽ bị giữ ở trạng thái "lơ lửng" (pending), gây lãng phí tài nguyên và khóa (lock) dữ liệu.
 * 2. Hành động bị bỏ quên: Lập trình viên đã quên gọi lệnh conn.rollback(). Đây là lệnh
 * bắt buộc để báo cho Database hủy bỏ toàn bộ các thay đổi tạm thời, đưa dữ liệu về
 * trạng thái an toàn trước khi bắt đầu giao dịch.
 */
public class Main {
    public void thanhToanVienPhi(int patientId, int invoiceId, double amount) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            conn = dbConnect.getConnection();

            // Tắt tự động lưu giao dịch
            conn.setAutoCommit(false);

            // Thao tác 1: Trừ tiền trong ví
            String sqlDeductWallet = "UPDATE Patient_Wallet SET balance = balance - ? WHERE patient_id = ?";
            ps1 = conn.prepareStatement(sqlDeductWallet);
            ps1.setDouble(1, amount);
            ps1.setInt(2, patientId);
            ps1.executeUpdate();

            // Thao tác 2: Cập nhật trạng thái hóa đơn (Cố tình viết sai tên bảng để tạo lỗi)
            String sqlUpdateInvoice = "UPDATE Invoicesss SET status = 'PAID' WHERE invoice_id = ?";
            ps2 = conn.prepareStatement(sqlUpdateInvoice);
            ps2.setInt(1, invoiceId);
            ps2.executeUpdate();

            // Xác nhận giao dịch thành công
            conn.commit();
            System.out.println("Thanh toán hoàn tất!");

        } catch (SQLException e) {
            // PHẦN 2 - THỰC THI: BỔ SUNG ROLLBACK TRONG KHỐI CATCH
            System.err.println("Lỗi hệ thống: " + e.getMessage());
            if (conn != null) {
                try {
                    System.out.println("Đang thực hiện Rollback dữ liệu...");
                    conn.rollback(); // Lệnh bắt buộc để khôi phục trạng thái an toàn
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Đóng tài nguyên và giải phóng kết nối
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
        new Main().thanhToanVienPhi(1, 101, 500000);
    }
}