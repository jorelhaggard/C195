package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Appointment;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides control for the Appointments screen
 */
public class AppointmentsController implements Initializable {
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment, Integer> apptIDColumn;
    public TableColumn<Appointment, Integer> apptCustIdColumn;
    public TableColumn<Appointment, String> apptTitleColumn;
    public TableColumn<Appointment, String> apptTypeColumn;
    public TableColumn<Appointment, String> apptDescColumn;
    public TableColumn<Appointment, String> apptLocationColumn;
    public TableColumn<Appointment, String> apptContactColumn;
    public TableColumn<Appointment, String> apptStartColumn;
    public TableColumn<Appointment, Integer> apptUserColumn;
    public TableColumn<Appointment, String> apptEndColumn;
    public RadioButton weeklyRadioButton;
    public ToggleGroup apptsdisplay;
    public RadioButton monthlyRadioButton;
    public RadioButton allRadioButton;
    public Button apptExitButton;
    public Button apptDeleteButton;
    public Button apptModifyButton;
    public Button apptAddButton;
    public boolean weekly;
    public boolean monthly;
    public boolean all;
    public static Appointment updateAppointment;
    public static int updateAppointmentIndex;

    /**
     * This method is called when the weekly radio button is pressed,
     * it populates the table with weekly appointments by calling the
     * populateWeekly() method.
     * @param actionEvent
     * @throws SQLException
     */
    public void weeklyRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        populateWeekly();
        weekly = true;
        monthly = false;
        all = false;
    }

    /**
     * This method is called when the monthly radio button is pressed,
     * it populates the table with monthly appointments by calling the
     * populateMonthly() method.
     * @param actionEvent
     * @throws SQLException
     */
    public void monthlyRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        populateMonthly();
        weekly = false;
        monthly = true;
        all = false;
    }

    /**
     * This method is called when the all radio button is pressed,
     * it populates the table with all appointments by calling
     * createOL() directly and setting it to the appointments Table.
     * @param actionEvent
     * @throws SQLException
     */
    public void allRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> appointmentsOL = createOL();
        appointmentsTable.setItems(appointmentsOL);
        weekly = false;
        monthly = false;
        all = true;
    }

    /**
     * This method is used throughout this controller to create an OL
     * of ALL appointments.
     * @return OL of all appointments
     * @throws SQLException
     */
    public ObservableList<Appointment> createOL() throws SQLException {
        ObservableList<Appointment> appointmentsOL = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT * FROM appointments");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int id = rs.getInt("Appointment_ID");
            int custyid = rs.getInt("Customer_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String desc = rs.getString("Description");
            String loc = rs.getString("Location");
            String contact = AppointmentsAddController.contactIDToName(rs.getInt("Contact_ID"));
            Timestamp startTS = rs.getTimestamp("Start");
            Timestamp endTS = rs.getTimestamp("End");
            int user = rs.getInt("User_ID");
            String start = startTS.toString();
            String end = endTS.toString();
            appointmentsOL.add(new Appointment(id, custyid, title, type, desc, loc, contact, start, end, user));
        }
        return appointmentsOL;
    }

    /**
     * This method re populates the appointments table using the
     * variables set when the radio buttons are pressed.
     * This is used when returning from the add/modify screen, or after
     * deleting an appointment.
     * @throws Exception
     */
    public void refreshApptsTable() throws Exception{
        if (weekly){
            populateWeekly();
        } else if (monthly){
            populateMonthly();
        } else if (all){
            ObservableList<Appointment> appointmentsOL = createOL();
            appointmentsTable.setItems(appointmentsOL);
        }
    }
