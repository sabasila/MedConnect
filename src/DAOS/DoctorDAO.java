package DAOS;

import Class.Doctor;
import Class.Rating;
import Database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> getTopRatedDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        String sql = """
            SELECT u.id, u.full_name, d.photo, d.bio, d.clinic_address,
                   COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM users u
            JOIN doctors d ON u.id = d.user_id
            LEFT JOIN ratings r ON u.id = r.doctor_id
            GROUP BY u.id, d.photo, d.bio, d.clinic_address
            ORDER BY avg_rating DESC
            LIMIT 5
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setBio(rs.getString("bio"));
                doctor.setClinicAddress(rs.getString("clinic_address"));
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
            SELECT u.full_name, d.photo, d.bio, d.clinic_address,
                   COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM users u
            JOIN doctors d ON u.id = d.user_id
            LEFT JOIN ratings r ON u.id = r.doctor_id
            WHERE u.id = ?
            GROUP BY u.full_name, d.photo, d.bio, d.clinic_address
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(doctorId);
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setBio(rs.getString("bio"));
                doctor.setClinicAddress(rs.getString("clinic_address"));
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

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);

            int rows = stmt.executeUpdate();
            System.out.println("Rating insert successful. Rows affected: " + rows);

        } catch (SQLException e) {
            System.err.println("Error while inserting rating:");
            e.printStackTrace();
            if (e.getSQLState().equals("23505")) { // duplicate key violation
                System.err.println("ამ ექიმისთვის უკვე შეფასებულია ამ მომხმარებლის მიერ.");
            } else {
                e.printStackTrace();
            }
        }
    }
    public List<Doctor> searchDoctors(String keyword) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT u.id, u.full_name, u.email, d.category, d.certification_file, d.photo, d.clinic_address, d.bio " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.id " +
                "WHERE u.full_name ILIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setEmail(rs.getString("email"));
                doctor.setCategory(rs.getString("category"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setClinicAddress(rs.getString("clinic_address"));
                doctor.setBio(rs.getString("bio"));

                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }



    public static List<Doctor> getDoctorsByCategory(String category) {
        List<Doctor> doctors = new ArrayList<>();

        String sql = """
            SELECT u.id, u.full_name, d.photo, d.bio, d.clinic_address,
                   COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM users u
            JOIN doctors d ON u.id = d.user_id
            LEFT JOIN ratings r ON u.id = r.doctor_id
            WHERE d.category = ?
            GROUP BY u.id, u.full_name, d.photo, d.bio, d.clinic_address
            ORDER BY avg_rating DESC
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhoto(rs.getString("photo"));
                doctor.setBio(rs.getString("bio"));
                doctor.setClinicAddress(rs.getString("clinic_address"));
                doctor.setAverageRating(rs.getDouble("avg_rating"));

                doctors.add(doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM doctors ORDER BY category ASC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}
