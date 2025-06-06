package DAOS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Class.Rating;
import Database.Database;


public class RatingDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    // Adds a new rating to the database
    public void addRating(int doctorId, int patientId, int rating, String comment) {
        String sql = "INSERT INTO ratings (doctor_id, patient_id, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.setInt(3, rating);
            ps.setString(4, comment);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("შეფასების დამატების შეცდომა: " + e.getMessage());
        }
    }

    // Retrieves all ratings for a specific doctor
    public List<Rating> getRatingsByDoctorId(int doctorId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT id, doctor_id, patient_id, rating, comment, created_at FROM ratings WHERE doctor_id = ? ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rating rating = new Rating();
                    rating.setId(rs.getInt("id"));
                    rating.setDoctorId(rs.getInt("doctor_id"));
                    rating.setPatientId(rs.getInt("patient_id"));
                    rating.setRating(rs.getInt("rating"));
                    rating.setComment(rs.getString("comment"));
                    rating.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    ratings.add(rating);
                }
            }

        } catch (SQLException e) {
            System.err.println("შეფასებების წამოღების შეცდომა: " + e.getMessage());
        }
        return ratings;
    }
}
