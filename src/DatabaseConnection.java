import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hotel_db";

    private static final String USERNAME =
            System.getenv().getOrDefault("HOTEL_DB_USERNAME", "root");

    private static final String PASSWORD =
            System.getenv("HOTEL_DB_PASSWORD");

    // Prevent object creation
    private DatabaseConnection() {
    }

    public static Connection getConnection()
            throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Database password is not configured. " +
                            "Please set the HOTEL_DB_PASSWORD environment variable."
            );
        }

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}