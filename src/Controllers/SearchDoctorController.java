package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import DAOS.DoctorDAO;
import Class.Doctor;
import java.io.IOException;
import java.util.List;

public class SearchDoctorController {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private VBox doctorContainer;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(
                "სტომატოლოგი", "თერაპევტი", "ოფთალოლოგი", "ქირურგი", "ორტოფედი",
                "ფსიქოლოგი", "პედიატრი", "დერმატოლოგი", "უროლოგი",
                "ალერგოლოგი", "სამხედრო მედიცინა"
        );

        categoryComboBox.setValue(null);

        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null) {
                loadDoctorsByCategory(selectedCategory);
            }
        });
    }


    private void loadDoctorsByCategory(String category) {
        doctorContainer.getChildren().clear();

        List<Doctor> doctors = DoctorDAO.getDoctorsByCategory(category);

        for (Doctor doctor : doctors) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/doctor_card.fxml"));
                Pane doctorCard = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor); // გადაეცემა Doctor ობიექტი

                doctorContainer.getChildren().add(doctorCard);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
