package Session13.Ex3;

import Session11.dbConnect;
import java.sql.*;

/**
 * BÁO CÁO PHÂN TÍCH:
 * 1. I/O:
 * - Input: maBenhNhan (int), tienVienPhi (double).
 * - Output: Thông báo thành công hoặc ném Exception kèm Rollback nếu có lỗi.
 * 2. Giải pháp: Sử dụng conn.setAutoCommit(false). Dùng PreparedStatement để truy vấn
 * số dư trước khi trừ (Bẫy 1). Kiểm tra giá trị trả về của executeUpdate(), nếu
 * bằng 0 thì chủ động throw Exception (Bẫy 2).
 * 3. Các bước thiết kế: Mở kết nối -> Tắt AutoCommit -> Check số dư -> Update tiền ->
 * Update giường -> Update trạng thái bệnh nhân -> Commit -> Đóng kết nối.
 */
public class Main {
    public void xuatVienVaThanhToan(int maBenhNhan, double tienVienPhi) throws Exception {
        Connection conn = null;
        PreparedStatement psCheck = null;
        PreparedStatement psTien = null;
        PreparedStatement psGiuong = null;
        PreparedStatement psTrangThai = null;

        try {
            conn = dbConnect.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // --- BẪY 1: Kiểm tra số dư tạm ứng ---
            String sqlCheck = "SELECT so_du_tam_ung FROM Patients WHERE id = ?";
            psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, maBenhNhan);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                double soDuHienTai = rs.getDouble("so_du_tam_ung");
                if (soDuHienTai < tienVienPhi) {
                    throw new Exception("LỖI NGHIỆP VỤ: Số dư không đủ để thanh toán viện phí!");
                }
            } else {
                throw new Exception("LỖI DỮ LIỆU: Không tìm thấy bệnh nhân mã " + maBenhNhan);
            }

            // --- Bước 1: Trừ tiền viện phí ---
            String sqlTien = "UPDATE Patients SET so_du_tam_ung = so_du_tam_ung - ? WHERE id = ?";
            psTien = conn.prepareStatement(sqlTien);
            psTien.setDouble(1, tienVienPhi);
            psTien.setInt(2, maBenhNhan);
            psTien.executeUpdate();

            // --- Bước 2: Giải phóng giường bệnh ---
            // Giả sử bảng Beds có cột patient_id để biết giường đó của ai
            String sqlGiuong = "UPDATE Beds SET status = 'Trống' WHERE patient_id = ?";
            psGiuong = conn.prepareStatement(sqlGiuong);
            psGiuong.setInt(1, maBenhNhan);
            int rowsGiuong = psGiuong.executeUpdate();

            // --- BẪY 2: Kiểm tra dữ liệu ảo (Row Affected == 0) ---
            if (rowsGiuong == 0) {
                throw new Exception("LỖI DỮ LIỆU: Bệnh nhân không có giường hoặc mã bệnh nhân sai!");
            }

            // --- Bước 3: Cập nhật trạng thái bệnh nhân ---
            String sqlTrangThai = "UPDATE Patients SET status = 'Đã xuất viện' WHERE id = ?";
            psTrangThai = conn.prepareStatement(sqlTrangThai);
            psTrangThai.setInt(1, maBenhNhan);
            int rowsStatus = psTrangThai.executeUpdate();

            if (rowsStatus == 0) {
                throw new Exception("LỖI: Không thể cập nhật trạng thái xuất viện!");
            }

            // XÁC NHẬN TẤT CẢ THÀNH CÔNG
            conn.commit();
            System.out.println("Chúc mừng! Bệnh nhân " + maBenhNhan + " đã xuất viện thành công.");

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Hủy bỏ mọi thay đổi nếu dính bất kỳ bẫy nào
                System.err.println("Hệ thống đã ROLLBACK: " + e.getMessage());
            }
        } finally {
            if (psCheck != null) psCheck.close();
            if (psTien != null) psTien.close();
            if (psGiuong != null) psGiuong.close();
            if (psTrangThai != null) psTrangThai.close();
            if (conn != null) conn.close();
        }
    }

    public static void main(String[] args) {
        try {
            new Main().xuatVienVaThanhToan(1005, 1250000.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}