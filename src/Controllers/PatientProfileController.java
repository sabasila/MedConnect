package Controllers;

import DAOS.DiagnosisDAO;
import DAOS.PatientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import Class.Patient;
import Class.Diagnosis;
import DAOS.RatingDAO;
import Database.Database;


public class PatientProfileController {
    private int patientId;

    @FXML private Label patientName;
    @FXML private Label patientPersonalId;
    @FXML private VBox diagnosesBox;

    // Class.Doctor rating UI elements
    @FXML private HBox starRatingBox;
    @FXML private TextArea ratingCommentArea;
    @FXML private Button submitRatingBtn;

    private int selectedRating = 0;

    private DiagnosisDAO diagnosisDAO = new DiagnosisDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private RatingDAO ratingDAO = new RatingDAO();

    public void setPatientId(int id) {
        this.patientId = id;
        loadPatientData();
        loadDiagnoses();
        setupStarRating();
    }

    private void loadPatientData() {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient != null) {
            patientName.setText(patient.getFullName());
            patientPersonalId.setText(patient.getPersonalId());
        }
    }

    private void loadDiagnoses() {
        List<Diagnosis> diagnoses = diagnosisDAO.getDiagnosesByPatientId(patientId);
        diagnosesBox.getChildren().clear();
        for (Diagnosis d : diagnoses) {
            Label label = new Label(d.getDescription() + " (" + d.getCreatedAt() + ")");
            diagnosesBox.getChildren().add(label);
        }
    }

    // ვარსკვლავების შექმნა და არჩევის ფუნქცია
    private void setupStarRating() {
        starRatingBox.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆");
            final int starValue = i;
            star.setStyle("-fx-font-size: 24; -fx-cursor: hand;");
            star.setOnMouseClicked(e -> {
                selectedRating = starValue;
                updateStars();
            });
            starRatingBox.getChildren().add(star);
        }
    }

    private void updateStars() {
        for (int i = 0; i < 5; i++) {
            Label star = (Label) starRatingBox.getChildren().get(i);
            if (i < selectedRating) {
                star.setText("★");
            } else {
                star.setText("☆");
            }
        }
    }

    // ექიმისთვის შეფასების დამატება (მაგალითად, ღილაკით)
    @FXML
    private void onSubmitRating() {
        if (selectedRating == 0) {
            System.out.println("გთხოვთ აირჩიოთ შეფასება ვარსკვლავებით");
            return;
        }

        String comment = ratingCommentArea.getText().trim();
        int doctorId = getCurrentDoctorIdSomehow(); // TODO: პოულობს ექიმის ID-ს

        ratingDAO.addRating(doctorId, patientId, selectedRating, comment);

        // შეფასების დამატების შემდეგ - კომენტარი და ვარსკვლავები გაასუფთავე
        ratingCommentArea.clear();
        selectedRating = 0;
        updateStars();

        System.out.println("შეფასება დამატებულია წარმატებით");
    }

    // TODO: რეალური მეთოდი ექიმის ID-ს მისაღებად (მომავალი კონტექსტიდან ან სესიისგან)
    private int getCurrentDoctorIdSomehow() {
        return 1; // ტესტისთვის, შეცვალე საჭიროების მიხედვით
    }
}
