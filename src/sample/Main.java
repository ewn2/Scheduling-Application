package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Customer;

import java.sql.*;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        primaryStage.setTitle("Meeting Scheduling System");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    public static void customerData() throws SQLException {
        int CustomerID = 0;
        String CustomerName = null;
        String CustomerPhone = null;
        String CustomerCountry = null;
        String CustomerPostal = null;
        String CustomerState = null;
        String CustomerAddress = null;
        String logQuery = "SELECT * FROM customers join first_level_divisions on customers.Division_ID=first_level_divisions.Division_ID join countries on countries.Country_ID=first_level_divisions.Country_ID";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs2 = checkQuery.getResultSet();
        while (rs2.next()) {
            CustomerID = rs2.getInt("Customer_ID");
            CustomerName = rs2.getString("Customer_Name");
            CustomerPhone = rs2.getString("Phone");
            CustomerCountry = rs2.getString("Country");
            CustomerPostal = rs2.getString("Postal_Code");
            CustomerState = rs2.getString("Division");
            CustomerAddress = rs2.getString("Address");
            Customer fillerCustomer = new Customer(CustomerID, CustomerName, CustomerPhone, CustomerCountry, CustomerPostal, CustomerState, CustomerAddress);
            Customer.addCustomer(fillerCustomer);
            Customer.usedIDs.add(CustomerID);
        }

    }


    public static void main(String[] args) throws SQLException {
        JDBC.makeConnection();
        String tableName = "users";
        String selectStatement = "SELECT * FROM";
        String allContacts = selectStatement + " " + tableName;
        JDBC.makePreparedStatement(allContacts, JDBC.getConnection());
        Statement selectContactsQuery = JDBC.getPreparedStatement();
        System.out.println(JDBC.getPreparedStatement());
        selectContactsQuery.execute(allContacts);
        ResultSet rs = selectContactsQuery.getResultSet();
        System.out.println(rs);
        while (rs.next()) {
            System.out.println(rs.getString("User_ID") + " " + rs.getString("User_Name") + " " + rs.getString("Password"));
        }
        ZoneId systemZone = ZoneId.systemDefault();
        Locale location = Locale.getDefault();
        String EnFr = location.getLanguage();
        ResourceBundle propertyChoice = ResourceBundle.getBundle("sample/Language", Locale.getDefault());
        System.out.println(systemZone);
        System.out.println(location);
        System.out.println(EnFr);
        customerData();
        launch(args);
        JDBC.closeConnection();
    }
}
