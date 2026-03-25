package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    public void addUser(String username, String email) throws SQLException {
        String sql = "INSERT INTO Users (username, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public void getUser(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("User: " + resultSet.getString("username"));
                }
            }
        }
    }
}
