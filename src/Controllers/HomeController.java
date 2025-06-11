package Controllers;

import DAOS.DoctorDAO;
import Class.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Class.Doctor;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    private VBox topRatedDoctorsBox;

    @FXML
    private Button btnSearchDoctor, btnHome, btnDoctors, btnPatients;

    private final DoctorDAO doctorDAO = new DoctorDAO();

    private int currentUserId;
    private String currentUserType;

    public void setUserData(int userId, String userType) {
        this.currentUserId = userId;
        this.currentUserType = userType;



        if (!"doctor".equals(userType) && btnPatients != null) {
            btnPatients.setVisible(false);
        }

        loadTopRatedDoctors();
    }

    @FXML private TextField searchField;


    private void loadTopRatedDoctors() {
        List<Doctor> topDoctors = doctorDAO.getTopRatedDoctors();

        topRatedDoctorsBox.getChildren().clear();

        for (Doctor doctor : topDoctors) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DoctorCard.fxml"));
                HBox doctorCard = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor);
                controller.setProfileButtonAction(() -> openDoctorProfile(doctor.getId()));

                topRatedDoctorsBox.getChildren().add(doctorCard);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openDoctorProfile(int doctorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/doctor_profile.fxml"));
            Parent root = loader.load();

            DoctorProfileController controller = loader.getController();
            controller.setDoctorId(doctorId);

            Stage stage = (Stage) topRatedDoctorsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onProfileClicked() {
        try {
            FXMLLoader loader;
            if (Session.isDoctor()) {
                loader = new FXMLLoader(getClass().getResource("../FXML/doctor_profile.fxml"));
            } else if (Session.isPatient()) {
                loader = new FXMLLoader(getClass().getResource("../FXML/patient_profile.fxml"));
            } else {
                System.out.println("მომხმარებლის როლი არ არის განსაზღვრული");
                return;
            }

            Parent root = loader.load();

            if (Session.isDoctor()) {
                DoctorProfileController controller = loader.getController();
                controller.setDoctorId(Session.getUserId());
            } else {
                PatientProfileController controller = loader.getController();
                controller.setPatientId(Session.getUserId());
            }


            Stage stage = (Stage) btnDoctors.getScene().getWindow(); // ან ნებისმიერი კომპონენტი
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onSearchDoctorClicked() {
        navigate("../FXML/search_doctor.fxml");
    }

    @FXML
    private void onDoctorsClicked() {
        navigate("../FXML/doctors_page.fxml");
    }

    @FXML
    private void onPatientsClicked() {
        navigate("../FXML/patients_page.fxml");
    }

    @FXML
    private void onHomeClicked() {
        navigate("../FXML/home.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUserData(currentUserId, currentUserType);

            Stage stage = (Stage) topRatedDoctorsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
