package Jorbo.Controller;

import Jorbo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsAddController implements Initializable {
    public Label apptOperationLabel;
    public Button apptAddCancelButton;
    public Button apptAddSaveButton;
    public ComboBox apptAddCustomerCombo;
    public ComboBox apptAddTypeCombo;
    public ComboBox apptAddLocationCombo;
    public ComboBox apptAddContactCombo;
    public ComboBox apptAddStartTimeCombo;
    public ComboBox apptAddEndTimeCombo;
    public ComboBox apptAddUserCombo;
    public DatePicker apptAddDatePicker;
    public TextField apptAddIDField;
    public TextField apptAddTitleTextField;
    public TextField apptAddDescTextField;

    public void apptAddCancelButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 787, 486);
        Stage stage = (Stage) (apptAddCancelButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    public void apptAddSaveButtonPress(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (AppointmentsController.apptOperation){
            case 0 -> apptOperationLabel.setText("ADDING APPOINTMENT");
            case 1 -> apptOperationLabel.setText("UPDATING APPOINTMENT");
        }

    }
}
