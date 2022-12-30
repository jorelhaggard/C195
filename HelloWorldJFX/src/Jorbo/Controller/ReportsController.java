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

public class ReportsController {
    public TableColumn typesMonthColumn;
    public TableColumn typesAnnRevColumn;
    public TableColumn typesNewCustyColumn;
    public TableColumn typesGenRevColumn;
    public TableColumn typesCloseAccountColumb;
    public TableColumn scheduleApptIDColumn;
    public TableColumn scheduleTypeColumn;
    public TableColumn scheduleDescColumn;
    public TableColumn scheduleStartColumn;
    public TableColumn scheduleEndColumn;
    public TableColumn scheduleTitleColumn;
    public TableColumn scheduleCustomerIDColumn;
    public RadioButton anikaRadioButton;
    public ToggleGroup scheduleTG;
    public RadioButton liRadioButton;
    public TableColumn userUserColumn;
    public TableColumn userLastUpdateColumn;
    public TableColumn userTotalCreateColumn;
    public Button reportsExitButton;
    public Tab userTable;
    public RadioButton danielRadioButton;
    public Tab schedulesTable;
    public TableView typesTable;

    public void danielRadioButtonPress(ActionEvent actionEvent) {
    }

    public void liRadioButtonPress(ActionEvent actionEvent) {
    }

    public void anikaRadioButtonPress(ActionEvent actionEvent) {
    }

    public void reportsExitButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 285, 299);
        Stage stage = (Stage) (reportsExitButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }
}
