
import java.sql.*;
import java.util.Scanner;

public class SQLInjectionDemo {

    private static final String URL =
            "jdbc:mysql://localhost:3306/assignment5_b2";

    private static final String DB_USER = "root";

    // Replace with your local MySQL password
    private static final String DB_PASSWORD = "YOUR_PASSWORD";

    public static boolean unsafeLogin(
            String username, String password) {

        String sql =
                "SELECT * FROM users WHERE username = '"
                + username + "' AND password = '"
                + password + "'";

        try (
            Connection con = DriverManager.getConnection(
                    URL, DB_USER, DB_PASSWORD);

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql)
        ) {
            return rs.next();

        } catch (SQLException e) {
            System.out.println(
                    "Unsafe login SQL error: " + e.getMessage());
            return false;
        }
    }

    public static boolean safeLogin(
            String username, String password) {

        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ?";

        try (
            Connection con = DriverManager.getConnection(
                    URL, DB_USER, DB_PASSWORD);

            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println(
                    "Safe login SQL error: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("SQL INJECTION DEMONSTRATION");
            System.out.println("===========================");

            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            System.out.println("\nUNSAFE LOGIN RESULT:");

            if (unsafeLogin(username, password)) {
                System.out.println(
                        "Login successful - unsafe query.");
            } else {
                System.out.println("Login rejected.");
            }

            System.out.println("\nSAFE LOGIN RESULT:");

            if (safeLogin(username, password)) {
                System.out.println(
                        "Login successful - PreparedStatement.");
            } else {
                System.out.println(
                        "Login rejected - PreparedStatement.");
            }
        }
    }
}
