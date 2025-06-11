package Controllers;

import DAOS.DoctorDAO;
import Class.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.util.List;

public class DoctorsByCategoryController {

    @FXML
    private Label categoryLabel;

    @FXML
    private VBox doctorsListBox;

    private final DoctorDAO doctorDAO = new DoctorDAO();

    private String category;

    public void setCategory(String category) {
        this.category = category;
        categoryLabel.setText("ექიმები კატეგორიით: " + category);
        loadDoctorsByCategory();
    }

    private void loadDoctorsByCategory() {
        List<Doctor> doctors = doctorDAO.getDoctorsByCategory(category);

        doctorsListBox.getChildren().clear();

        for (Doctor doctor : doctors) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/DoctorCard.fxml"));
                HBox doctorCard = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor);
                controller.setProfileButtonAction(() -> openDoctorProfile(doctor.getId()));

                doctorsListBox.getChildren().add(doctorCard);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openDoctorProfile(int doctorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/doctor_profile.fxml"));
            Parent root = loader.load();

            DoctorProfileController controller = loader.getController();
            controller.setDoctorId(doctorId);

            Stage stage = (Stage) doctorsListBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/doctors_page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) doctorsListBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
