package Jorbo.Model;

import Jorbo.Util.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class models the user objects used throughout the application
 */
public class User {

    private static int userID;
    private static String username;
    private static String password;

    /**
     * This is the constructor used upon login
     * @param userID
     * @param username
     * @param password
     */
    public User(int userID, String username, String password) {
        User.userID = userID;
        User.username = username;
        User.password = password;
    }

    public User() {
        userID = 0;
        username = null;
        password = null;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        User.userID = userID;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    /**
     * A static method that takes a username and returns the user's id
     * @param name username
     * @return user id
     * @throws SQLException
     */
    public static int userNameToID(String name) throws SQLException {
        int ID = 0;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT User_ID FROM users WHERE User_Name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ID = rs.getInt("User_ID");
        }
        return ID;
    }

    /**
     * A static method that takes a user id and returns the username
     * @param ID user id
     * @return username
     * @throws SQLException
     */
    public static String userIDToName(int ID) throws SQLException {
        String name = "";
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT User_Name FROM users WHERE User_ID = ?");
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            name = rs.getString("User_Name");
        }
        return name;
    }
}
