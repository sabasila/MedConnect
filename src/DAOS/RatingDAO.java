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
    public static void addRating(int doctorId, int patientId, int rating, String comment) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO ratings (doctor_id, patient_id, rating, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Rating> getRatingsForDoctor(int doctorId) {
        List<Rating> list = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT rating, comment FROM ratings WHERE doctor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rating r = new Rating();
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
