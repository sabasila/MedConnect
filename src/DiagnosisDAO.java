import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }


    public List<Diagnosis> getDiagnosesByPatientId(int patientId) {
        List<Diagnosis> diagnoses = new ArrayList<>();
        String sql = "SELECT id, patient_id, description, created_at FROM diagnoses WHERE patient_id = ? ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Diagnosis diagnosis = new Diagnosis();
                    diagnosis.setId(rs.getInt("id"));
                    diagnosis.setPatientId(rs.getInt("patient_id"));
                    diagnosis.setDescription(rs.getString("description"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        diagnosis.setCreatedAt(ts.toLocalDateTime());
                    } else {
                        diagnosis.setCreatedAt(null);
                    }

                    diagnoses.add(diagnosis);
                }
            }

        } catch (SQLException e) {
            System.err.println("დიაგნოზების წამოღების შეცდომა: " + e.getMessage());
        }

        return diagnoses;
    }
}
