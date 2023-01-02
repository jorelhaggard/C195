package Jorbo.Controller;

import Jorbo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class provides control for the main screen of the application.
 */
public class MainController {
    public Button mainExitButton;
    public Button mainCustomerButton;
    public Button mainAppointmentsButton;
    public Button mainReportsButton;

    /**
     * This method is called when the exit button is pressed. It returns to the login screen.
     * @param actionEvent
     * @throws IOException
     */
    public void mainExitButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 285, 299);
        Stage stage = (Stage) (mainExitButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    /**
     * This method is called when the customer button is pressed, it pulls up the customers screen.
     * @param actionEvent
     * @throws IOException
     */
    public void mainCustomerButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/CustomersScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 419);
        Stage stage = (Stage) (mainCustomerButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    /**
     * This function is called when the appointments button is pressed. It pulls up the
     * appointments screen.
     * @param actionEvent
     * @throws IOException
     */
    public void mainAppointmentsButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 872, 486);
        Stage stage = (Stage) (mainAppointmentsButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    /**
     * This method is called when the reports button is pressed. It pulls up the reports screen.
     * @param actionEvent
     * @throws IOException
     */
    public void mainReportsButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/ReportsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) (mainReportsButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }
}
