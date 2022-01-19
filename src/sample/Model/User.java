package sample.Model;

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

    /*
    public User(){
    }
*/
    public String getUsername() {
        return loggedInUser;
    }
    public int getUser_ID(){
        return loggedInUser_ID;
    }

    public static void setUsername(String username) {
        loggedInUser = username;
    }

    public static void setUser_ID(int User_ID) {
        loggedInUser_ID = User_ID;
    }

    public static Boolean validUser(String username, String password) throws SQLException, IOException {
        String logQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while(rs.next())
        {
            User.loginAttempt(username, String.valueOf(LocalDate.now()), String.valueOf(LocalTime.now()),true);
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
        User.loginAttempt(username, String.valueOf(LocalDate.now()), String.valueOf(LocalTime.now()),false);
        return false;
    }

    public static void loginAttempt(String username, String date, String time, boolean valid) throws IOException {
        PrintWriter fout = new PrintWriter(new FileOutputStream(new File("login_activity.txt"),true));
        fout.append("\n" + username + " " + date + " " + time + " " + valid);
        fout.close();
    }
}
