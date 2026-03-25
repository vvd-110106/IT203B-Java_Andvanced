package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý kết nối cơ sở dữ liệu.
 * Sử dụng Design Pattern Singleton để đảm bảo chỉ có một instance quản lý kết nối.
 */
public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;
    
    // Thông tin kết nối
    private static final String URL = "jdbc:mysql://localhost:3306/flashsale_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; 

    // Constructor private để ngăn việc tạo instance từ bên ngoài
    private DatabaseConnectionManager() {
        try {
            // Load driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Phương thức getInstance() trả về instance duy nhất của class (Thread-safe)
    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    /**
     * Lấy một kết nối mới từ database.
     * Lưu ý: Mỗi thread gọi hàm này sẽ nhận được một Connection riêng biệt để đảm bảo Thread-safety
     * khi thực hiện các giao dịch (Transactions).
     *
     * @return Connection đối tượng kết nối
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
