import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try (Connection conn = Database.connect()) {
            String sql = "SELECT id, user_type FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userType = rs.getString("user_type");
                int userId = rs.getInt("id");

                // გადახვედით მთავარ გვერდზე მომხმარებლის ტიპის მიხედვით
                if (userType.equals("doctor")) {
                    // გადადი ექიმის მთავარ გვერდზე
                    System.out.println("Doctor login: ID = " + userId);
                } else {
                    // გადადი პაციენტის მთავარ გვერდზე
                    System.out.println("Patient login: ID = " + userId);
                }
            } else {
                showAlert("არასწორი ელ.ფოსტა ან პაროლი");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRegisterChoice(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("register_choice.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
