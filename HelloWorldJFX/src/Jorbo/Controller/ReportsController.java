package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Appointment;
import Jorbo.Model.Report;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


/**
 * This is the controller for the Reports screen.
 *
 * A LAMBDA EXPRESSION CAN BE FOUND IN THE monthGenerator() METHOD.
 */
public class ReportsController implements Initializable {
    public TableColumn<Report, String> typesMonthColumn;
    public TableColumn<Report, Integer> typesAnnRevColumn;
    public TableColumn<Report, Integer> typesNewCustyColumn;
    public TableColumn<Report, Integer> typesGenRevColumn;
    public TableColumn<Report, Integer> typesCloseAccountColumb;
    public TableColumn<Appointment, Integer> scheduleApptIDColumn;
    public TableColumn<Appointment, String> scheduleTypeColumn;
    public TableColumn<Appointment, String> scheduleDescColumn;
    public TableColumn<Appointment, String> scheduleStartColumn;
    public TableColumn<Appointment, String> scheduleEndColumn;
    public TableColumn<Appointment, String> scheduleTitleColumn;
    public TableColumn<Appointment, Integer> scheduleCustomerIDColumn;
    public RadioButton anikaRadioButton;
    public ToggleGroup scheduleTG;
    public RadioButton liRadioButton;
    public TableColumn<Report, String> userUserColumn;
    public TableColumn<Report, String> userLastUpdateColumn;
    public TableColumn<Report, Integer> userTotalCreateColumn;
    public Button reportsExitButton;
    public TableView<Report> userTable;
    public RadioButton danielRadioButton;
    public TableView<Appointment> schedulesTable;
    public TableView<Report> typesTable;
    public ObservableList<Report> userReportOL = FXCollections.observableArrayList();
    public ObservableList<Report> typesReportOL = FXCollections.observableArrayList();
    public ObservableList<String> typesOL = FXCollections.observableArrayList();
    public ObservableList<Appointment> scheduleReportOL = FXCollections.observableArrayList();
    public int[][] typesArray = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    /**
     * This method is called when daniel's radio button is pressed. it calls populateSchedule with the string "Daniel Garcia"
     * @param actionEvent
     * @throws SQLException
     */
    public void danielRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        populateSchedule("Daniel Garcia");
    }

    /**
     * This method is called when Li's radio button is pressed. It calls populateSchedule with string "Li Lee"
     * @param actionEvent
     * @throws SQLException
     */
    public void liRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        populateSchedule("Li Lee");
    }

    /**
     * This method is called when Anika's radio button is pressed. It calls populateSchedule with string "Anika Costa".
     * @param actionEvent
     * @throws SQLException
     */
    public void anikaRadioButtonPress(ActionEvent actionEvent) throws SQLException {
        populateSchedule("Anika Costa");
    }

    /**
     * This method returns to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void reportsExitButtonPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 237, 292);
        Stage stage = (Stage) (reportsExitButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

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
     * This method takes a string and uses it in an sql query to find all appointments for a given contact
     * @param contact
     * @throws SQLException
     */
    public void populateSchedule(String contact) throws SQLException {
        ObservableList<Appointment> contactOL = FXCollections.observableArrayList();
        contactOL.clear();
        int contactId = contactNameToID(contact);
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT * FROM appointments" +
                " WHERE Contact_ID = ?");
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int apptid = rs.getInt("Appointment_ID");
            String type = rs.getString("Type");
            String desc = rs.getString("Description");
            Timestamp startTS = rs.getTimestamp("Start");
            Timestamp endTS = rs.getTimestamp("End");
            String start = startTS.toString();
            String end = endTS.toString();
            String title = rs.getString("Title");
            int custid = rs.getInt("Customer_ID");
            contactOL.add(new Appointment(apptid, type, desc, start, end, title, custid));
            schedulesTable.setItems(contactOL);}
            if (contactOL.isEmpty()){
                schedulesTable.setItems(contactOL);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No appointments found for this contact!");
                alert.setContentText("No appointments found.");
                Optional<ButtonType> result = alert.showAndWait();
                }
            }

    /**
     * This method populates the user activity report. It creates two Report objects, one for
     * test and one for admin, then sets them to the user activity table.
     */
    public void populateUserActivity() {
        String testLast = "";
        String adminLast = "";
        String userTest = "test";
        String userAdmin = "admin";
        int testTotal = 0;
        int adminTotal = 0;

        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                    "SELECT MAX(Last_Update) " +
                    "FROM (SELECT Last_Update FROM customers WHERE Last_Updated_By = ? " +
                    "UNION ALL SELECT Last_Update FROM appointments WHERE Last_Updated_By = ?) t;");
            ps.setString(1, "test");
            ps.setString(2, "test");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getTimestamp("MAX(Last_Update)") != null) {
                    testLast = rs.getTimestamp("MAX(Last_Update)").toString();
                } else {
                    testLast = "No applicable records in database.";
                }
            }
            PreparedStatement ps1 = JDBC.makeConnection().prepareStatement("" +
                    "SELECT MAX(Last_Update) " +
                    "FROM (SELECT Last_Update FROM customers WHERE Last_Updated_By = ? " +
                    "UNION ALL SELECT Last_Update FROM appointments WHERE Last_Updated_By = ?) t;");
            ps1.setString(1, "admin");
            ps1.setString(2, "admin");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                if (rs1.getTimestamp("MAX(Last_Update)") != null) {
                    adminLast = rs1.getTimestamp("MAX(Last_Update)").toString();
                } else {
                    adminLast = "No applicable records in database.";
                }
            }

            PreparedStatement ps2 = JDBC.makeConnection().prepareStatement("SELECT * FROM " +
                    "(SELECT Created_By FROM appointments WHERE Created_By = ? " +
                    "UNION ALL SELECT Created_By FROM customers WHERE Created_By = ?) t;");
            ps2.setString(1, "test");
            ps2.setString(2, "test");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                testTotal++;
            }
            PreparedStatement ps3 = JDBC.makeConnection().prepareStatement("SELECT * FROM " +
                    "(SELECT Created_By FROM appointments WHERE Created_By = ? " +
                    "UNION ALL SELECT Created_By FROM customers WHERE Created_By = ?) t;");
            ps3.setString(1, "admin");
            ps3.setString(2, "admin");

            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                adminTotal++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userReportOL.add(new Report(userTest, testLast, testTotal));
        userReportOL.add(new Report(userAdmin, adminLast, adminTotal));
        userTable.setItems(userReportOL);
    }

    /**
     * This method populates the types by month table. It first queries all appointments, then goes through the
     * result set, checks the month and type, and iterates the corresponding cell of the typesArray. Then,
     * in the for loop, 12 report objects are created, one with each row of the typesArray, and the objects are set to the
     * types table.
     * @throws SQLException
     */
    public void populateTypesTable() throws SQLException {
        int falseType = 0;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                "SELECT * FROM appointments");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String type = rs.getString("Type");
            int month = Integer.parseInt(rs.getTimestamp("Start").toString().substring(5, 7)) - 1;
            switch (month) {
                case 0 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[0][0]++;
                        case "General Review" -> typesArray[0][1]++;
                        case "Annual Review" -> typesArray[0][2]++;
                        case "Close Account" -> typesArray[0][3]++;
                        default -> falseType = 1;
                    }
                }
                case 1 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[1][0]++;
                        case "General Review" -> typesArray[1][1]++;
                        case "Annual Review" -> typesArray[1][2]++;
                        case "Close Account" -> typesArray[1][3]++;
                        default -> falseType = 1;
                    }
                }
                case 2 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[2][0]++;
                        case "General Review" -> typesArray[2][1]++;
                        case "Annual Review" -> typesArray[2][2]++;
                        case "Close Account" -> typesArray[2][3]++;
                        default -> falseType = 1;
                    }
                }
                case 3 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[3][0]++;
                        case "General Review" -> typesArray[3][1]++;
                        case "Annual Review" -> typesArray[3][2]++;
                        case "Close Account" -> typesArray[3][3]++;
                        default -> falseType = 1;
                    }
                }
                case 4 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[4][0]++;
                        case "General Review" -> typesArray[4][1]++;
                        case "Annual Review" -> typesArray[4][2]++;
                        case "Close Account" -> typesArray[4][3]++;
                        default -> falseType = 1;
                    }
                }
                case 5 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[5][0]++;
                        case "General Review" -> typesArray[5][1]++;
                        case "Annual Review" -> typesArray[5][2]++;
                        case "Close Account" -> typesArray[5][3]++;
                        default -> falseType = 1;
                    }
                }
                case 6 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[6][0]++;
                        case "General Review" -> typesArray[6][1]++;
                        case "Annual Review" -> typesArray[6][2]++;
                        case "Close Account" -> typesArray[6][3]++;
                        default -> falseType = 1;
                    }
                }
                case 7 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[7][0]++;
                        case "General Review" -> typesArray[7][1]++;
                        case "Annual Review" -> typesArray[7][2]++;
                        case "Close Account" -> typesArray[7][3]++;
                        default -> falseType = 1;
                    }
                }
                case 8 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[8][0]++;
                        case "General Review" -> typesArray[8][1]++;
                        case "Annual Review" -> typesArray[8][2]++;
                        case "Close Account" -> typesArray[8][3]++;
                        default -> falseType = 1;
                    }
                }
                case 9 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[9][0]++;
                        case "General Review" -> typesArray[9][1]++;
                        case "Annual Review" -> typesArray[9][2]++;
                        case "Close Account" -> typesArray[9][3]++;
                        default -> falseType = 1;
                    }
                }
                case 10 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[10][0]++;
                        case "General Review" -> typesArray[10][1]++;
                        case "Annual Review" -> typesArray[10][2]++;
                        case "Close Account" -> typesArray[10][3]++;
                        default -> falseType = 1;
                    }
                }
                case 11 -> {
                    switch (type) {
                        case "New Customer" -> typesArray[11][0]++;
                        case "General Review" -> typesArray[11][1]++;
                        case "Annual Review" -> typesArray[11][2]++;
                        case "Close Account" -> typesArray[11][3]++;
                        default -> falseType = 1;
                    }
                }
            }
        }
        for (int i = 0; i < 12; i++){
            int newAcc = typesArray[i][0];
            int genRev = typesArray[i][1];
            int annRev = typesArray[i][2];
            int closeAcc = typesArray[i][3];
            typesReportOL.add(new Report(monthGenerator(i), newAcc, genRev, annRev, closeAcc));
        }
        typesTable.setItems(typesReportOL);
        if (falseType == 1){
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Issue with types table");
            alert.setHeaderText("Appropriate types include 'New Customer', 'General Review', 'Annual Review', 'Close Account'.");
            alert.setContentText("The types by month table will not include one or more of the current scheduled appointments because their 'Type' field contain invalid values");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**This is the Month Generator method, it returns the name of the month
     * according to an input integer.
     *
     * There is also a Lambda expression here defined as a Consumer function,
     * it takes a string, "month", and sets name to this value. The .accept()
     * function on the Consumer applies the Lambda to the value determined
     * by the switch function.
     *
     * @param i an integer corresponding to one of the 12 months
     *
     * @return String of month name     ///LAMBDA EXPRESSION//////
     */                                ////////IS HERE///////////
    public String monthGenerator(int i){ ///////////////////////
        AtomicReference<String> name = new AtomicReference<>();
        Consumer<String> setName = (month) -> name.set(month);
        switch (i) {                /////////////////////////
            case 0 -> setName.accept("January");
            case 1 -> setName.accept("February");
            case 2 -> setName.accept("March");
            case 3 -> setName.accept("April");
            case 4 -> setName.accept("May");
            case 5 -> setName.accept("June");
            case 6 -> setName.accept("July");
            case 7 -> setName.accept("August");
            case 8 -> setName.accept("September");
            case 9 -> setName.accept("October");
            case 10 -> setName.accept("November");
            case 11 -> setName.accept("December");
        }
        return name.get();
    }

    /**
     * This is the initialize method for the screen. It creates all the property value factories for the
     * table columns and sets them to each column. 
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PropertyValueFactory<Report, String> monthFactory = new PropertyValueFactory<>("Month");
        PropertyValueFactory<Report, Integer> newAccFactory = new PropertyValueFactory<>("NewAccount");
        PropertyValueFactory<Report, Integer> genRevFactory = new PropertyValueFactory<>("GenRev");
        PropertyValueFactory<Report, Integer> annRevFactory = new PropertyValueFactory<>("AnnRev");
        PropertyValueFactory<Report, Integer> closeAccFactory = new PropertyValueFactory<>("CloseAccount");
        typesMonthColumn.setCellValueFactory(monthFactory);
        typesNewCustyColumn.setCellValueFactory(newAccFactory);
        typesGenRevColumn.setCellValueFactory(genRevFactory);
        typesAnnRevColumn.setCellValueFactory(annRevFactory);
        typesCloseAccountColumb.setCellValueFactory(closeAccFactory);

        PropertyValueFactory<Appointment, Integer> apptIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, String> typeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, String> descFactory = new PropertyValueFactory<>("Description");
        PropertyValueFactory<Appointment, String> startFactory = new PropertyValueFactory<>("StartTime");
        PropertyValueFactory<Appointment, String> endFactory = new PropertyValueFactory<>("EndTime");
        PropertyValueFactory<Appointment, String> titleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, Integer> custIDFactory = new PropertyValueFactory<>("CustomerID");
        scheduleApptIDColumn.setCellValueFactory(apptIDFactory);
        scheduleTypeColumn.setCellValueFactory(typeFactory);
        scheduleDescColumn.setCellValueFactory(descFactory);
        scheduleStartColumn.setCellValueFactory(startFactory);
        scheduleEndColumn.setCellValueFactory(endFactory);
        scheduleTitleColumn.setCellValueFactory(titleFactory);
        scheduleCustomerIDColumn.setCellValueFactory(custIDFactory);

        PropertyValueFactory<Report, String> userFactory = new PropertyValueFactory<>("UserName");
        PropertyValueFactory<Report, String> lastFactory = new PropertyValueFactory<>("LastUpdate");
        PropertyValueFactory<Report, Integer> totalFactory = new PropertyValueFactory<>("TotalCreated");
        userUserColumn.setCellValueFactory(userFactory);
        userLastUpdateColumn.setCellValueFactory(lastFactory);
        userTotalCreateColumn.setCellValueFactory(totalFactory);

        populateUserActivity();
        try {
            populateTypesTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
