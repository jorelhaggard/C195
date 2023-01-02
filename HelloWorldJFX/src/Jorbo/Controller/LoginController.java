package Jorbo.Controller;

import Jorbo.Main;
import Jorbo.Model.Appointment;
import Jorbo.Model.User;
import Jorbo.Util.Chrono;
import Jorbo.Util.JDBC;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides control for the login screen.
 */
public class LoginController implements Initializable {

    public DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public Label loginLoginLabel;
    public Label loginRegionLabel;
    public Button loginLoginButton;
    public Label loginUsernameField;
    public Label loginPasswordField;
    public ObservableList<Appointment> reminderOL = FXCollections.observableArrayList();
    public String error = "";
    public TextField loginUsernameText;
    public PasswordField loginPasswordText;

    /**
     * this method converts a given username to the corresponding user id.
     * @param username
     * @return
     * @throws SQLException
     */
    public int nametoID(String username) throws SQLException {
        int id = 0;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                "SELECT * FROM users WHERE User_Name = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            id = rs.getInt("User_ID");
        }
        return id;
    }

    /**
     * This method is called when the login button is pressed. It parses the provided username and password,
     * then calls loginCheck() to check if they are valid. If they are, the main screen is loaded, otherwise an
     * alert is displayed.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void loginLoginButtonPress(ActionEvent actionEvent) throws IOException, SQLException {
        String password = loginPasswordText.getText();
        String username = loginUsernameText.getText();
        int id = nametoID(username);
        User user = new User();
        if (loginValidCheck(username, password)){
            User.setUserID(id);
            User.setUsername(username);
            loginLog(User.getUsername(), 0);
            apptAlert(filterAppts(makeApptList()));
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/MainScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 237, 292);
            Stage stage = (Stage) (loginLoginButton.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth())/2);
            stage.setY((screenBounds.getHeight() - stage.getHeight())/2);
        } else {
            loginLog(username, 1);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(error);
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * This method checks a given username and password for validity using the sql table users.
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public boolean loginValidCheck(String username, String password) throws SQLException {
        boolean r = false;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("" +
                "SELECT * FROM users WHERE User_Name = ? AND Password = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            r = true;
        }
        return r;
    }

    /**
     * This method controls the login logging by creating a new buffered writer and filewriter
     * and using them to append new login records to the file login_activity.txt.
     * @param username
     * @param succ
     */
    public void loginLog(String username, int succ){
        String attempt = "";
        switch (succ){
            case 0 -> attempt += "Successful Login";
            case 1 -> attempt += "Failed Login";
        }
        try {
            String name = "login_activity.txt";
            BufferedWriter buff = new BufferedWriter(new FileWriter(name, true));
            buff.append(String.valueOf(Chrono.getTimestamp())).append(" / ").append(username).append(" / ").append(attempt).append("\n");
            System.out.println("New login appended to Log.");
            buff.flush();
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This is the initialize method for the screen. Here, the resource bundle is set to the default of the system. For
     * french systems, this will populate the login screen and its alerts to all french language.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginRegionLabel.setText(Locale.getDefault().toString());
        try {
            rb = ResourceBundle.getBundle("Jorbo.Properties.rb", Locale.getDefault());
            loginLoginLabel.setText(rb.getString("label"));
            loginUsernameField.setText(rb.getString("username"));
            loginPasswordField.setText(rb.getString("password"));
            loginLoginButton.setText(rb.getString("login"));
            error = rb.getString("error");
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
    }

    /**
     * This method creates and returns a list of all appointments for the current user.
     * @return
     */
    public ObservableList<Appointment> makeApptList(){
        ObservableList<Appointment> appointmentsOL = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Appointment_ID, Start FROM appointments" +
                    " WHERE User_ID = ?");
            ps.setInt(1, User.getUserID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                Timestamp startTS = rs.getTimestamp("Start");
                appointmentsOL.add(new Appointment(apptID, startTS.toString()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsOL;

    }

    /**
     * This method takes an observable list of appointments and filters it to only include appointments starting in the
     * next 15 minutes of the system's local time. It then returns the filtered list.
     * @param OL
     * @return
     */
    public ObservableList<Appointment> filterAppts(ObservableList<Appointment> OL){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15 = now.plusMinutes(15);
        ObservableList<Appointment> filteredOL = FXCollections.observableArrayList();
        for (Appointment a : OL){
            String start = a.getStartTime().substring(0,10) + 'T' + a.getStartTime().substring(11,19);
            LocalDateTime startLDT = LocalDateTime.parse(start);
            if (startLDT.isEqual(now) || (startLDT.isAfter(now) && startLDT.isBefore(nowPlus15))){
                filteredOL.add(a);
            }
        }
        return filteredOL;
    }

    /**
     * This method takes the filtered observable list of appointments. If it's empty,
     * an alert is displayed saying so. If there is an upcoming appointment, this method
     * creates a custom alert for it and displays it.
     * @param OL
     */
    public void apptAlert(ObservableList<Appointment> OL){
        String error = "";
        String context = "";
        if (OL.isEmpty()){
            error = "No upcoming appointments.";
            context = "";
        } else {
            int apptID = OL.get(0).getAppointmentID();
            String start = OL.get(0).getStartTime();
            error = "Upcoming appointment found!";
            context = "Appointment " + apptID + " starting soon at " + start + "!";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checking for upcoming appointments");
        alert.setHeaderText(error);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
    }
}
