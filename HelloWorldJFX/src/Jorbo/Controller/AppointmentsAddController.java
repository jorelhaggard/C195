package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Appointment;
import Jorbo.Model.Customer;
import Jorbo.Model.User;
import Jorbo.Util.Chrono;
import Jorbo.Util.JDBC;
import com.sun.javafx.collections.ElementObservableListDecorator;
import com.sun.webkit.LoadListenerClient;
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
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller for the appointments add/modify screen.
 *
 * A LAMBDA EXPRESSION CAN BE FOUND IN THE OVERRIDE METHOD
 */
public class AppointmentsAddController implements Initializable {
    public Label apptOperationLabel;
    public Button apptAddCancelButton;
    public Button apptAddSaveButton;
    public ComboBox<String> apptAddCustomerCombo;
    public ComboBox<String> apptAddTypeCombo;
    public ComboBox<String> apptAddLocationCombo;
    public ComboBox<String> apptAddContactCombo;
    public ComboBox<String> apptAddStartTimeCombo;
    public ComboBox<String> apptAddEndTimeCombo;
    public ComboBox<String> apptAddUserCombo;
    public DatePicker apptAddDatePicker;
    public TextField apptAddIDField;
    public TextField apptAddTitleTextField;
    public TextField apptAddDescTextField;

    /**
     * This method populates the Start and End time combo boxes with 15 minute increments from 8am EST to 10pm EST.
     * It displays the times in the default zone of the user's system.
     */
    public void timeComboPop(){
        ObservableList<String> startOL = FXCollections.observableArrayList();
        ObservableList<String> endOL = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        ZonedDateTime startZDT = ZonedDateTime.of(2022, 01, 01, 8, 0, 0, 0, ZoneId.of("America/New_York"));
        ZonedDateTime endZDT = ZonedDateTime.of(2022, 01, 01, 22, 15, 0, 0, ZoneId.of("America/New_York"));
        ZonedDateTime start = startZDT;
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
        while (start.isBefore(endZDT)){
            String time = tf.format(start);
            startOL.add(time);
            endOL.add(time);
            start = start.plusMinutes(15);
        }
        startOL.remove(56);
        apptAddStartTimeCombo.setItems(startOL);
        endOL.remove(0);
        apptAddEndTimeCombo.setItems(endOL);
    }

    /**
     * This method populates the locations combo box.
     */
    public void locationPop(){
        ObservableList<String> locationOL = FXCollections.observableArrayList();
        locationOL.setAll("Phoenix", "White Plains", "Montreal", "London");
        apptAddLocationCombo.setItems(locationOL);
    }

    ObservableList<String> customerOL = FXCollections.observableArrayList();

