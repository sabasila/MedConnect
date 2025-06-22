package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import Class.Diagnosis;

import Database.Database;

public class DoctorRegisterController
{

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField personalIdField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField clinicAddressField;
    @FXML private TextArea bioArea;

    @FXML private Label certificationFileLabel;
    @FXML private Label photoFileLabel;

    private String certificationPath = "";
    private String photoPath = "";

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(
                "სტომატოლოგი", "თერაპევტი", "ოფთალოლოგი", "ქირურგი", "ორტოფედი",
                "ფსიქოლოგი", "პედიატრი", "დერმატოლოგი", "უროლოგი",
                "ალერგოლოგი", "სამხედრო მედიცინა"
        );
    }

    @FXML
    private void handleChooseCertification() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("აირჩიეთ დამადასტურებელი დოკუმენტი");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF ფაილები", "*.pdf"),
                new FileChooser.ExtensionFilter("სურათები", "*.jpg", "*.png")
        );
        Window stage = certificationFileLabel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            certificationPath = file.getAbsolutePath();
            certificationFileLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("აირჩიეთ ფოტო");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("სურათები", "*.JPG", "*.jpeg", "*.png")
        );
        Window stage = photoFileLabel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getName().substring(i);
                }
                String uniqueFileName = file.getName().substring(0, i) + "_" + UUID.randomUUID().toString() + extension;

                File destDir = new File("src/images");
                if (!destDir.exists()) {
                    boolean created = destDir.mkdirs();
                    System.out.println("Folder created? " + created);
                }

                File destFile = new File(destDir, uniqueFileName);
                System.out.println("Copying file to: " + destFile.getAbsolutePath());

                java.nio.file.Files.copy(
                        file.toPath(),
                        destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                photoPath = uniqueFileName;
                photoFileLabel.setText(uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "სურათის გადატანის შეცდომა: " + e.getMessage());
            }
        }
    }



    @FXML
    private void handleDoctorRegister(ActionEvent event) {
        // ვალიდაცია
        if (fullNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                personalIdField.getText().isEmpty() ||
                categoryComboBox.getValue() == null ||
                clinicAddressField.getText().isEmpty() ||
                bioArea.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "გთხოვთ, შეავსეთ ყველა ველი სერტიფიკატის გარდა.");
            return;
        }

        if (!emailField.getText().contains("@")) {
            showAlert(Alert.AlertType.WARNING, "გთხოვთ, ჩაწერეთ სწორი ელ.ფოსტა (@ სიმბოლოთი).");
            return;
        }

        if (passwordField.getText().length() < 8) {
            showAlert(Alert.AlertType.WARNING, "პაროლი უნდა შეიცავდეს მინიმუმ 8 სიმბოლოს.");
            return;
        }

        if (!personalIdField.getText().matches("\\d{11}")) {
            showAlert(Alert.AlertType.WARNING, "პირადი ნომერი უნდა შეიცავდეს ზუსტად 11 ციფრს.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String userSql = "INSERT INTO users (full_name, email, password, personal_id, user_type) VALUES (?, ?, ?, ?, 'doctor') RETURNING id";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, fullNameField.getText());
            userStmt.setString(2, emailField.getText());
            userStmt.setString(3, passwordField.getText());
            userStmt.setString(4, personalIdField.getText());
            ResultSet rs = userStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                String docSql = "INSERT INTO doctors (user_id, category, certification_file, photo, clinic_address, bio) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement docStmt = conn.prepareStatement(docSql);
                docStmt.setInt(1, userId);
                docStmt.setString(2, categoryComboBox.getValue());
                docStmt.setString(3, certificationPath);
                docStmt.setString(4, photoPath);
                docStmt.setString(5, clinicAddressField.getText());
                docStmt.setString(6, bioArea.getText());
                docStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "წარმატებით რეგისტრირდით!");
                goToLoginPage(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "დაფიქსირდა შეცდომა რეგისტრაციისას");
        }
    }

    public void goBackToLogin(ActionEvent event) {
        goToLoginPage(event);
    }

    private void goToLoginPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("შესვლა");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

