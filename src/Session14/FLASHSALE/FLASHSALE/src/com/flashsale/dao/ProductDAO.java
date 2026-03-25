package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        this.connection = DatabaseConnectionManager.getInstance().getConnection();
    }
    
    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(String name, double price, int quantity) throws SQLException {
        String sql = "INSERT INTO Products (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        }
    }
    
    public int getProductQuantity(int productId) throws SQLException {
        String sql = "SELECT quantity FROM Products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity");
                }
            }
        }
        return -1;
    }
    
    public double getProductPrice(int productId) throws SQLException {
        String sql = "SELECT price FROM Products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                }
            }
        }
        return 0.0;
    }

    public void updateProductQuantity(int productId, int quantity) throws SQLException {
        String sql = "UPDATE Products SET quantity = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }
}
