package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Customer;
import Jorbo.Util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {
    public Label customerOperationLabel;
    public TableView<Customer> customerTableView;
    public TableColumn<Customer, Integer> custIDColumn;
    public TableColumn<Customer, String> custNameColumn;
    public TableColumn<Customer, String> custCountryColumn;
    public TableColumn<Customer, String> custFirstDivColumn;
    public TableColumn<Customer, String> custAddressColumn;
    public TableColumn<Customer, String> custPhoneColumn;
    public TableColumn<Customer, String> custPostalColumn;
    public Button custExitButton;
    public Button custAddButton;
    public Button custDeleteButton;
    public Button custModifyButton;
    public Button custCancelButton;
    public Button custSaveEditsButton;
    public TextField custIdTextField;
    public TextField custNameTextField;
    public TextField custAddressTextField;
    public TextField custPostalTextField;
    public TextField custPhoneTextField;
    public ComboBox<String> custCountryComboBox;
    public ComboBox<String> custFirstDivComboBox;

    public void custExitButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 237, 292);
        Stage stage = (Stage) (custExitButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    public void custAddButtonPress(ActionEvent actionEvent) {
    }

    public void custDeleteButtonPress(ActionEvent actionEvent) {
    }

    public void custModifyButtonPress(ActionEvent actionEvent) {
    }

    public void custCancelButtonPress(ActionEvent actionEvent) {
    }

    public void custSaveEditsButtonPress(ActionEvent actionEvent) {
    }

    public void custCountryComboBoxHandler(ActionEvent actionEvent) {
    }

    public void custFirstDivComboBoxHandler(ActionEvent actionEvent) {
    }

    public void countryComboPop(){
        ObservableList<String> countryOL = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM countries");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String country = rs.getString("Country");
                countryOL.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        custCountryComboBox.setItems(countryOL);
    }

    public void firstDivComboPop(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
