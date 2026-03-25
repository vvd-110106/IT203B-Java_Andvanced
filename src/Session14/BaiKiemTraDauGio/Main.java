package Session14.BaiKiemTraDauGio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        transferMoney("ACC01", "ACC02", 1000.0);
    }

    public static void transferMoney(String senderId, String receiverId, double amount) {
        Connection conn = null;
        try {
            conn = dbConnect.getConnection();
            if (conn == null) return;

            conn.setAutoCommit(false);

            String sqlCheck = "SELECT Balance FROM Accounts WHERE AccountId = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setString(1, senderId);
                ResultSet rs = psCheck.executeQuery();
                if (!rs.next() || rs.getDouble("Balance") < amount) {
                    System.out.println("Lỗi: Tài khoản không tồn tại hoặc không đủ số dư!");
                    return;
                }
            }

            try (CallableStatement cstmt = conn.prepareCall("{call sp_UpdateBalance(?, ?)}")) {
                cstmt.setString(1, senderId);
                cstmt.setDouble(2, -amount);
                cstmt.executeUpdate();

                cstmt.setString(1, receiverId);
                cstmt.setDouble(2, amount);
                cstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Giao dịch thành công!");

            showFinalResults(conn, senderId, receiverId);

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private static void showFinalResults(Connection conn, String id1, String id2) throws SQLException {
        String sql = "SELECT * FROM Accounts WHERE AccountId IN (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id1);
            ps.setString(2, id2);
            ResultSet rs = ps.executeQuery();
            System.out.println("\nID | Họ Tên | Số Dư Cuối");
            while (rs.next()) {
                System.out.printf("%s | %s | %.2f\n", rs.getString(1), rs.getString(2), rs.getDouble(3));
            }
        }
    }
}