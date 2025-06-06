package DAOS;

import Class.Doctor;
import Class.Rating;
import Database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    private Connection conn = Database.getConnection();

    public List<Doctor> getTopRatedDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        String sql = """
            SELECT u.id, u.full_name, d.photo, d.bio, COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM users u
            JOIN doctors d ON u.id = d.user_id
            LEFT JOIN ratings r ON u.id = r.doctor_id
            GROUP BY u.id, d.photo, d.bio
            ORDER BY avg_rating DESC
            LIMIT 5
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setBio(rs.getString("bio"));
                doctor.setAverageRating(rs.getDouble("avg_rating"));

                doctors.add(doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public Doctor getDoctorById(int doctorId) {
        String sql = """
            SELECT u.full_name, d.photo, d.bio,
                   COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM users u
            JOIN doctors d ON u.id = d.user_id
            LEFT JOIN ratings r ON u.id = r.doctor_id
            WHERE u.id = ?
            GROUP BY u.full_name, d.photo, d.bio
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(doctorId);
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setBio(rs.getString("bio"));
                doctor.setAverageRating(rs.getDouble("avg_rating"));
                return doctor;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Rating> getRatingsForDoctor(int doctorId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT rating, comment FROM ratings WHERE doctor_id = ? ORDER BY id DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ratingValue = rs.getInt("rating");
                String comment = rs.getString("comment");
                ratings.add(new Rating(ratingValue, comment));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ratings;
    }

    public void addRating(int doctorId, int patientId, int rating, String comment) {
        String sql = "INSERT INTO ratings (doctor_id, patient_id, rating, comment) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
