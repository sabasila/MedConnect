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

public class DoctorRegisterController {

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
                new FileChooser.ExtensionFilter("სურათები", "*.jpg", "*.jpeg", "*.png")
        );
        Window stage = photoFileLabel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            photoPath = file.getAbsolutePath();
            photoFileLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleDoctorRegister() {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("შესვლა");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
