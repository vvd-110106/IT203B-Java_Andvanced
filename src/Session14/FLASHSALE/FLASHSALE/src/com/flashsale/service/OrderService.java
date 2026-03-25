package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.service;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao.OrderDAO;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao.ProductDAO;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service quản lý nghiệp vụ đặt hàng.
 * Chịu trách nhiệm xử lý Transaction (Giao dịch) để đảm bảo tính toàn vẹn dữ liệu.
 */
public class OrderService {
    
    public OrderService() {
        // Constructor mặc định
    }
    
    // Phương thức tiện ích để đặt 1 sản phẩm
    public void placeOrder(int userId, int productId, int quantity) {
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("quantity", quantity);
        items.add(item);
        
        placeOrder(userId, items);
    }

    /**
     * Xử lý đặt hàng với Transaction Management.
     * Đảm bảo tính chất ACID: Atomicity, Consistency, Isolation, Durability.
     */
    public void placeOrder(int userId, List<Map<String, Object>> items) {
        Connection connection = null;
        try {
            // 1. Lấy kết nối mới cho mỗi giao dịch (đảm bảo Thread-safety)
            connection = DatabaseConnectionManager.getInstance().getConnection();
            
            // 2. Tắt chế độ tự động commit để bắt đầu Transaction thủ công
            connection.setAutoCommit(false);
            
            // 3. Thiết lập mức độ cô lập cao nhất (SERIALIZABLE) để chống hiện tượng
            // "Lost Update" hoặc "Phantom Read" khi nhiều người cùng mua hàng.
            // Điều này giúp khóa (lock) dữ liệu chặt chẽ nhất.
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // 4. Khởi tạo DAO với kết nối hiện tại để chúng cùng nằm trong 1 Transaction
            ProductDAO productDAO = new ProductDAO(connection);
            OrderDAO orderDAO = new OrderDAO(connection);

            double totalAmount = 0;
            List<Map<String, Object>> orderDetails = new ArrayList<>();

            // 5. Duyệt qua từng sản phẩm để kiểm tra và trừ kho
            for (Map<String, Object> item : items) {
                int productId = (Integer) item.get("productId");
                int quantity = (Integer) item.get("quantity");
                
                // Kiểm tra số lượng tồn kho hiện tại
                int availableQuantity = productDAO.getProductQuantity(productId);
                
                if (availableQuantity >= quantity) {
                    double price = productDAO.getProductPrice(productId);
                    totalAmount += price * quantity;

                    // Trừ tồn kho. Nếu có lỗi DB hoặc ràng buộc CHECK, sẽ ném Exception
                    productDAO.updateProductQuantity(productId, availableQuantity - quantity);
                    
                    // Chuẩn bị dữ liệu cho chi tiết đơn hàng
                    Map<String, Object> detail = new HashMap<>(item);
                    detail.put("unitPrice", price);
                    orderDetails.add(detail);
                } else {
                    // Nếu không đủ hàng, ném lỗi để kích hoạt Rollback toàn bộ
                    throw new SQLException("Hết hàng! Sản phẩm ID: " + productId);
                }
            }
            
            // 6. Tạo đơn hàng chính (Orders table)
            int orderId = orderDAO.createOrder(userId, totalAmount);
            
            // 7. Thêm chi tiết đơn hàng (Sử dụng Batch Processing để tối ưu hiệu năng)
            orderDAO.addOrderDetailsBatch(orderId, orderDetails);
            
            // 8. Nếu tất cả thành công, Commit (Lưu) vào Database
            connection.commit();
            System.out.println("Đặt hàng thành công! Đã lưu " + items.size() + " sản phẩm.");
            
        } catch (SQLException e) {
            System.out.println("Giao dịch thất bại: " + e.getMessage());
            if (connection != null) {
                try {
                    // 9. Nếu có bất kỳ lỗi nào, Rollback (Hoàn tác) về trạng thái ban đầu
                    // Đảm bảo không bị trừ kho nếu đơn hàng chưa được tạo xong
                    connection.rollback();
                    System.out.println("Đã Rollback lại dữ liệu.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    // 10. Trả lại trạng thái mặc định và đóng kết nối
                    connection.setAutoCommit(true); 
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
