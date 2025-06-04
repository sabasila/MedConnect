import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class DoctorProfileController {

    private int doctorId;

    @FXML
    private Label nameLabel;

    @FXML
    private Label bioLabel;

    @FXML
    private ImageView photoImageView;

    @FXML
    private Label ratingLabel;

    @FXML
    private TextArea commentArea;

    @FXML
    private ComboBox<Integer> ratingComboBox;

    @FXML
    private Button submitButton;

    @FXML
    private VBox commentsBox;

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
        loadDoctorDetails();
        loadComments();
    }

    private void loadDoctorDetails() {
        DoctorDAO dao = new DoctorDAO();
        Doctor doctor = dao.getDoctorById(doctorId);

        if (doctor != null) {
            nameLabel.setText(doctor.getFullName());
            bioLabel.setText(doctor.getBio());

            if (doctor.getPhoto() != null && !doctor.getPhoto().isEmpty()) {
                photoImageView.setImage(new Image("file:" + doctor.getPhoto()));
            }

            ratingLabel.setText("შეფასება: " + String.format("%.1f", doctor.getAverageRating()) + " ⭐");
        }
    }

    private void loadComments() {
        DoctorDAO dao = new DoctorDAO();
        List<Rating> ratings = dao.getRatingsForDoctor(doctorId);

        commentsBox.getChildren().clear();
        for (Rating r : ratings) {
            Label commentLabel = new Label("⭐" + r.getRating() + ": " + r.getComment());
            commentsBox.getChildren().add(commentLabel);
        }
    }

    @FXML
    private void initialize() {
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
        submitButton.setOnAction(e -> submitRating());
    }

    private void submitRating() {
        Integer selectedRating = ratingComboBox.getValue();
        String comment = commentArea.getText();

        if (selectedRating == null || comment.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "გთხოვთ შეავსოთ შეფასება და კომენტარი.");
            alert.show();
            return;
        }


        int patientId = 2;

        DoctorDAO dao = new DoctorDAO();
        dao.addRating(doctorId, patientId, selectedRating, comment);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "შეფასება წარმატებით დაემატა!");
        alert.show();

        ratingComboBox.setValue(null);
        commentArea.clear();
        loadDoctorDetails();
        loadComments();
    }
}
