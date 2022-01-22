package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Controller.loginForm;
import sample.JDBC;
import sample.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Vector;

public class Appointment {
    private int AppointmentID;
    private String AppointmentTitle;
    private String AppointmentDesc;
    private String AppointmentLocation;
    private String AppointmentContact;
    private String AppointmentType;
    private String AppointmentStartDateTime;//YYYY-MM-DD hh:mm:ss
    private String AppointmentEndDateTime;//YYYY-MM-DD hh:mm:ss
    private int AppointmentCustomerID;
    private int AppointmentUserID;
    //SQL DATETIME format that database will expect: "YYYY-MM-DD hh:mm:ss"
    //minus quotation marks.


    public Appointment(int AppointmentID, String AppointmentTitle, String AppointmentDesc, String AppointmentLocation, String AppointmentContact, String AppointmentType, String AppointmentStartDateTime, String AppointmentEndDateTime, int AppointmentCustomerID, int AppointmentUserID) {
        this.AppointmentID = AppointmentID;
        this.AppointmentTitle = AppointmentTitle;
        this.AppointmentDesc = AppointmentDesc;
        this.AppointmentLocation = AppointmentLocation;
        this.AppointmentContact = AppointmentContact;
        this.AppointmentType = AppointmentType;
        this.AppointmentStartDateTime = AppointmentStartDateTime;
        this.AppointmentEndDateTime = AppointmentEndDateTime;
        this.AppointmentCustomerID = AppointmentCustomerID;
        this.AppointmentUserID = AppointmentUserID;
    }

