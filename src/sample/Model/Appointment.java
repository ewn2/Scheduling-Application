package sample.Model;
/**
 * Appointment data structures and methods
 */

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
    private String AppointmentStartDateTime;
    private String AppointmentEndDateTime;
    private int AppointmentCustomerID;
    private int AppointmentUserID;

    /**
     * Appointment data structure
     * @param AppointmentID The Appointment's ID, unique as used to reference to specific instances of Appointment
     * @param AppointmentTitle The Appointment's title string
     * @param AppointmentDesc The Appointment's description string
     * @param AppointmentLocation The Appointment's location string
     * @param AppointmentContact The Appointment's designated Contact that exists within the database
     * @param AppointmentType The Appointment's type description string
     * @param AppointmentStartDateTime The Appointment's Start Date and Time, stored as a String but treated as
     *                                 a DateTime in most instances and converted as needed whenever accessing
     *                                 or assigning value
     * @param AppointmentEndDateTime The Appointment's End Date and Time, stored as a String but treated as
     *                               a DateTime in most instances and converted as needed whenever accessing
     *                               or assigning value
     * @param AppointmentCustomerID The Appointment's designated Customer that exists within the database
     * @param AppointmentUserID The Appointment's designated User that exists within the database
     */
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

    /**
     * Constructs and executes a prepared Insert sql query as a middle-man translator between instances
     * of the Appointment data structure and the SQL database being added to
     * @param givenAppointment the provided instance of Appointment whose values are to be entered into the SQL database
     * @return a boolean value indicating a successful sql insertion
     * @throws SQLException thrown in case of SQL database interaction issues
     */
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

    /**
     * Returns the AppointmentID integer for an instance of Appointment
     * @return The integer value stored within private AppointmentID
     */
    public int getAppointmentID() {
        return AppointmentID;
    }

    /**
     * Sets the AppointmentID integer for an instance of Appointment
     * @param AppointmentID The passed integer to be set as the value of private AppointmentID
     */
    public void setAppointmentID(int AppointmentID) {
        this.AppointmentID = AppointmentID;
    }

    /**
     * Returns the AppointmentTitle String for an instance of Appointment
     * @return The String value stored within private AppointmentTitle
     */
    public String getAppointmentTitle() {
        return AppointmentTitle;
    }

    /**
     * Sets the AppointmentTitle String for an instance of Appointment
     * @param AppointmentTitle
     */
    public void setAppointmentTitle(String AppointmentTitle) {
        this.AppointmentTitle = AppointmentTitle;
    }

    /**
     *
     * @return
     */
    public String getAppointmentDesc() {
        return AppointmentDesc;
    }

    /**
     *
     * @param AppointmentDesc
     */
    public void setAppointmentDesc(String AppointmentDesc) {
        this.AppointmentDesc = AppointmentDesc;
    }

    /**
     *
     * @return
     */
    public String getAppointmentLocation() {
        return AppointmentLocation;
    }

    /**
     *
     * @param AppointmentLocation
     */
    public void setAppointmentLocation(String AppointmentLocation) {
        this.AppointmentLocation = AppointmentLocation;
    }

    /**
     *
     * @return
     */
    public String getAppointmentContact() {
        return AppointmentContact;
    }

    /**
     *
     * @param AppointmentContact
     */
    public void setAppointmentContact(String AppointmentContact) {
        this.AppointmentContact = AppointmentContact;
    }

    /**
     *
     * @return
     */
    public String getAppointmentType() {
        return AppointmentType;
    }

    /**
     *
     * @param AppointmentType
     */
    public void setAppointmentType(String AppointmentType) {
        this.AppointmentType = AppointmentType;
    }

    /**
     *
     * @return
     */
    public String getAppointmentStartDateTime() {
        return AppointmentStartDateTime;
    }

    /**
     *
     * @param AppointmentStartDateTime
     */
    public void setAppointmentStartDateTime(String AppointmentStartDateTime) {
        this.AppointmentStartDateTime = AppointmentStartDateTime;
    }

    /**
     *
     * @return
     */
    public String getAppointmentEndDateTime() {
        return AppointmentEndDateTime;
    }

    /**
     *
     * @param AppointmentEndDateTime
     */
    public void setAppointmentEndDateTime(String AppointmentEndDateTime) {
        this.AppointmentEndDateTime = AppointmentEndDateTime;
    }

    /**
     *
     * @return
     */
    public int getAppointmentCustomerID() {
        return AppointmentCustomerID;
    }

    /**
     *
     * @param AppointmentCustomerID
     */
    public void setAppointmentCustomerID(int AppointmentCustomerID) {
        this.AppointmentCustomerID = AppointmentCustomerID;
    }

    /**
     *
     * @return
     */
    public int getAppointmentUserID() {
        return AppointmentUserID;
    }

    /**
     *
     * @param AppointmentUserID
     */
    public void setAppointmentUserID(int AppointmentUserID) {
        this.AppointmentUserID = AppointmentUserID;
    }

    private static int appointmentIDGenerated = -1;

    public static Vector<Integer> usedAppointmentIDs = new Vector<>(0);

    /**
     *
     * @return
     */
    public static int uniqueAppointmentID() {
        appointmentIDGenerated++;
        while (usedAppointmentIDs.contains(appointmentIDGenerated)) {
            appointmentIDGenerated++;
        }
        usedAppointmentIDs.add(appointmentIDGenerated);
        return appointmentIDGenerated;
    }

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     *
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> appointmentPopulation() throws SQLException {
        allAppointments.clear();
        Main.appointmentData();
        return allAppointments;
    }

    /**
     *
     * @param ContactID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> ContactAppointmentPopulation(int ContactID) throws SQLException {
        allAppointments.clear();
        Main.ContactAppointmentData(ContactID);
        return allAppointments;
    }

    /**
     *
     * @param CustomerID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> CustomerAppointmentPopulation(int CustomerID) throws SQLException {
        allAppointments.clear();
        Main.CustomerAppointmentData(CustomerID);
        return allAppointments;
    }

    /**
     *
     * @param newAppointment
     */
    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    /**
     *
     * @param appointmentID
     * @return
     */
    public static Appointment lookupAppointment(int appointmentID) {
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }

    /**
     *
     * @param appointmentTitle
     * @return
     */
    public static ObservableList<Appointment> lookupAppointment(String appointmentTitle) {
        ObservableList<Appointment> matches = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentTitle().toUpperCase().contains((String.valueOf(appointment)).toUpperCase())) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     *
     * @param givenAppointment
     * @return
     * @throws SQLException
     */
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
            JDBC.makePreparedStatement(addAppointmentQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addAppointmentQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param index
     * @param selectedAppointment
     * @throws SQLException
     */
    public static void updateAppointment(int index, Appointment selectedAppointment) throws SQLException {
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentID() == index) {
                int f = allAppointments.indexOf(appointment);
                allAppointments.set(f, selectedAppointment);
            }
        }
    }

    /**
     *
     * @param givenAppointment
     * @return
     */
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

    /**
     *
     * @param selectedAppointment
     * @return
     * @throws SQLException
     */
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
