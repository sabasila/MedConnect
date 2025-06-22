package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Class.Doctor;

import Class.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DoctorCardController {

    @FXML private ImageView photoView;
    @FXML private Label nameLabel;
    @FXML private Label bioLabel;
    @FXML private Label ratingLabel;
    @FXML private Label addressLabel;
    @FXML private Button profileBtn;

    private Doctor doctor;

    public void setDoctorData(Doctor doctor) {
        this.doctor = doctor;

        nameLabel.setText(doctor.getFullName());
        bioLabel.setText(doctor.getBio());
        ratingLabel.setText("შეფასება: " + String.format("%.1f", doctor.getAverageRating()) + " ⭐");
        addressLabel.setText("მისამართი: " + doctor.getClinicAddress());

        try {
            String photoFileName = doctor.getPhoto();
            String photoUrl;

            if (photoFileName == null || photoFileName.isEmpty()) {
                int randomIndex = (int) (Math.random() * 5) + 1;
                photoUrl = getClass().getResource("/images/default_doctor_photo" + randomIndex + ".jpg").toExternalForm();
            } else {
                photoUrl = getClass().getResource("/images/" + photoFileName).toExternalForm();
            }

            Image image = new Image(photoUrl, true);
            photoView.setImage(image);
        } catch (Exception e) {
            System.out.println("Image loading error: " + doctor.getPhoto());
        }

        profileBtn.setOnAction(e -> openDoctorProfile());
    }


    private void openDoctorProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/doctor_profile.fxml"));
            Parent profileRoot = loader.load();

            DoctorProfileController controller = loader.getController();
            controller.setDoctorId(doctor.getId());

            // ამოიღე ღილაკიდან მიმდინარე Scene
            Scene currentScene = profileBtn.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();

            // შეცვალე სცენა იმავე ფანჯარაში
            currentStage.setScene(new Scene(profileRoot));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