/**
 * This method uses createOL() to create an OL of all appointments,
 * then filters them into the monthlyOL, which is then set to the
 * appointments table.
 */
    public void populateMonthly() throws SQLException{
        ObservableList<Appointment> monthlyOL = FXCollections.observableArrayList();
        ObservableList<Appointment> appointmentsOL = createOL();
        for (Appointment a : appointmentsOL){
            String startString = a.getStartTime().substring(0,10) + 'T' + a.getStartTime().substring(11,19);
            LocalDateTime startLDT = LocalDateTime.parse(startString);
            String endString = a.getEndTime().substring(0,10) + 'T' + a.getEndTime().substring(11,19);
            LocalDateTime endLDT = LocalDateTime.parse(endString);
            if (startLDT.isBefore(LocalDateTime.now().plusMonths(1))){
                monthlyOL.add(a);
            }
        }
        appointmentsTable.setItems(monthlyOL);
    }

    /**
     * This appointments uses createOL() to create an OL of
     * all appointmemts, then filters them into the weeklyOL
     * and sets the OL to the appointments table.
     * @throws SQLException
     */
    public void populateWeekly() throws SQLException {
        ObservableList<Appointment> weeklyOL = FXCollections.observableArrayList();
        ObservableList<Appointment> appointmentsOL = createOL();
        for (Appointment a : appointmentsOL){
            String startString = a.getStartTime().substring(0,10) + 'T' + a.getStartTime().substring(11,19);
            LocalDateTime startLDT = LocalDateTime.parse(startString);
            String endString = a.getEndTime().substring(0,10) + 'T' + a.getEndTime().substring(11,19);
            LocalDateTime endLDT = LocalDateTime.parse(endString);
            if (startLDT.isBefore(LocalDateTime.now().plusWeeks(1))){
                weeklyOL.add(a);
            }
        }
        appointmentsTable.setItems(weeklyOL);
    }

    /**
     * This method returns to the main screen.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This method deletes a selected appointment, then refreshes the table.
     * If no appointment is selected an error message is displayed.
     * @param actionEvent
     * @throws Exception
     */
    public void apptDeleteButtonPress(ActionEvent actionEvent) throws Exception {
        updateAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (updateAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment selected");
            alert.setContentText("Select an appointment to delete it");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
        int id = updateAppointment.getAppointmentID();
        String start = updateAppointment.getStartTime();
        String type = updateAppointment.getType();
        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("DELETE FROM appointments WHERE " +
                    "Appointment_ID = ?");
            ps.setInt(1, id);
            int rs = ps.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment " + id + " deleted.");
            alert.setHeaderText("Delete successful");
            alert.setContentText("The " + type + " appointment at " + start + " has been deleted.");
            Optional<ButtonType> result = alert.showAndWait();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
        refreshApptsTable();
    }

    /**
     * This method is called when the modify button is pressed. If no
     * appointment is selected, an error message is displayed, otherwise
     * the appointments add screen is brought up and the selected
     * appointment information is populated into its fields.
     * @param actionEvent
     * @throws IOException
     */
    public void apptModifyButtonPress(ActionEvent actionEvent) throws IOException {
        apptOperation = 1;
        updateAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (updateAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No appointment selected");
            alert.setContentText("You must select an appointment to update");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AppointmentsAddScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 396, 510);
            Stage stage = (Stage) (apptModifyButton.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }
    public static int apptOperation;

    /**
     * This method pulls up a blank instance of the add appointments screen.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This is the initialize method for the screen. It creates property value factories
     * for each column and then sets them to the columns. Then, the weekly radio
     * button method is called to set the appointments table to a weekly view by default.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PropertyValueFactory<Appointment, Integer> apptIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, Integer> custyIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<Appointment, String> titleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, String> typeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, String> descFactory = new PropertyValueFactory<>("Description");
        PropertyValueFactory<Appointment, String> locFactory = new PropertyValueFactory<>("Location");
        PropertyValueFactory<Appointment, String> contactFactory = new PropertyValueFactory<>("Contact");
        PropertyValueFactory<Appointment, String> startFactory = new PropertyValueFactory<>("StartTime");
        PropertyValueFactory<Appointment, String> endFactory = new PropertyValueFactory<>("EndTime");
        PropertyValueFactory<Appointment, Integer> userFactory = new PropertyValueFactory<>("UserID");

        apptIDColumn.setCellValueFactory(apptIDFactory);
        apptCustIdColumn.setCellValueFactory(custyIDFactory);
        apptTitleColumn.setCellValueFactory(titleFactory);
        apptTypeColumn.setCellValueFactory(typeFactory);
        apptDescColumn.setCellValueFactory(descFactory);
        apptLocationColumn.setCellValueFactory(locFactory);
        apptContactColumn.setCellValueFactory(contactFactory);
        apptStartColumn.setCellValueFactory(startFactory);
        apptEndColumn.setCellValueFactory(endFactory);
        apptUserColumn.setCellValueFactory(userFactory);


        ActionEvent press = new ActionEvent();
        try {
            weeklyRadioButtonPress(press);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
