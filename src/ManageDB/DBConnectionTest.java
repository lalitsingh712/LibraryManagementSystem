package ManageDB;

import java.sql.*;

public class DBConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
        String user = "root";           // apna MySQL username
        String pass = "Indi@123";  // apna MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("✅ Connected to MySQL successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
