import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.*;

public class DoctorRegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField personalIdField;
    @FXML private TextField categoryField;
    @FXML private TextField clinicAddressField;
    @FXML private TextField bioField;

    private String certificationPath = "";
    private String photoPath = "";

    @FXML
    private void handleChooseCertification() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            certificationPath = file.getAbsolutePath();
        }
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            photoPath = file.getAbsolutePath();
        }
    }

    @FXML
    private void handleDoctorRegister() {
        try (Connection conn = Database.connect()) {
            // insert into users table
            String userSql = "INSERT INTO users (full_name, email, password, personal_id, user_type) VALUES (?, ?, ?, ?, 'doctor') RETURNING id";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, fullNameField.getText());
            userStmt.setString(2, emailField.getText());
            userStmt.setString(3, passwordField.getText());
            userStmt.setString(4, personalIdField.getText());
            ResultSet rs = userStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                // insert into doctors table
                String docSql = "INSERT INTO doctors (user_id, category, certification_file, photo, clinic_address, bio) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement docStmt = conn.prepareStatement(docSql);
                docStmt.setInt(1, userId);
                docStmt.setString(2, categoryField.getText());
                docStmt.setString(3, certificationPath);
                docStmt.setString(4, photoPath);
                docStmt.setString(5, clinicAddressField.getText());
                docStmt.setString(6, bioField.getText());
                docStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
