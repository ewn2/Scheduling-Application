package sample;
/**
 * Class focused on connecting and managing query statements with
 * SQL database
 *
 * @author Precreated in WGU JDK 11 Virtual Lab
 */

import java.sql.*;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement;

    /**
     * Logs in to the SQL database host
     */
    public static void makeConnection() {

        try {
            Class.forName(driver); // Locate Driver
            //password = Details.getPassword(); // Assign password
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     *
     * @return the connection interface for the database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Terminates the connection to the database host
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Takes in a string and SQL database connection target and has the string readied to be sent as
     * a SQL query to the target connection.
     * @param sqlStatement string to be treated as a SQL query
     * @param conn connection interface to use
     * @throws SQLException thrown in case of SQL database issues
     */
    public static void makePreparedStatement(String sqlStatement, Connection conn) throws SQLException {
        if (conn != null)
            preparedStatement = conn.prepareStatement(sqlStatement);
        else
            System.out.println("Prepared Statement Creation Failed!");
    }

    /**
     * returns an already prepared statement with Query String and Connection target loaded
     * @return the prepared statement
     * @throws SQLException thrown in case of SQL database issues
     */
    public static PreparedStatement getPreparedStatement() throws SQLException {
        if (preparedStatement != null)
            return preparedStatement;
        else System.out.println("Null reference to Prepared Statement");
        return null;
    }

}