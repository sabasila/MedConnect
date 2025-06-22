package Controllers;

import DAOS.DoctorDAO;
import Class.Doctor;
import Class.Rating;
import Class.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DoctorProfileController {

    private int doctorId;

    @FXML private Label nameLabel;
    @FXML private Label bioLabel;
    @FXML private ImageView photoImageView;
    @FXML private Label ratingLabel;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextArea commentArea;
    @FXML private Button submitButton;
    @FXML private VBox commentsBox;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
        submitButton.setOnAction(e -> submitRating());
    }

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
                try {
                    String photoPath = getClass().getResource("/images/" + doctor.getPhoto()).toExternalForm();
                    photoImageView.setImage(new Image(photoPath));
                } catch (Exception e) {
                    System.out.println("ფოტო ვერ მოიძებნა: " + doctor.getPhoto());
                    photoImageView.setImage(null);
                }
            } else {
                photoImageView.setImage(null);
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
            commentLabel.setWrapText(true);
            commentsBox.getChildren().add(commentLabel);
        }
    }

    private void submitRating() {
        Integer selectedRating = ratingComboBox.getValue();
        String comment = commentArea.getText();

        if (selectedRating == null || comment.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "გთხოვთ შეავსოთ შეფასება და კომენტარი.");
            return;
        }

        int patientId = Session.getUserId();
        if (patientId == 0) {
            showAlert(Alert.AlertType.ERROR, "სისტემაში შესვლა აუცილებელია შეფასების დასამატებლად.");
            return;
        }

        DoctorDAO dao = new DoctorDAO();
        try {
            dao.addRating(doctorId, patientId, selectedRating, comment);

            showAlert(Alert.AlertType.INFORMATION, "შეფასება წარმატებით დაემატა!");

            ratingComboBox.setValue(null);
            commentArea.clear();

            loadDoctorDetails();
            loadComments();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "შეფასების დამატება ვერ მოხერხდა.");
        }
    }

    @FXML
    private void handleGoHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUserData(Session.getUserId(), Session.getRole());  // აქ getRole() გამოიყენე

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("მთავარი გვერდი");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "მთავარ გვერდზე გადასვლა ვერ მოხერხდა.");
            alert.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}