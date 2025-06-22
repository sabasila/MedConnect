package Controllers;

import DAOS.DiagnosisDAO;
import DAOS.PatientDAO;
import Class.Diagnosis;
import Class.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

import java.util.List;

public class Doctor_Profile_Controller {

    private int patientId; // პაციენტის ID (როცა ვნახულობთ პაციენტის პროფილს)
    private int doctorId;  // ექიმის ID (თუ ექიმის პროფილის ჩვენება დაგვჭირდება მომავალში)

    @FXML
    private Label patientNameLabel;

    @FXML
    private Label patientPersonalIdLabel;

    @FXML
    private VBox diagnosesBox;

    @FXML
    private TextArea diagnosisInput;

    // საჭირო იმისთვის, რომ არ იყოს წითლად: PatientsSearchController-ში იყენებს
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
        // მომავალში აქ შეიძლება დაემატოს ექიმის ინფორმაციის ჩატვირთვა
    }

    // საჭირო იმისთვის, რომ არ იყოს წითლად: PatientsSearchController-ში იყენებს
    public void setDoctorUserId(int doctorUserId) {
        // ამ ეტაპზე არაფერს აკეთებს
    }

    // გამოიყენება მხოლოდ მაშინ, როცა ვნახულობთ პაციენტის პროფილს
    public void setPatientId(int patientId) {
        this.patientId = patientId;
        loadPatientDetails();
        loadDiagnoses();
    }

    private void loadPatientDetails() {
        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient != null) {
            patientNameLabel.setText(patient.getFullName());
            patientPersonalIdLabel.setText(patient.getPersonalId());
        } else {
            showAlert(Alert.AlertType.ERROR, "პაციენტი ვერ მოიძებნა");
        }
    }

    private void loadDiagnoses() {
        DiagnosisDAO diagnosisDAO = new DiagnosisDAO();
        List<Diagnosis> diagnoses = diagnosisDAO.getDiagnosesByPatientId(patientId);

        diagnosesBox.getChildren().clear();
        for (Diagnosis d : diagnoses) {
            Label label = new Label(d.getDescription() + " (" + d.getCreatedAt() + ")");
            label.setWrapText(true);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5; -fx-background-radius: 5;");
            diagnosesBox.getChildren().add(label);
        }
    }

    @FXML
    private void onAddDiagnosis() {
        String text = diagnosisInput.getText().trim();
        if (text.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "შეიყვანე დიაგნოზი");
            return;
        }

        DiagnosisDAO dao = new DiagnosisDAO();
        Diagnosis d = new Diagnosis();
        d.setPatientId(patientId);
        d.setDescription(text);

        try {
            dao.addDiagnosis(d);
            diagnosisInput.clear();
            loadDiagnoses();
            showAlert(Alert.AlertType.INFORMATION, "დიაგნოზი დაემატა");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "დიაგნოზის დამატება ვერ მოხერხდა");
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
