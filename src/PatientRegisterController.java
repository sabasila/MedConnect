import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class PatientRegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField personalIdField;

    @FXML
    private void handleRegisterPatient(ActionEvent event) {
        try (Connection conn = Database.connect()) {
            String userSql = "INSERT INTO users (full_name, email, password, personal_id, user_type) VALUES (?, ?, ?, ?, 'patient') RETURNING id";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, fullNameField.getText());
            userStmt.setString(2, emailField.getText());
            userStmt.setString(3, passwordField.getText());
            userStmt.setString(4, personalIdField.getText());
            ResultSet rs = userStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                String patientSql = "INSERT INTO patients (user_id) VALUES (?)";
                PreparedStatement patStmt = conn.prepareStatement(patientSql);
                patStmt.setInt(1, userId);
                patStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "წარმატებით რეგისტრირდით!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "დაფიქსირდა შეცდომა რეგისტრაციისას");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("შესვლა");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
