package sample.Model;

import sample.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private String username;

    public User(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static Boolean validUser(String username, String password) throws SQLException {
        String logQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while(rs.next())
        {
            return true;
        }
        return false;
    }


    private String logFile = "login_activity.txt";

    public void loginAttempt(String username, String date, String time, boolean valid){
        //Write to a file if it exists, otherwise create it first. Lookup Java file I/O in the morning.
    }
}
