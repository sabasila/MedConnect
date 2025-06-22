package Controllers;

import Database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class DoctorProfile_Controller {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField categoryField;
    @FXML private TextField clinicAddressField;
    @FXML private TextArea bioArea;
    @FXML private TextField certificationField;
    @FXML private Label photoNameLabel;
    @FXML private ImageView photoImageView;
    @FXML private Button saveButton;

    private int doctorUserId;
    private String currentPhotoName = "";  // ფოტოს სახელი, რომელიც ბაზაში ინახება

    private final String IMAGES_DIR = "src/images/";

    public void loadDoctorData(int userId) {
        this.doctorUserId = userId;

        try (Connection conn = Database.connect()) {
            String sql = "SELECT u.full_name, u.email, d.category, d.clinic_address, d.bio, d.certification_file, d.photo " +
                    "FROM users u JOIN doctors d ON u.id = d.user_id WHERE u.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fullNameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
                categoryField.setText(rs.getString("category"));
                clinicAddressField.setText(rs.getString("clinic_address"));
                bioArea.setText(rs.getString("bio"));
                certificationField.setText(rs.getString("certification_file"));

                currentPhotoName = rs.getString("photo");
                photoNameLabel.setText(currentPhotoName != null ? currentPhotoName : "");

                if (currentPhotoName != null && !currentPhotoName.isEmpty()) {
                    File imgFile = new File(IMAGES_DIR + currentPhotoName);
                    if (imgFile.exists()) {
                        Image image = new Image(imgFile.toURI().toString());
                        photoImageView.setImage(image);
                    } else {
                        System.err.println("Photo not found at: " + imgFile.getAbsolutePath());
                        photoImageView.setImage(null);
                    }
                } else {
                    photoImageView.setImage(null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "მონაცემების ჩატვირთვა ვერ მოხერხდა");
        }
    }

    public void setDoctorId(int doctorId) {
        loadDoctorData(doctorId);
    }

    @FXML
    private void handleGoHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUserData(this.doctorUserId, "doctor");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("მთავარი გვერდი");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "მთავარ გვერდზე გადასვლა ვერ მოხერხდა");
        }
    }

    @FXML
    private void handleSave() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String category = categoryField.getText().trim();
        String clinicAddress = clinicAddressField.getText().trim();
        String bio = bioArea.getText().trim();
        String certification = certificationField.getText().trim();
        String photo = currentPhotoName;

        if (fullName.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "სახელი და მეილი სავალდებულოა");
            return;
        }

        try (Connection conn = Database.connect()) {
            String userUpdate = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";
            PreparedStatement userStmt = conn.prepareStatement(userUpdate);
            userStmt.setString(1, fullName);
            userStmt.setString(2, email);
            userStmt.setInt(3, doctorUserId);
            userStmt.executeUpdate();

            String doctorUpdate = "UPDATE doctors SET category = ?, clinic_address = ?, bio = ?, certification_file = ?, photo = ? WHERE user_id = ?";
            PreparedStatement doctorStmt = conn.prepareStatement(doctorUpdate);
            doctorStmt.setString(1, category);
            doctorStmt.setString(2, clinicAddress);
            doctorStmt.setString(3, bio);
            doctorStmt.setString(4, certification);
            doctorStmt.setString(5, photo);
            doctorStmt.setInt(6, doctorUserId);
            doctorStmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "მონაცემები წარმატებით შენახულია");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "მონაცემების შენახვა ვერ მოხერხდა");
        }
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("აირჩიე ახალი ფოტო");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("სურათები", "*.jpg", "*.jpeg", "*.png")
        );

        if (photoImageView.getScene() == null) {
            showAlert(Alert.AlertType.ERROR, "ვინდოუსი არ არის მზად ფოტოს ატვირთვისთვის.");
            return;
        }

        File file = fileChooser.showOpenDialog(photoImageView.getScene().getWindow());
        if (file != null && file.exists() && file.isFile()) {
            try {
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) extension = file.getName().substring(i);

                String uniqueName = "doctor_" + UUID.randomUUID() + extension;

                File destDir = new File(IMAGES_DIR);
                if (!destDir.exists()) destDir.mkdirs();

                File destFile = new File(destDir, uniqueName);

                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentPhotoName = uniqueName;
                photoNameLabel.setText(uniqueName);

                Image image = new Image(destFile.toURI().toString());
                photoImageView.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "ფოტოს შეცვლა ვერ მოხერხდა: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "გთხოვთ აირჩიოთ სწორი ფოტო.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "სისტემიდან გამოსვლა ვერ მოხერხდა");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
