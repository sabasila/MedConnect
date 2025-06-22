package Controllers;

import Class.User;
import Database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientsSearchController {

    @FXML
    private TextField patientSearchField;

    @FXML
    private ListView<User> patientListView;

    private List<User> searchResults = new ArrayList<>();

    @FXML
    public void initialize() {
        patientListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                openUserProfile(newVal);
            }
        });
    }

    @FXML
    private void onSearchClicked() {
        String query = patientSearchField.getText().trim();
        searchResults.clear();
        patientListView.getItems().clear();

        if (!query.isEmpty()) {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT id, full_name, personal_id, user_type FROM users " +
                        "WHERE full_name ILIKE ? OR personal_id ILIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + query + "%");
                stmt.setString(2, "%" + query + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("personal_id"),
                            rs.getString("user_type")
                    );
                    searchResults.add(user);
                }

                patientListView.getItems().addAll(searchResults);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void openUserProfile(User user) {
        try {
            FXMLLoader loader;
            Parent root;

            if ("doctor".equalsIgnoreCase(user.getUserType())) {

                loader = new FXMLLoader(getClass().getResource("/FXML/DoctorProfile_.fxml"));
                root = loader.load();

                Doctor_Profile_Controller controller = loader.getController();
                controller.setDoctorId(user.getId());
                controller.setDoctorUserId(user.getId());

            } else if ("patient".equalsIgnoreCase(user.getUserType())) {
                // პაციენტის პროფილი იხსნება
                loader = new FXMLLoader(getClass().getResource("/FXML/PatientProfile.fxml"));
                root = loader.load();

                PatientProfile_Controller controller = loader.getController();
                controller.setPatientId(user.getId());         // პაციენტის ID

            } else {
                // თუ მომხმარებლის ტიპი არ არის doctor ან patient, არაფერი ვაკეთოთ
                return;
            }

            // არსებული ფანჯარაში სცენის შეცვლა
            Stage currentStage = (Stage) patientListView.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle(user.getFullName() + " - პროფილი");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
