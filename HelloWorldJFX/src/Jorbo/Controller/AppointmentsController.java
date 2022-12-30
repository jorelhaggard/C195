package Jorbo.Controller;

import Jorbo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentsController {
    public TableView appointmentsTable;
    public TableColumn apptIDColumn;
    public TableColumn apptCustIdColumn;
    public TableColumn apptTitleColumn;
    public TableColumn apptTypeColumn;
    public TableColumn apptDescColumn;
    public TableColumn apptLocationColumn;
    public TableColumn apptContactColumn;
    public TableColumn apptStartColumn;
    public TableColumn apptUserIdColumn;
    public TableColumn apptEndColumn;
    public RadioButton weeklyRadioButton;
    public ToggleGroup apptsdisplay;
    public RadioButton monthlyRadioButton;
    public RadioButton allRadioButton;
    public Button apptExitButton;
    public Button apptDeleteButton;
    public Button apptModifyButton;
    public Button apptAddButton;

    public void weeklyRadioButtonPress(ActionEvent actionEvent) {
    }

    public void monthlyRadioButtonPress(ActionEvent actionEvent) {
    }

    public void allRadioButtonPress(ActionEvent actionEvent) {
    }

    public void apptExitButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 237, 292);
        Stage stage = (Stage) (apptExitButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    public void apptDeleteButtonPress(ActionEvent actionEvent) {
    }

    public void apptModifyButtonPress(ActionEvent actionEvent) throws IOException {
        apptOperation = 1;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AddAppointmentScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 396, 510);
        Stage stage = (Stage) (apptModifyButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    public static int apptOperation;

    public void apptAddButtonPress(ActionEvent actionEvent) throws IOException {
        apptOperation = 0;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsAddScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 396, 510);
        Stage stage = (Stage) (apptAddButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }
}
