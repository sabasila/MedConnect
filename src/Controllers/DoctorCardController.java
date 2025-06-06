package Controllers;

import Class.Doctor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DoctorCardController {

    @FXML
    private ImageView photoView;

    @FXML
    private Label nameLabel, bioLabel, ratingLabel, addressLabel;

    @FXML
    private Button profileBtn;

    private int doctorId;

    public void setDoctorData(Doctor doctor, Runnable onProfileClick) {
        this.doctorId = doctor.getId();

        nameLabel.setText("სახელი: " + doctor.getFullName());
        bioLabel.setText("მოკლე აღწერა: " + doctor.getBio());
        ratingLabel.setText("შეფასება: " + String.format("%.1f", doctor.getAverageRating()) + " ⭐");
        addressLabel.setText("მისამართი: " + doctor.getClinicAddress()); // თუ გინდა დაამატე ეს კლასი Doctor-ში

        try {
            photoView.setImage(new Image(doctor.getPhoto()));
        } catch (Exception e) {
            System.out.println("ფოტოს ჩატვირთვის შეცდომა");
        }

        profileBtn.setOnAction(e -> onProfileClick.run());
    }
}
