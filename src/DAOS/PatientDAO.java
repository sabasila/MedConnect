package DAOS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Class.Patient;
import Database.Database;


public class PatientDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public Patient getPatientById(int patientId) {
        String sql = "SELECT u.id, u.full_name, u.personal_id " +
                "FROM users u " +
                "JOIN patients p ON u.id = p.user_id " +
                "WHERE u.id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setFullName(rs.getString("full_name"));
                    patient.setPersonalId(rs.getString("personal_id"));
                    return patient;
                }
            }
        } catch (SQLException e) {
            System.err.println("პაციენტის წამოღების შეცდომა: " + e.getMessage());
        }
        return null;
    }
}
