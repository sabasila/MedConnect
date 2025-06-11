package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Class.Doctor;

public class DoctorCardController {

    @FXML private ImageView photoView;
    @FXML private Label nameLabel;
    @FXML private Label bioLabel;
    @FXML private Label ratingLabel;
    @FXML private Label addressLabel;
    @FXML private Button profileBtn;

    public void setDoctorData(Doctor doctor) {
        nameLabel.setText(doctor.getFullName());
        bioLabel.setText(doctor.getBio());
        ratingLabel.setText("შეფასება: " + String.format("%.1f", doctor.getAverageRating()) + " ⭐");
        addressLabel.setText("მისამართი: " + doctor.getClinicAddress());

        try {
            String photoUrl = doctor.getPhoto();
            if (photoUrl == null || photoUrl.isEmpty()) {
                int randomIndex = (int) (Math.random() * 5) + 1;
                photoUrl = getClass().getResource("/images/default_doctor_photo" + randomIndex + ".jpg").toExternalForm();
            }

            Image image = new Image(photoUrl, true);
            photoView.setImage(image);
        } catch (Exception e) {
            System.out.println("Image loading error: " + doctor.getPhoto());
        }
    }


    public void setProfileButtonAction(Runnable action) {
        profileBtn.setOnAction(e -> action.run());
    }
}
