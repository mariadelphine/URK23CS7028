import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/clinicdb2";
    private static final String USER = "root";  // Replace with your MySQL username
    private static final String PASSWORD = "johndanis06#";  // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL driver (not strictly necessary since JDBC 4.0)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create and return the connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add the MySQL JDBC driver to your build path.");
        }
    }
}