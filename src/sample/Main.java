package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller.loginForm;
import sample.Model.Appointment;
import sample.Model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
        if (!Customer.usedIDs.contains(0)) {
            Customer.usedIDs.add(0);
        }
    }

    public static void appointmentData() throws SQLException {
        int AppointmentID = 0;
        String AppointmentTitle = null;
        String AppointmentDesc = null;
        String AppointmentLocation = null;
        String AppointmentContact = null;
        String AppointmentType = null;
        String AppointmentStartDateTime = null;//YYYY-MM-DD hh:mm:ss
        String AppointmentEndDateTime = null;//YYYY-MM-DD hh:mm:ss
        int AppointmentCustomerID = 0;
        int AppointmentUserID = 0;
        String logQuery = "SELECT * FROM appointments join customers on appointments.Customer_ID=customers.Customer_ID join contacts on appointments.Contact_ID=contacts.Contact_ID join users on appointments.User_ID=users.User_ID";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs2 = checkQuery.getResultSet();
        while (rs2.next()) {
            AppointmentID = rs2.getInt("Appointment_ID");
            AppointmentTitle = rs2.getString("Title");
            AppointmentDesc = rs2.getString("Description");
            AppointmentLocation = rs2.getString("Location");
            AppointmentContact = rs2.getString("Contact_Name");
            AppointmentType = rs2.getString("Type");
            AppointmentStartDateTime = rs2.getTimestamp("Start").toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S z"));
            AppointmentEndDateTime = rs2.getTimestamp("End").toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S z"));
            AppointmentCustomerID = rs2.getInt("Customer_ID");
            AppointmentUserID = rs2.getInt("User_ID");
            Appointment fillerAppointment = new Appointment(AppointmentID, AppointmentTitle, AppointmentDesc, AppointmentLocation, AppointmentContact, AppointmentType, AppointmentStartDateTime, AppointmentEndDateTime, AppointmentCustomerID, AppointmentUserID);
            Appointment.addAppointment(fillerAppointment);
            Appointment.usedAppointmentIDs.add(AppointmentID);
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
        appointmentData();
        launch(args);
        JDBC.closeConnection();
    }
}
