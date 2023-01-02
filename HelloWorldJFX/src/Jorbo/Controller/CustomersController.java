package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Customer;
import Jorbo.Model.User;
import Jorbo.Util.Chrono;
import Jorbo.Util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides control for the customer screen
 */
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
    public ObservableList<Customer> customerOL = FXCollections.observableArrayList();

    /**
     * This method is called when the exit button is pressed, it returns to the main screen
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This method disabled all the text fields and combo boxes in the grid pane.
     */
    public void disableFields(){
        custIdTextField.setDisable(true);
        custNameTextField.setDisable(true);
        custAddressTextField.setDisable(true);
        custPhoneTextField.setDisable(true);
        custPostalTextField.setDisable(true);
        custCountryComboBox.setDisable(true);
        custFirstDivComboBox.setDisable(true);
    }

    /**
     * This method enables all the text fields and combo boxes in the grid pane.
     */
    public void enableFields(){
        custNameTextField.setDisable(false);
        custAddressTextField.setDisable(false);
        custPhoneTextField.setDisable(false);
        custPostalTextField.setDisable(false);
        custCountryComboBox.setDisable(false);
        custFirstDivComboBox.setDisable(false);
    }

    /**
     * This method clears all fields in the gridpane
     */
    public void clearFields(){
        custIdTextField.clear();
        custNameTextField.clear();
        custAddressTextField.clear();
        custPhoneTextField.clear();
        custPostalTextField.clear();
        custCountryComboBox.getSelectionModel().clearSelection();
        custFirstDivComboBox.getSelectionModel().clearSelection();
    }

    /**
     * This method is called when the add button is pressed, it
     * enables the text fields and sets the operation label accordingly
     * @param actionEvent
     */
    public void custAddButtonPress(ActionEvent actionEvent) {
        enableFields();
        customerOperationLabel.setText("Adding Customer");
        custCancelButton.setDisable(false);
        custSaveEditsButton.setDisable(false);
    }

    /**
     * This method is called when the delete button is pressed. It provides
     * an alert if there is no customer selected in the table. If there is
     * a customer selected, the customer is deleted using deleteCusty() and
     * an alert is provided.
     * @param actionEvent
     */
    public void custDeleteButtonPress(ActionEvent actionEvent) {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot delete without selected customer.");
            alert.setContentText("Select a customer to delete");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            String name = selectedCustomer.getCustomerName();
            deleteCusty(selectedCustomer);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer deleted");
            alert.setContentText("Customer " + name + " has been deleted.");
            Optional<ButtonType> result = alert.showAndWait();
            refreshCustyTable();
        }
    }

    /**
     * This method takes the name of a customer, deletes all of their appointments from the DB,
     * then deletes their record from the DB as well.
     * @param custy
     */
    public void deleteCusty(Customer custy){
        int id = custy.getCustomerID();
        try {
            PreparedStatement ps1 = JDBC.makeConnection().prepareStatement("DELETE from appointments WHERE" +
                    " Customer_ID = ?");
            ps1.setInt(1, id);
            ps1.executeUpdate();
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("DELETE FROM customers WHERE" +
                    " Customer_ID = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * This method is called when the modify button is pressed. If no customer is selected,
     * an alert is provided. If one is selected, the text fields are enabled and populated
     * with the selected customer's information.
     * @param actionEvent
     */
    public void custModifyButtonPress(ActionEvent actionEvent) {
        Customer selectedCusty = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCusty == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Customer must be selected to update");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            enableFields();
            customerOperationLabel.setText("Updating Customer");
            custNameTextField.setText(selectedCusty.getCustomerName());
            custAddressTextField.setText(selectedCusty.getAddress());
            custIdTextField.setText(selectedCusty.getCustomerID().toString());
            custPostalTextField.setText(selectedCusty.getPostalCode());
            custPhoneTextField.setText(selectedCusty.getPhone());
            custFirstDivComboBox.getSelectionModel().select(selectedCusty.getCity());
            custCountryComboBox.getSelectionModel().select(selectedCusty.getCountry());
            custCancelButton.setDisable(false);
            custSaveEditsButton.setDisable(false);
        }

    }

    /**
     * This method is called when the cancel button is pressed, it clears and disables all fields.
     * @param actionEvent
     */
    public void custCancelButtonPress(ActionEvent actionEvent) {
        clearFields();
        disableFields();
        customerOperationLabel.setText("");
    }

    /**
     * This method is called when the SaveEdits button is pressed. If a add operation
     * is occuring, it saves the record, clears and disables the fields, and refreshes
     * the table view.
     * If an update is occuring, the same thing happens except it calls updateCusty() instead of saveNew().
     * @param actionEvent
     */
    public void custSaveEditsButtonPress(ActionEvent actionEvent) {
        if (customerOperationLabel.getText().equals("Adding Customer")){
            if (validCustomer()) {
                saveNew();
                clearFields();
                disableFields();
                refreshCustyTable();
            }
        } else if (customerOperationLabel.getText().equals("Updating Customer")) {
            if (validCustomer()) {
                updateCusty();
                clearFields();
                disableFields();
                refreshCustyTable();
            }
        }
    }

    /**
     * This method checks the fields for valid information. If valid information is provided,
     * true is passed, otherwise false is passed and an error message is generated and sent.
     * @return
     */
    public boolean validCustomer(){
        String name = custNameTextField.getText();
        String address = custAddressTextField.getText();
        String phone = custPhoneTextField.getText();
        String postal = custPostalTextField.getText();
        String country = custCountryComboBox.getSelectionModel().getSelectedItem();
        String city = custFirstDivComboBox.getSelectionModel().getSelectedItem();
        String error = "";
        boolean k = false;
        if (name == null || (name.length() == 0)){
            error += "Name cannot be blank. \n";
        }
        if (address == null || address.length() == 0){
            error += "Address cannot be blank. \n";
        }
        if (phone == null || phone.length() == 0){
            error += "Phone cannot be blank. \n";
        } else if (phone.length() < 10 || phone.length() > 15){
            error += "Phone number must be between 10-15 characters. \n";
        }
        if (postal == null || postal.length() == 0){
            error += "Postal code cannot be blank. \n";
        } else if (postal.length() > 10 || postal.length() < 5) {
            error += "Postal code must be between 5-10 characters. \n";
        }
        if (country == null || country.length() == 0){
            error += "You must select a country. \n";
        }
        if (city == null || city.length() == 0){
            error += "You must select a first division.";
        }
        if (error.length() == 0){
            k = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not save customer");
            alert.setContentText(error);
            Optional<ButtonType> result = alert.showAndWait();
        }
        return k;
    }

    /**
     * This method converts a first division name into the corresponding Division_ID.
     * @param div division name
     * @return division id
     */
    public int divToID(String div){
        PreparedStatement ps = null;
        int id = 0;
        try {
            ps = JDBC.makeConnection().prepareStatement("SELECT Division_ID" +
                    " FROM first_level_divisions WHERE Division = ?");
            ps.setString(1, div);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                id = rs.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Problem with divToID SQL");
        }
        return id;
    }

    /**
     * This method parses information from the fields and enters it into the DB with
     * an  UPDATE statement.
     */
    public void updateCusty(){
        int id = Integer.parseInt(custIdTextField.getText());
        String name = custNameTextField.getText();
        String address = custAddressTextField.getText();
        String phone = custPhoneTextField.getText();
        String postal = custPostalTextField.getText();
        String city = custFirstDivComboBox.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("UPDATE customers SET" +
                    " Customer_Name = ?, Address = ?, Phone = ?, Postal_Code = ?, Division_ID = ?, Last_Update = ?, " +
                    "Last_Updated_By = ? WHERE Customer_ID = ?");
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);
            ps.setString(4, postal);
            ps.setInt(5, divToID(city));
            ps.setTimestamp(6, Chrono.getTimestamp());
            ps.setString(7, User.getUsername());
            ps.setInt(8, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Issue with update SQL");
            throwables.printStackTrace();
        }
    }

    /**
     * This method parses information from the fields and saves it to the DB
     * using an INSERT statement.
     */
    public void saveNew(){
        String name = custNameTextField.getText();
        String address = custAddressTextField.getText();
        String phone = custPhoneTextField.getText();
        String postal = custPostalTextField.getText();
        String city = custFirstDivComboBox.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("INSERT INTO customers (" +
                    "Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, phone);
            ps.setInt(9, divToID(city));
            ps.setTimestamp(5, Chrono.getTimestamp());
            ps.setString(6, User.getUsername());
            ps.setTimestamp(7, Chrono.getTimestamp());
            ps.setString(8, User.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String countryExtracted = "";
    public String divExtracted = "";

    /**
     * This method takes a division ID and returns the corresponding Division and Country name.
     * @param i division id
     */
    public void divIDExtractor(int i){
        int countryID = 0;
        PreparedStatement ps = null;
        try {
            ps = JDBC.makeConnection().prepareStatement("SELECT Country_ID, Division FROM first_level_divisions" +
                    " WHERE Division_ID = ?");
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                countryID = rs.getInt("Country_ID");
                divExtracted = rs.getString("Division");
            }
            PreparedStatement ps1 = JDBC.makeConnection().prepareStatement("SELECT Country FROM countries " +
                    "WHERE Country_ID = ?");
            ps1.setInt(1, countryID);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()){
                countryExtracted = rs1.getString("Country");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Problem with div ID extractor SQL");
        }
    }

    /**
     * This method clears the customer OL, adds to it every existing customer, then sets the OL
     * to the customer table.
     */
    public void refreshCustyTable(){
        customerOL.clear();
        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT * FROM customers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                int divID = rs.getInt("Division_ID");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                divIDExtractor(id);
                customerOL.add(new Customer(id, name, countryExtracted, divExtracted, address, phone, postal));
            }
        } catch (SQLException throwables) {
            System.out.println("Problem with refresh sql");
            throwables.printStackTrace();
        }
        customerTableView.setItems(customerOL);
        customerOperationLabel.setText("");
        disableFields();
        custCancelButton.setDisable(true);
        custSaveEditsButton.setDisable(true);
    }

    /**
     * This method is called when the country combo box is assigned a value.
     * It calls the method firstDivComboPop() which populates the first Div combo.
     * This ensures that the first div combo only shows first divisions that are
     * within the selected Country.
     * @param actionEvent
     */
    public void custCountryComboBoxHandler(ActionEvent actionEvent) {
        firstDivComboPop();
        custFirstDivComboBox.setPromptText("Make a selection...");
    }

    public void custFirstDivComboBoxHandler(ActionEvent actionEvent) {
    }

    /**
     * This method populates the country combo box with every country in the countries table.
     */
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
            System.out.println("Problem with country pop");
            throwables.printStackTrace();
        }
        custCountryComboBox.setItems(countryOL);
    }

    /**
     * This method converts a given country name to the corresponding country id.
     * @param name country name
     * @return country id
     */
    public int countryToID(String name){
        int id = 0;
        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Country_ID FROM" +
                    " countries WHERE Country = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                id = rs.getInt("Country_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    /**
     * This method populates the first division combo box with every first division from the first_level_divisions table.
     */
    public void firstDivComboPop(){
        ObservableList<String> firstDivOL = FXCollections.observableArrayList();
        String selectedCountry = custCountryComboBox.getSelectionModel().getSelectedItem();
        int countryID = countryToID(selectedCountry);
        if (selectedCountry != null){
            try {
                PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Division FROM first_level_divisions" +
                        " WHERE Country_ID = ?");
                ps.setInt(1, countryID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    firstDivOL.add(rs.getString("Division"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            firstDivOL.removeAll();
            firstDivOL.add("Select a country first...");
        }
        custFirstDivComboBox.setItems(firstDivOL);
    }

    /**
     * This is the initialize method for the screen, it calls all the methods to populate the combo boxes,
     * then creates and sets the property value factories and refreshes the table.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboPop();
        firstDivComboPop();
        disableFields();
        customerOperationLabel.setText("");

        PropertyValueFactory<Customer, Integer> custIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<Customer, String> custNameFactory = new PropertyValueFactory<>("CustomerName");
        PropertyValueFactory<Customer, String> custAddressFactory = new PropertyValueFactory<>("Address");
        PropertyValueFactory<Customer, String> customerPhoneFactory = new PropertyValueFactory<>("Phone");
        PropertyValueFactory<Customer, String> customerPostalFactory = new PropertyValueFactory<>("PostalCode");
        PropertyValueFactory<Customer ,String> customerCountryFactory = new PropertyValueFactory<>("Country");
        PropertyValueFactory<Customer, String> customerCityFactory = new PropertyValueFactory<>("City");
        custIDColumn.setCellValueFactory(custIDFactory);
        custNameColumn.setCellValueFactory(custNameFactory);
        custAddressColumn.setCellValueFactory(custAddressFactory);
        custPhoneColumn.setCellValueFactory(customerPhoneFactory);
        custPostalColumn.setCellValueFactory(customerPostalFactory);
        custCountryColumn.setCellValueFactory(customerCountryFactory);
        custFirstDivColumn.setCellValueFactory(customerCityFactory);

        custCancelButton.setDisable(true);
        custSaveEditsButton.setDisable(true);
        refreshCustyTable();
    }
}
