import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class HomeController {

    @FXML
    private VBox topRatedDoctorsBox;

    @FXML
    private Button btnSearchDoctor, btnHome, btnDoctors, btnPatients;

    private DoctorDAO doctorDAO = new DoctorDAO();

    private int currentUserId;
    private String currentUserType;


    public void setUserData(int userId, String userType) {
        this.currentUserId = userId;
        this.currentUserType = userType;

        loadTopRatedDoctors();

        if (!"doctor".equals(currentUserType)) {
            btnPatients.setVisible(false);
        }
    }

    private void loadTopRatedDoctors() {
        List<Doctor> topDoctors = doctorDAO.getTopRatedDoctors();

        topRatedDoctorsBox.getChildren().clear();

        for (Doctor doctor : topDoctors) {
            HBox doctorCard = new HBox(10);
            doctorCard.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-background-radius: 10;");

            ImageView photo = new ImageView(new Image(doctor.getPhoto()));
            photo.setFitWidth(80);
            photo.setFitHeight(80);

            VBox infoBox = new VBox(5);
            Label name = new Label("სახელი: " + doctor.getFullName());
            Label bio = new Label("მოკლე აღწერა: " + doctor.getBio());
            Label rating = new Label("შეფასება: " + String.format("%.1f", doctor.getAverageRating()) + " ⭐");

            Button viewBtn = new Button("პროფილი");
            viewBtn.setOnAction(e -> openDoctorProfile(doctor.getId()));

            infoBox.getChildren().addAll(name, bio, rating, viewBtn);
            doctorCard.getChildren().addAll(photo, infoBox);

            topRatedDoctorsBox.getChildren().add(doctorCard);
        }
    }

    private void openDoctorProfile(int doctorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor_profile.fxml"));
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
    private void onSearchDoctorClicked() {
        navigate("search_doctor.fxml");
    }

    @FXML
    private void onDoctorsClicked() {
        navigate("doctors_page.fxml");
    }

    @FXML
    private void onPatientsClicked() {
        navigate("patients_page.fxml");
    }

    @FXML
    private void onHomeClicked() {
        navigate("home.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) topRatedDoctorsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
