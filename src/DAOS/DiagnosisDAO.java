package DAOS;
import Class.Diagnosis;
import Database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public void addDiagnosis(Diagnosis diagnosis) {
        String sql = "INSERT INTO diagnoses (patient_id, description) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, diagnosis.getPatientId());
            stmt.setString(2, diagnosis.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Diagnosis> getDiagnosesByPatientId(int patientId) {
        List<Diagnosis> list = new ArrayList<>();
        String sql = "SELECT * FROM diagnoses WHERE patient_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Diagnosis d = new Diagnosis();
                d.setId(rs.getInt("id"));
                d.setPatientId(rs.getInt("patient_id"));
                d.setDescription(rs.getString("description"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}