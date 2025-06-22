package Controllers;

import Class.Diagnosis;
import Class.Patient;
import DAOS.DiagnosisDAO;
import DAOS.PatientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PatientProfile_Controller {

    private int patientId;

    @FXML private Label patientName;
    @FXML private Label patientPersonalId;
    @FXML private VBox diagnosesBox;
    @FXML private TextArea diagnosesInput;

    private final DiagnosisDAO diagnosisDAO = new DiagnosisDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    public void setPatientData(int id, String fullName, String personalId) {
        this.patientId = id;
        patientName.setText(fullName);
        patientPersonalId.setText(personalId);
        loadDiagnoses();
    }

    public void setPatientId(int id) {
        this.patientId = id;
        loadPatientData();
        loadDiagnoses();
    }

    private void loadPatientData() {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient != null) {
            patientName.setText(patient.getFullName());
            patientPersonalId.setText(patient.getPersonalId());
        }
    }

    private void loadDiagnoses() {
        diagnosesBox.getChildren().clear();
        List<Diagnosis> diagnoses = diagnosisDAO.getDiagnosesByPatientId(patientId);
        for (Diagnosis d : diagnoses) {
            Label label = new Label(d.getDescription() + " (" + d.getCreatedAt() + ")");
            label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5;");
            diagnosesBox.getChildren().add(label);
        }
    }
    @FXML
    private void handleCloseWindow(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    private void onSubmitDiagnosis() {
        String diagnosisText = diagnosesInput.getText().trim();

        if (!diagnosisText.isEmpty()) {
            Diagnosis newDiagnosis = new Diagnosis();
            newDiagnosis.setPatientId(patientId);
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

}