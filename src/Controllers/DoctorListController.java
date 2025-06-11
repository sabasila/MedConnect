package Controllers;

import DAOS.DoctorDAO;
import Class.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DoctorListController {

    @FXML
    private VBox doctorListContainer;

    private DoctorDAO doctorDAO = new DoctorDAO();
    private List<Doctor> allDoctors;

    @FXML
    public void initialize() {
        allDoctors = doctorDAO.getTopRatedDoctors();  // ან doctorDAO.getAllDoctors() თუ გაქვს
        showDoctors(allDoctors);
    }

    public void showDoctors(List<Doctor> doctors) {
        doctorListContainer.getChildren().clear();

        for (Doctor doctor : doctors) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DoctorCard.fxml"));
                Node cardNode = loader.load();

                DoctorCardController controller = loader.getController();
                controller.setDoctorData(doctor);
                controller.setProfileButtonAction(() -> {
                    // აქ დაამატე ლოგიკა პროფილის ღილაკისთვის
                    System.out.println("პროფილი: " + doctor.getFullName());
                });

                doctorListContainer.getChildren().add(cardNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 🔍 ფილტრაცია - მაგალითი: რეიტინგი > 4
    public void filterDoctors(Predicate<Doctor> filter) {
        List<Doctor> filtered = allDoctors.stream()
                .filter(filter)
                .collect(Collectors.toList());
        showDoctors(filtered);
    }

    // მაგალითად რომ გამოიყენო:
    public void showHighRatedDoctors() {
        filterDoctors(d -> d.getAverageRating() > 4.0);
    }

    public void filterByName(String namePart) {
        filterDoctors(d -> d.getFullName().toLowerCase().contains(namePart.toLowerCase()));
    }
}
