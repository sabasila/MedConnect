package Controllers;

import DAOS.DoctorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class DoctorsPageController {

    @FXML
    private VBox categoriesBox;

    private final DoctorDAO doctorDAO = new DoctorDAO();

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {

        List<String> categories = doctorDAO.getAllCategories();

        for (String category : categories) {
            Button btn = new Button(category);
            btn.setOnAction(e -> openDoctorsByCategory(category));
            btn.setMaxWidth(Double.MAX_VALUE);
            categoriesBox.getChildren().add(btn);
        }
    }

    private void openDoctorsByCategory(String category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/doctors_by_category.fxml"));
            Parent root = loader.load();

            DoctorsByCategoryController controller = loader.getController();
            controller.setCategory(category);

            Stage stage = (Stage) categoriesBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
