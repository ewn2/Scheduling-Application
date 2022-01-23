package sample.Model;
/**
 * User class data structures and methods
 *
 * @author Erwin Uppal
 */

import sample.JDBC;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class User {
    private static String loggedInUser;
    private static int loggedInUser_ID;

    /**
     * Getter for the private String loggedInUser
     * @return the String value stored within loggedInUser that represents the Logged in User's username
     */
    public String getUsername() {
        return loggedInUser;
    }

    /**
     * Getter for the private int loggedInUser_ID
     * @return the int value stored within loggedInUser_ID that represents the Logged in User's ID number
     */
    public static int getUser_ID() {
        return loggedInUser_ID;
    }

    /**
     * Setter for the private String loggedInUser
     * @param username the String value to be stored within the private loggedInUser
     */
    public static void setUsername(String username) {
        loggedInUser = username;
    }

    /**
     * Setter for the private int loggedInUser_ID
     * @param User_ID the int value to be stored within the private loggedInUser_ID
     */
    public static void setUser_ID(int User_ID) {
        loggedInUser_ID = User_ID;
    }

    /**
     * Takes in the User's provided username and password String values and compares the pair against
     * the existing entries within the users SQL table, also passes details to log the attempt and whether or not
     * it was successful
     * @param username the User's entered username String to be checked within the users SQL table in tandem with the entered
     *                 password String
     * @param password the User's entered password String to be checked within the users SQL table in tandem with the entered
     *                 username String
     * @return the boolean result of if the provided username and password String combination exist together within the SQL database
     * @throws SQLException thrown in case of SQL database issues
     * @throws IOException thrown in case of log file creation/appending issues
     */
    public static Boolean validUser(String username, String password) throws SQLException, IOException {
        String logQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            User.loginAttempt(username, String.valueOf(LocalDate.now()), String.valueOf(LocalTime.now()), true);
            setUsername(username);
            String logQuery2 = "SELECT User_ID FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            ResultSet rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                setUser_ID(rs2.getInt("User_ID"));
            }
            return true;
        }
        User.loginAttempt(username, String.valueOf(LocalDate.now()), String.valueOf(LocalTime.now()), false);
        return false;
    }

    /**
     * Attempts to create or append entries to a log file detailing log in attempts to the application with details
     * passed during the call of the method
     * @param username the username String entered in the login attempt
     * @param date the date String of the login attempt
     * @param time the time String of the login attempt
     * @param valid a boolean representing whether the login attempt had been successful or not
     * @throws IOException thrown in case of log file creation/appending issues
     */
    public static void loginAttempt(String username, String date, String time, boolean valid) throws IOException {
        PrintWriter fout = new PrintWriter(new FileOutputStream(new File("login_activity.txt"), true));
        fout.append("\n" + username + " " + date + " " + time + " " + valid);
        fout.close();
    }
}