    public static boolean addAppointmentToDatabase(Appointment givenAppointment) throws SQLException {
        try {
            int AppointmentContactID = 0;
            String logQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + givenAppointment.getAppointmentContact() + "'";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            ResultSet rs = checkQuery.getResultSet();
            while (rs.next()) {
                AppointmentContactID = rs.getInt("Contact_ID");
            }
            String addCustomerQuery = "INSERT INTO appointments VALUES ("+givenAppointment.getAppointmentID()+", '"+givenAppointment.getAppointmentTitle()+"', '"+givenAppointment.getAppointmentDesc()+"', '"+givenAppointment.getAppointmentLocation()+"', '"+givenAppointment.getAppointmentType()+"', '"+givenAppointment.getAppointmentStartDateTime()+"','"+givenAppointment.getAppointmentEndDateTime()+"', CURRENT_TIMESTAMP, 'program', CURRENT_TIMESTAMP, 'program', "+givenAppointment.getAppointmentCustomerID()+","+givenAppointment.getAppointmentUserID()+","+AppointmentContactID+")";
            JDBC.makePreparedStatement(addCustomerQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addCustomerQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    public int getAppointmentID() {
        return AppointmentID;
    }
    public void setAppointmentID(int AppointmentID) {
        this.AppointmentID = AppointmentID;
    }

    public String getAppointmentTitle() {
        return AppointmentTitle;
    }
    public void setAppointmentTitle(String AppointmentTitle) {
        this.AppointmentTitle = AppointmentTitle;
    }

    public String getAppointmentDesc() {
        return AppointmentDesc;
    }
    public void setAppointmentDesc(String AppointmentDesc) {
        this.AppointmentDesc = AppointmentDesc;
    }

    public String getAppointmentLocation() {
        return AppointmentLocation;
    }
    public void setAppointmentLocation(String AppointmentLocation) {
        this.AppointmentLocation = AppointmentLocation;
    }

    public String getAppointmentContact() {
        return AppointmentContact;
    }
    public void setAppointmentContact(String AppointmentContact) {
        this.AppointmentContact = AppointmentContact;
    }

    public String getAppointmentType() {
        return AppointmentType;
    }
    public void setAppointmentType(String AppointmentType) {
        this.AppointmentType = AppointmentType;
    }

    public String getAppointmentStartDateTime() {
        return AppointmentStartDateTime;
    }
    public void setAppointmentStartDateTime(String AppointmentStartDateTime) {
        this.AppointmentStartDateTime = AppointmentStartDateTime;
    }

    public String getAppointmentEndDateTime() {
        return AppointmentEndDateTime;
    }
    public void setAppointmentEndDateTime(String AppointmentEndDateTime) {
        this.AppointmentEndDateTime = AppointmentEndDateTime;
    }

    public int getAppointmentCustomerID() {
        return AppointmentCustomerID;
    }
    public void setAppointmentCustomerID(int AppointmentCustomerID) {
        this.AppointmentCustomerID = AppointmentCustomerID;
    }

    public int getAppointmentUserID() {
        return AppointmentUserID;
    }
    public void setAppointmentUserID(int AppointmentUserID) {
        this.AppointmentUserID = AppointmentUserID;
    }

    private static int appointmentIDGenerated = -1;

    public static Vector<Integer> usedAppointmentIDs = new Vector<>(0);

    public static int uniqueAppointmentID() {
        appointmentIDGenerated++;
        while (usedAppointmentIDs.contains(appointmentIDGenerated)) {
            appointmentIDGenerated++;
        }
        usedAppointmentIDs.add(appointmentIDGenerated);
        return appointmentIDGenerated;
    }

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentPopulation() throws SQLException {
        allAppointments.clear();
        Main.appointmentData();
        return allAppointments;
    }
    public static ObservableList<Appointment> ContactAppointmentPopulation(int ContactID) throws SQLException {
        allAppointments.clear();
        Main.ContactAppointmentData(ContactID);
        return allAppointments;
    }
    public static ObservableList<Appointment> CustomerAppointmentPopulation(int CustomerID) throws SQLException {
        allAppointments.clear();
        Main.CustomerAppointmentData(CustomerID);
        return allAppointments;
    }

    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }
    public static Appointment lookupAppointment(int appointmentID) {
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }
    public static ObservableList<Appointment> lookupAppointment(String appointmentTitle) {
        ObservableList<Appointment> matches = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentTitle().toUpperCase().contains((String.valueOf(appointment)).toUpperCase())) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    public static boolean modifyAppointmentInDatabase(Appointment givenAppointment) throws SQLException {
        try {
            int AppointmentContactID = 0;
            String logQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + givenAppointment.getAppointmentContact() + "'";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            ResultSet rs = checkQuery.getResultSet();
            while (rs.next()) {
                AppointmentContactID = rs.getInt("Contact_ID");
            }
            String addAppointmentQuery = "UPDATE appointments JOIN users on appointments.User_ID=users.User_ID join contacts on appointments.Contact_ID=contacts.Contact_ID join customers on appointments.Customer_ID=customers.Customer_ID SET Title='"+givenAppointment.getAppointmentTitle()+"', Location='"+givenAppointment.getAppointmentLocation()+"', Type='"+givenAppointment.getAppointmentType()+"', Start='"+givenAppointment.getAppointmentStartDateTime()+"', End='"+givenAppointment.getAppointmentEndDateTime()+"', appointments.Last_Update=CURRENT_TIMESTAMP, appointments.Last_Updated_By='"+ loginForm.loggedInUser +"', appointments.Customer_ID="+givenAppointment.getAppointmentCustomerID()+", appointments.User_ID="+givenAppointment.getAppointmentUserID()+", appointments.Contact_ID="+AppointmentContactID+" WHERE Appointment_ID=" + givenAppointment.getAppointmentID();
            System.out.println(addAppointmentQuery);
            JDBC.makePreparedStatement(addAppointmentQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addAppointmentQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static void updateAppointment(int index, Appointment selectedAppointment) throws SQLException {
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentID() == index) {
                System.out.println(allAppointments.indexOf(appointment));
                int f = allAppointments.indexOf(appointment);
                System.out.println(allAppointments.indexOf(appointment));
                allAppointments.set(f, selectedAppointment);
            }
        }
    }
    public static boolean deleteAppointmentFromDatabase(Appointment givenAppointment) {
        try {
            String deleteAppointmentQuery = "DELETE FROM appointments WHERE Appointment_ID="+givenAppointment.getAppointmentID()+"";
            JDBC.makePreparedStatement(deleteAppointmentQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(deleteAppointmentQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public static boolean deleteAppointment(Appointment selectedAppointment) throws SQLException {
        if (allAppointments.contains(selectedAppointment)) {
            if (usedAppointmentIDs.contains(selectedAppointment.getAppointmentID())) {
                usedAppointmentIDs.clear();
                int AppointmentID = 0;
                String logQuery = "SELECT Appointment_ID FROM appointments join users on appointments.User_ID=users.User_ID join contacts on appointments.Contact_ID=contacts.Contact_ID join customers on appointments.Customer_ID=customers.Customer_ID";
                JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
                Statement checkQuery = JDBC.getPreparedStatement();
                checkQuery.execute(logQuery);
                ResultSet rs2 = checkQuery.getResultSet();
                while (rs2.next()) {
                    AppointmentID = rs2.getInt("Appointment_ID");
                    Appointment.usedAppointmentIDs.add(AppointmentID);
                }
                if (!Appointment.usedAppointmentIDs.contains(0)) {
                    Appointment.usedAppointmentIDs.add(0);
                }
            }
            allAppointments.remove(selectedAppointment);
            return true;
        }
        return false;
    }
}
