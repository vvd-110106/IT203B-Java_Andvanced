package Session12.Ex4;

import Session11.dbConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Giả lập lớp đối tượng kết quả xét nghiệm
class TestResult {
    private String data;
    public TestResult(String data) { this.data = data; }
    public String getData() { return data; }
}

public class Main {
    public static void main(String[] args) {
        // Giả lập danh sách 1000 kết quả xét nghiệm
        List<TestResult> list = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            list.add(new TestResult("Kết quả xét nghiệm số " + i));
        }

        String sql = "INSERT INTO Results(data) VALUES(?)";

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Vô hiệu hóa Auto-commit để tối ưu hiệu năng và đảm bảo tính toàn vẹn
            conn.setAutoCommit(false);

            System.out.println("Đang bắt đầu xử lý Batch cho " + list.size() + " bản ghi...");
            long startTime = System.currentTimeMillis();

            for (TestResult tr : list) {
                pstmt.setString(1, tr.getData());
                // Thêm vào hàng đợi Batch thay vì thực thi ngay
                pstmt.addBatch();
            }

            // Thực thi toàn bộ Batch
            int[] results = pstmt.executeBatch();

            // Commit toàn bộ giao dịch
            conn.commit();

            long endTime = System.currentTimeMillis();
            System.out.println("Xử lý hoàn tất!");
            System.out.println("Số dòng đã chèn: " + results.length);
            System.out.println("Thời gian thực hiện: " + (endTime - startTime) + "ms");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}