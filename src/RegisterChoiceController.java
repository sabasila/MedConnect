import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;

public class RegisterChoiceController {

    // "Register as Doctor" ღილაკის მოქმედება
    public void registerAsDoctor(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("doctor_register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ექიმის რეგისტრაცია");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // "Register as Patient" ღილაკის მოქმედება
    public void registerAsPatient(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("patient_register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("პაციენტის რეგისტრაცია");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