    /**
     * This method populates the customer combo box with the names of all customers from the customers table.
     * @throws SQLException
     */
    public void customerPop() throws SQLException {
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Customer_Name FROM customers");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            customerOL.add(rs.getString("Customer_Name"));
        }
        apptAddCustomerCombo.setItems(customerOL);
    }

    /**
     * This method populates the types combo box.
     */
    public void typePop(){
        ObservableList<String> typeOL = FXCollections.observableArrayList();
        typeOL.setAll("New Customer", "General Review", "Annual Review", "Close Account");
        apptAddTypeCombo.setItems(typeOL);
    }
    ObservableList<String> userOL = FXCollections.observableArrayList();

    /**
     * This method populates the user combo box with all users from the users table.
     * @throws SQLException
     */
    public void userPop() throws SQLException {
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT User_Name FROM users");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            userOL.add(rs.getString("User_Name"));
        }
        apptAddUserCombo.setItems(userOL);
    }

    ObservableList<String> contactOL = FXCollections.observableArrayList();

    /**
     * This method populates the contact combo box with all contacts from the contacts table.
     * @throws SQLException
     */
    public void contactPop() throws SQLException {
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Contact_Name FROM contacts");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            contactOL.add(rs.getString("Contact_Name"));
        }
        apptAddContactCombo.setItems(contactOL);
    }

    /**
     * This method takes a contact name and returns the associated ID.
     * @param name contact name
     * @return contact id
     * @throws SQLException
     */
    public static int contactNameToID(String name) throws SQLException {
        int ID = 0;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            ID = rs.getInt("Contact_ID");
        }
        return ID;
    }

    /**
     * This method takes a contact id and returns the associated name.
     * @param ID contact id
     * @return contact name
     * @throws SQLException
     */
    public static String contactIDToName(int ID) throws SQLException {
        String name = "";
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = ?");
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            name = rs.getString("Contact_Name");
        }
        return name;
    }

    /**
     * This method returns to the appointments screen, it is called when the cancel button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void apptAddCancelButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 872, 486);
        Stage stage = (Stage) (apptAddCancelButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
    }

    /**
     * This method clears all fields on the screen.
     */
    public void clearFields(){
        apptAddDatePicker.setValue(null);
        apptAddEndTimeCombo.getSelectionModel().clearSelection();
        apptAddStartTimeCombo.getSelectionModel().clearSelection();
        apptAddContactCombo.getSelectionModel().clearSelection();
        apptAddLocationCombo.getSelectionModel().clearSelection();
        apptAddTypeCombo.getSelectionModel().clearSelection();
        apptAddCustomerCombo.getSelectionModel().clearSelection();
        apptAddUserCombo.getSelectionModel().clearSelection();
        apptAddDescTextField.clear();
        apptAddTitleTextField.clear();
        apptAddIDField.clear();
    }

    /**
     * This method used validAppointment() to check if the supplied information is valid. If it's not,
     * validAppointment() creates an error message. If the info is valid, then the information is parsed from
     * the fields and added to an UPDATE sql statement.
     * @throws Exception
     */
    public void updateAppointment() throws Exception {
        if (validAppointment()){
            int apptid = Integer.parseInt(apptAddIDField.getText());
            String custy = apptAddCustomerCombo.getValue();
            String title = apptAddTitleTextField.getText();
            String type = apptAddTypeCombo.getValue();
            String desc = apptAddDescTextField.getText();
            String location = apptAddLocationCombo.getValue();
            String contact = apptAddContactCombo.getValue();
            String start = apptAddStartTimeCombo.getValue();
            String end = apptAddEndTimeCombo.getValue();
            String user = apptAddUserCombo.getValue();
            LocalTime startLT = LocalTime.parse(start);
            LocalTime endLT = LocalTime.parse(end);
            LocalDate date = apptAddDatePicker.getValue();
            LocalDateTime startLDT = LocalDateTime.of(date, startLT);
            LocalDateTime endLDT = LocalDateTime.of(date, endLT);
            Timestamp startTS = Timestamp.valueOf(startLDT);
            Timestamp endTS = Timestamp.valueOf(endLDT);
            try {
                PreparedStatement ps = JDBC.makeConnection().prepareStatement("UPDATE appointments SET " +
                        "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                        " Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?" +
                        " WHERE Appointment_ID = ?");
                ps.setString(1, title);
                ps.setString(2, desc);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, startTS);
                ps.setTimestamp(6, endTS);
                ps.setTimestamp(7, Chrono.getTimestamp());
                ps.setString(8, User.getUsername());
                ps.setInt(9, Customer.custNameToID(custy));
                ps.setInt(10, User.userNameToID(user));
                ps.setInt(11, contactNameToID(contact));
                ps.setInt(12, apptid);
                int rs = ps.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Problem with update sql");
            }
        }
    }

    /**
     * This method parses all information from the fields and enters it into the database
     * with an INSERT statement.
     */
    public void saveAppointment(){
        String custy = apptAddCustomerCombo.getValue();
        String title = apptAddTitleTextField.getText();
        String type = apptAddTypeCombo.getValue();
        String desc = apptAddDescTextField.getText();
        String location = apptAddLocationCombo.getValue();
        String contact = apptAddContactCombo.getValue();
        String start = apptAddStartTimeCombo.getValue();
        String end = apptAddEndTimeCombo.getValue();
        String user = apptAddUserCombo.getValue();
        LocalTime startLT = LocalTime.parse(start);
        LocalTime endLT = LocalTime.parse(end);
        LocalDate date = apptAddDatePicker.getValue();
        LocalDateTime startLDT = LocalDateTime.of(date, startLT);
        LocalDateTime endLDT = LocalDateTime.of(date, endLT);
        Timestamp startTS = Timestamp.valueOf(startLDT);
        Timestamp endTS = Timestamp.valueOf(endLDT);
            try {
                PreparedStatement ps = JDBC.makeConnection().prepareStatement("INSERT INTO appointments (" +
                        "Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                        " Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, title);
                ps.setString(2, desc);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, startTS);
                ps.setTimestamp(6, endTS);
                ps.setTimestamp(7, Chrono.getTimestamp());
                ps.setString(8, User.getUsername());
                ps.setTimestamp(9, Chrono.getTimestamp());
                ps.setString(10, User.getUsername());
                ps.setInt(11, Customer.custNameToID(custy));
                ps.setInt(12, User.userNameToID(user));
                ps.setInt(13, contactNameToID(contact));
                int rs = ps.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Problem with save sql");
            }
        }

    /**
     * This method is called when the save button is pressed, if it is an update operation it checks for
     * validity then calls updateAppointment(). If it is an add operation, it checks for validity then calls saveAppointment().
     * The text fields are then cleared and the Appointments screen is called.
     * @param actionEvent
     * @throws Exception
     */
    public void apptAddSaveButtonPress(ActionEvent actionEvent) throws Exception {
        if (apptOperationLabel.getText().equals("ADDING APPOINTMENT")){
            if (validAppointment()) {
                boolean k = validAppointment();
                saveAppointment();
                System.out.println("Attempting to save appt");
                clearFields();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 872, 486);
                Stage stage = (Stage) (apptAddSaveButton.getScene().getWindow());
                stage.setScene(scene);
                stage.show();
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
                stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
         }
        }
        if (apptOperationLabel.getText().equals("UPDATING APPOINTMENT")){
            if (validAppointment()){
            boolean k = validAppointment();
            updateAppointment();
            clearFields();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 872, 486);
                Stage stage = (Stage) (apptAddCancelButton.getScene().getWindow());
                stage.setScene(scene);
                stage.show();
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
                stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
        }
    }
    }
    /**
     * this is the override method for the class, it sets the appointment operation
     * label, and populates the fields and combo boxes if it's an update operation.
     *
     * There is also a Lambda expression here, it is passed as an argument to the
     * setDayCellFactory method of the my datePicker. The updateItem method of the
     * DateCell is called for each day in the datePicker, setting anything in the
     * past to disabled using the setDisable method.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        switch (AppointmentsController.apptOperation){
            case 0 -> apptOperationLabel.setText("ADDING APPOINTMENT");
            case 1 -> apptOperationLabel.setText("UPDATING APPOINTMENT");
        }
                   //LAMBDA EXPRESSION IS HERE///////////
        apptAddDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override ////////////////////////////////
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                 //////////////////////////////////
                setDisable(item.isBefore(LocalDate.now()));
            } ///////////////////////////////////
        }); ////////////////////////////////////
        if (AppointmentsController.apptOperation == 1){
            Appointment appt = AppointmentsController.updateAppointment;
            int id = appt.getAppointmentID();
            String custy = null;
            try {
                custy = Customer.custIDToName(appt.getCustomerID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String title = appt.getTitle();
            String type = appt.getType();
            String desc = appt.getDescription();
            String loc = appt.getLocation();
            String user = null;
            try {
                user = User.userIDToName(appt.getUserID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String contact = appt.getContact();
            String start = appt.getStartTime().substring(11, 19);
            String end = appt.getEndTime().substring(11, 19);
            String date = appt.getStartTime().substring(0, 10);
            LocalDate dateLD = LocalDate.parse(date);
            apptAddIDField.setText(Integer.toString(id));
            apptAddIDField.setDisable(true);
            apptAddCustomerCombo.setValue(custy);
            apptAddTitleTextField.setText(title);
            apptAddTypeCombo.setValue(type);
            apptAddDescTextField.setText(desc);
            apptAddLocationCombo.setValue(loc);
            apptAddContactCombo.setValue(contact);
            apptAddStartTimeCombo.setValue(start);
            apptAddEndTimeCombo.setValue(end);
            apptAddDatePicker.setValue(dateLD);
            apptAddUserCombo.setValue(user);
        }
        try {
            customerPop();
            userPop();
            contactPop();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        locationPop();
        timeComboPop();
        typePop();
    }

    DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * This method checks the information in the fields for validity. A number of checks are run: for
     * empty fields,  invalid start and end times, and a separate method conflictingCheck() checks for
     * overlapping times with existing appointments. If everything is valid, true is returned.
     * @return bool;
     * @throws Exception
     */
    public boolean validAppointment() throws Exception {
        String custy = apptAddCustomerCombo.getValue();
        String title = apptAddTitleTextField.getText();
        String type = apptAddTypeCombo.getValue();
        String desc = apptAddDescTextField.getText();
        String location = apptAddLocationCombo.getValue();
        String contact = apptAddContactCombo.getValue();
        String start = apptAddStartTimeCombo.getValue();
        String end = apptAddEndTimeCombo.getValue();
        LocalDate date = LocalDate.now();
        LocalTime startLT;
        LocalTime endLT;
        LocalDateTime startLDT;
        LocalDateTime endLDT;
        try {
            date = apptAddDatePicker.getValue();
            startLT = LocalTime.parse(start);
            endLT = LocalTime.parse(end);
            startLDT = LocalDateTime.of(date, startLT);
            endLDT = LocalDateTime.of(date, endLT); } catch (Exception e) {
            if (custy != null && title != null && type != null && desc != null && location != null && contact != null){
            if (start == null && end == null && date == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Start, end, and date fields cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
                return false;}else if (start != null && end != null && date == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Date field cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            } else if (start != null && end == null && date != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("End field cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            } else if (start == null && end != null && date != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Start field cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            } else if (start == null && end != null && date == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Start and date fields cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            } else if (start != null && end == null && date == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("End and date fields cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            } else if (start == null && end == null && date != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Start and end fields cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            }} else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Fields cannot be blank");
                Optional<ButtonType> result = alert.showAndWait();
            }

            return false;}
            String error = "";
            if (custy == null || custy.length() == 0) {
                error += "Select a customer name. \n";
            }
            if (title == null || title.length() == 0) {
                error += "Enter a title. \n";
            }
            if (type == null || type.length() == 0) {
                error += "Select a type. \n";
            }
            if (desc == null || desc.length() == 0) {
                error += "Enter a description. \n";
            }
            if (location == null || location.length() == 0) {
                error += "Select a location. \n";
            }
            if (contact == null || contact.length() == 0) {
                error += "Select a contact. \n";
            }
            if (start == null) {
                error += "Select a start time. \n";
            }
            if (end == null) {
                error += "Select an end time. \n";
            } else if (endLT.isBefore(startLT) || endLT.equals(startLT)) {
                error += "End time must be after start time. \n";
            } else {
                try {
                    if (conflictingCheck(startLDT, endLDT)) {
                        error += "These times conflict with an existing appointment for this user!";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Problem with conflict check");
                }
            }
            if (error.length() == 0) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText(error);
                Optional<ButtonType> result = alert.showAndWait();
                return false;

            }
        }

    /**
     * This method takes two new times and ensures that they do not conflict in any way
     * with all the existing appointment times in the database. false is returned if there are no conflicts.
     * @param start new start
     * @param end new end
     * @return bool
     * @throws SQLException
     */
    public boolean conflictingCheck(LocalDateTime start, LocalDateTime end) throws SQLException {
        if (apptOperationLabel.getText().equals("UPDATING APPOINTMENT")) {
            int id = Integer.parseInt(apptAddIDField.getText());
            int userid = User.userNameToID(apptAddUserCombo.getValue());
            try {
                PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                        "SELECT * FROM appointments " +
                        "WHERE ((Start BETWEEN ? AND ?)" +
                        " OR (End BETWEEN ? AND ?)" +
                        " OR (? BETWEEN Start AND End)" +
                        " OR (? BETWEEN Start AND End)) AND (Appointment_ID != ? AND User_ID = ?)");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setTimestamp(3, Timestamp.valueOf(start));
                ps.setTimestamp(4, Timestamp.valueOf(end));
                ps.setTimestamp(5, Timestamp.valueOf(start));
                ps.setTimestamp(6, Timestamp.valueOf(end));
                ps.setInt(7, id);
                ps.setInt(8, userid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    return true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (apptOperationLabel.getText().equals("ADDING APPOINTMENT")){
            int userid = User.userNameToID(apptAddUserCombo.getValue());
            try {
                PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                        "SELECT * FROM appointments " +
                        "WHERE ((Start BETWEEN ? AND ?)" +
                        " OR (End BETWEEN ? AND ?)" +
                        " OR (? BETWEEN Start AND End)" +
                        " OR (? BETWEEN Start AND End)) AND (User_ID = ?)");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setTimestamp(3, Timestamp.valueOf(start));
                ps.setTimestamp(4, Timestamp.valueOf(end));
                ps.setTimestamp(5, Timestamp.valueOf(start));
                ps.setTimestamp(6, Timestamp.valueOf(end));
                ps.setInt(7, userid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    return true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
