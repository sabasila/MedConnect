package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import DAOS.UserDAO;
import Class.User;



public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(email, password);

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/home.fxml"));

                Parent root = loader.load();

                HomeController controller = loader.getController();
                controller.setUserData(user.getId(), user.getUserType());

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("მთავარი გვერდი");
                newStage.show();

                Stage oldStage = (Stage) emailField.getScene().getWindow();
                oldStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("ფაილის ჩატვირთვის შეცდომა.");
            }

        } else {
            showAlert("არასწორი ელ.ფოსტა ან პაროლი");
        }
    }


    @FXML
    private void goToRegisterChoice(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/register_choice.fxml"));
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
