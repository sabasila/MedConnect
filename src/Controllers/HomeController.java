package Controllers;

import DAOS.DoctorDAO;
import Class.Session;
import Class.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    private VBox topRatedDoctorsBox;

    @FXML
    private Button btnSearchDoctor, btnHome, btnDoctors, btnPatients;

    @FXML
    private TextField searchField;

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

    private void loadTopRatedDoctors() {
        List<Doctor> topDoctors = doctorDAO.getTopRatedDoctors();
        topRatedDoctorsBox.getChildren().clear();

        for (Doctor doctor : topDoctors) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DoctorCard.fxml"));
                HBox doctorCard = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor);

                topRatedDoctorsBox.getChildren().add(doctorCard);

            } catch (IOException e) {
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
            stage.setTitle("ექიმის პროფილი");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onProfileClicked() {
        try {
            FXMLLoader loader;
            Parent root;

            if (Session.isDoctor()) {
                loader = new FXMLLoader(getClass().getResource("/FXML/DoctorProfile.fxml"));
                root = loader.load();
                DoctorProfile_Controller controller = loader.getController();
                controller.setDoctorId(Session.getUserId());
            } else if (Session.isPatient()) {
                loader = new FXMLLoader(getClass().getResource("/FXML/patient_profile.fxml"));
                root = loader.load();
                PatientProfileController controller = loader.getController();
                controller.setPatientId(Session.getUserId());
            } else {
                System.out.println("მომხმარებლის როლი არ არის განსაზღვრული");
                return;
            }

            Stage stage = (Stage) topRatedDoctorsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("პროფილი");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPatientClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PatientsSearch.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("პაციენტების ძებნა");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ძიების ლოგიკა აქვე, აღარ არის navigate სხვა ფაილზე
    @FXML
    private void onSearchDoctorClicked() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadTopRatedDoctors();
            return;
        }

        List<Doctor> searchResults = doctorDAO.searchDoctors(keyword);
        topRatedDoctorsBox.getChildren().clear();

        for (Doctor doctor : searchResults) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DoctorCard.fxml"));
                HBox doctorCard = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor);

                topRatedDoctorsBox.getChildren().add(doctorCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (searchResults.isEmpty()) {
            Label noResults = new Label("ვერ მოიძებნა ექიმი: " + keyword);
            noResults.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            topRatedDoctorsBox.getChildren().add(noResults);
        }
    }

    @FXML
    private void onDoctorsClicked() {
        navigate("/FXML/doctors_page.fxml");
    }

    @FXML
    private void onPatientsClicked() {
        navigate("/FXML/patients_page.fxml");
    }
    @FXML
    private void onLogoClicked() {
        navigate("/FXML/home.fxml");
    }

    @FXML
    private void onHomeClicked() {
        navigate("/FXML/home.fxml");
    }

    private void navigate(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUserData(currentUserId, currentUserType);

            Stage stage = (Stage) topRatedDoctorsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}