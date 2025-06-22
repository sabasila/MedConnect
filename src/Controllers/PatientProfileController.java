
package Controllers;

import Class.Diagnosis;
import Class.Patient;
import DAOS.DiagnosisDAO;
import DAOS.PatientDAO;
import Controllers.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class PatientProfileController {

    private int userId;
    private String role; // "patient" or "doctor"

    @FXML
    private Label patientName;

    @FXML
    private Label patientPersonalId;

    @FXML
    private VBox diagnosesBox;

    @FXML
    private VBox doctorUI;

    @FXML
    private TextArea diagnosesInput;

    @FXML
    private Button addDiagnosisButton;

    @FXML
    private Button logoutButton;

    private final DiagnosisDAO diagnosisDAO = new DiagnosisDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    public void setUserData(int userId, String role) {
        this.userId = userId;
        this.role = role;

        // თუ მომხმარებელი ექიმია, ვაჩვენებთ ექიმის UI-ს
        boolean isDoctor = "doctor".equalsIgnoreCase(role);

        doctorUI.setVisible(isDoctor);
        doctorUI.setManaged(isDoctor);

        // Logout ღილაკი მხოლოდ პაციენტისთვის გამოჩნდება
        logoutButton.setVisible(!isDoctor);
        logoutButton.setManaged(!isDoctor);

        setPatientId(userId);
    }


    public void setPatientId(int id) {
        Patient patient = patientDAO.getPatientById(id);
        if (patient != null) {
            this.userId = patient.getId();
            patientName.setText(patient.getFullName());
            patientPersonalId.setText(patient.getPersonalId());
            loadDiagnoses();
        }
    }

    private void loadDiagnoses() {
        diagnosesBox.getChildren().clear();
        List<Diagnosis> diagnoses = diagnosisDAO.getDiagnosesByPatientId(userId);
        for (Diagnosis d : diagnoses) {
            Label label = new Label(d.getDescription() + " (" + d.getCreatedAt() + ")");
            label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5;");
            diagnosesBox.getChildren().add(label);
        }
    }

    @FXML
    private void onSubmitDiagnosis() {
        String diagnosisText = diagnosesInput.getText().trim();

        if (!diagnosisText.isEmpty()) {
            Diagnosis newDiagnosis = new Diagnosis();
            newDiagnosis.setPatientId(userId);
            newDiagnosis.setDescription(diagnosisText);
            diagnosisDAO.addDiagnosis(newDiagnosis);
            diagnosesInput.clear();
            loadDiagnoses();
        } else {
            showAlert(Alert.AlertType.WARNING, "გთხოვ შეიყვანე დიაგნოზი");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("შეტყობინება");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setUserData(this.userId, this.role);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("მთავარი გვერდი");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "მთავარ გვერდზე გადასვლა ვერ მოხერხდა");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        switchScene(event, "/FXML/login.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}