package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) cho Đơn hàng.
 * Tách biệt logic truy xuất dữ liệu (Data Access Layer) khỏi logic nghiệp vụ (Service Layer).
 */
public class OrderDAO {
    private Connection connection;

    // Constructor mặc định cho các tác vụ đơn lẻ
    public OrderDAO() {
        this.connection = DatabaseConnectionManager.getInstance().getConnection();
    }
    
    // Constructor nhận Connection bên ngoài (được sử dụng trong Transaction)
    // Để đảm bảo OrderDAO và ProductDAO sử dụng chung 1 Connection trong cùng 1 Transaction
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Tạo đơn hàng mới trong bảng Orders.
     * Sử dụng Statement.RETURN_GENERATED_KEYS để lấy ID của đơn hàng vừa tạo.
     */
    public int createOrder(int userId, double totalAmount) throws SQLException {
        String sql = "INSERT INTO Orders (user_id, total_amount) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setDouble(2, totalAmount);
            statement.executeUpdate();
            
            // Lấy ID tự sinh (AUTO_INCREMENT) từ database
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về Order ID
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Thêm nhiều chi tiết đơn hàng cùng lúc (Batch Processing).
     * Giúp giảm tải cho Database bằng cách gửi 1 lệnh SQL chứa nhiều bản ghi thay vì gửi nhiều lệnh nhỏ.
     * Cực kỳ quan trọng khi xử lý lượng dữ liệu lớn trong Flash Sale.
     */
    public void addOrderDetailsBatch(int orderId, List<Map<String, Object>> details) throws SQLException {
        String sql = "INSERT INTO Order_Details (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Duyệt danh sách và thêm vào Batch (chưa gửi đi ngay)
            for (Map<String, Object> detail : details) {
                statement.setInt(1, orderId);
                statement.setInt(2, (Integer) detail.get("productId"));
                statement.setInt(3, (Integer) detail.get("quantity"));
                statement.setDouble(4, (Double) detail.get("unitPrice"));
                statement.addBatch(); // Thêm vào hàng đợi Batch
            }
            // Gửi toàn bộ Batch xuống DB cùng lúc
            statement.executeBatch();
        }
    }
}
