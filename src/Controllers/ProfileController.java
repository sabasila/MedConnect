package Controllers;

import Class.User;
import Database.Database; // დარწმუნდი, რომ ეს კლასი არსებობს და სწორად იმპორტდება
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private TextField tfFullName;

    @FXML
    private TextField tfPersonalId;

    @FXML
    private TextField tfUserType;

    // დამატებითი ველები, რომლებიც იქნება მხოლოდ ექიმებისთვის
    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfSpecialization;

    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblSpecialization;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;

        tfFullName.setText(user.getFullName());
        tfPersonalId.setText(user.getPersonalId());
        tfUserType.setText(user.getUserType());

        // userType readonly
        tfUserType.setEditable(false);
        tfPersonalId.setEditable(false);

        // თუ ექიმია - აჩვენე დამატებითი ველები, თუ არა - დამალე
        if ("doctor".equalsIgnoreCase(user.getUserType())) {
            // მაგალითისთვის, აქ შენ უნდა იტვირთო ექიმის დანარჩენი დეტალები ბაზიდან

            // მაგალითად, გინდა რომ დაისვას ცარიელი ან ბაზიდან წამოღებული
            tfEmail.setVisible(true);
            tfPhone.setVisible(true);
            tfSpecialization.setVisible(true);

            lblEmail.setVisible(true);
            lblPhone.setVisible(true);
            lblSpecialization.setVisible(true);

            // თუ გაქვს მეტი ინფო User ან სხვა მოდელში, აქ დააყენე
            // მაგალითად:
            // tfEmail.setText(user.getEmail());
            // tfPhone.setText(user.getPhone());
            // tfSpecialization.setText(user.getSpecialization());

        } else {
            // თუ პაციენტი, დამალე ექიმის ველები
            tfEmail.setVisible(false);
            tfPhone.setVisible(false);
            tfSpecialization.setVisible(false);

            lblEmail.setVisible(false);
            lblPhone.setVisible(false);
            lblSpecialization.setVisible(false);
        }
    }

    @FXML
    private void onSaveClicked() {
        // ვალიდაცია (მაგ: აუცილებლად უნდა ჰქონდეს სახელი და პირადი ნომერი)
        if (tfFullName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "შეცდომა", "სახელი აუცილებლად უნდა იყოს შეყვანილი");
            return;
        }

        // ცვლადებში ახალი მნიშვნელობების შენახვა
        currentUser.setFullName(tfFullName.getText().trim());
        // პირადი ნომრის შეცვლა, თუ გინდა, შეზღუდე - აქ თუ editable=false, მომხმარებელი ვერ შეცვლის

        // userType არ იცვლება
        // ახლა შევინახოთ ბაზაში
        boolean success = updateUserInDatabase(currentUser);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "წარმატება", "პროფილი შენახულია.");
        } else {
            showAlert(Alert.AlertType.ERROR, "შეცდომა", "პროფილის შენახვა ვერ მოხერხდა.");
        }
    }

    private boolean updateUserInDatabase(User user) {
        // ეს ლოგიკა უნდა შეადგინო ბაზასთან სამუშაო DAO-ს მიხედვით,
        // აქ არის მხოლოდ მაგალითი, თუ როგორ შეიძლება იყოს:

        String sql = "UPDATE users SET full_name = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setInt(2, user.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
