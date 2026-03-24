package Session13.Ex4;

import Session11.dbConnect;
import java.sql.*;
import java.util.*;

/**
 * PHẦN 1 - PHÂN TÍCH:
 * - Giải pháp chọn: LEFT JOIN Query (1 Query duy nhất).
 * - Cơ chế: Dùng LinkedHashMap để ánh xạ ID bệnh nhân với Object DTO.
 * Nếu ID đã tồn tại trong Map, chỉ thêm dịch vụ mới vào danh sách của bệnh nhân đó.
 */
public class DashboardManager {

    public List<BenhNhanDTO> getDashboardData() {
        // Sử dụng LinkedHashMap để giữ đúng thứ tự bệnh nhân từ Database trả về
        Map<Integer, BenhNhanDTO> mapBenhNhan = new LinkedHashMap<>();

        // SQL LEFT JOIN: Đảm bảo bệnh nhân chưa có dịch vụ vẫn xuất hiện (Bẫy 2)
        String sql = "SELECT b.id, b.ten_benh_nhan, d.ten_dich_vu, d.gia_tien " +
                "FROM BenhNhan b " +
                "LEFT JOIN DichVuSuDung d ON b.id = d.ma_benh_nhan " +
                "ORDER BY b.id ASC";

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                // Nếu bệnh nhân chưa có trong Map thì tạo mới
                if (!mapBenhNhan.containsKey(id)) {
                    BenhNhanDTO dto = new BenhNhanDTO();
                    dto.setId(id);
                    dto.setTenBenhNhan(rs.getString("ten_benh_nhan"));
                    dto.setDsDichVu(new ArrayList<>());
                    mapBenhNhan.put(id, dto);
                }

                // XỬ LÝ BẪY 2: Kiểm tra xem có dịch vụ đi kèm không
                String tenDV = rs.getString("ten_dich_vu");
                if (tenDV != null) {
                    // Chỉ add vào danh sách nếu ten_dich_vu không phải NULL (nhờ LEFT JOIN)
                    DichVu dv = new DichVu(tenDV, rs.getDouble("gia_tien"));
                    mapBenhNhan.get(id).getDsDichVu().add(dv);
                }
                // Nếu tenDV là null (bệnh nhân mới), dsDichVu sẽ để trống, không gây lỗi
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(mapBenhNhan.values());
    }

    public static void main(String[] args) {
        DashboardManager manager = new DashboardManager();
        List<BenhNhanDTO> list = manager.getDashboardData();

        System.out.println("--- DASHBOARD Y TÁ TRƯỞNG ---");
        for (BenhNhanDTO bn : list) {
            System.out.println("Bệnh nhân: " + bn.getTenBenhNhan() +
                    " | Số dịch vụ: " + bn.getDsDichVu().size());
        }
    }
}