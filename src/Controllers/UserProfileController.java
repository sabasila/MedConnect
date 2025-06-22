package Controllers;

import Class.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserProfileController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPersonalId;

    @FXML
    private Label lblUserType;

    public void setUser(User user) {
        lblName.setText("სახელი: " + user.getFullName());
        lblPersonalId.setText("პირადი ნომერი: " + user.getPersonalId());
        lblUserType.setText("მომხმარებლის ტიპი: " + (user.getUserType().equals("doctor") ? "ექიმი" : "პაციენტი"));
    }
}
