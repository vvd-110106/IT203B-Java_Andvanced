package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.service;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseConnectionManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {
    private Connection connection;

    public ReportService() {
        this.connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    public void getTopBuyers() {
        String sql = "{CALL SP_GetTopBuyers()}";
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("Top 5 Buyers:");
            while (rs.next()) {
                System.out.println(rs.getString("username") + " - Spent: " + rs.getDouble("total_spent"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getProductRevenue() {
        String sql = "{CALL SP_GetProductRevenue()}";
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("Product Revenue:");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " - Revenue: " + rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
