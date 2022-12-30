package Jorbo.Model;

public class User {

    private static int userID;
    private static String username;
    private static String password;

    public User(int userID, String username, String password){
        User.userID = userID;
        User.username = username;
        User.password = password;
    }

    public User(){
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
}
