import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/MedConnect";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Saba1234";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
