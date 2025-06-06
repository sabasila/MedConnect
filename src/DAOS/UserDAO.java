package DAOS;

import java.sql.*;

import Class.User;
import Database.Database;


public class UserDAO {

    public User authenticate(String email, String password) {
        String sql = "SELECT id, user_type FROM users WHERE email = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String userType = rs.getString("user_type");

                User user = new User();
                user.setId(id);
                user.setUserType(userType);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
