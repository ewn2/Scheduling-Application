package sample.Controller;
/**
 * viewReport.fxml related Controller
 *
 * @author Erwin Uppal
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.JDBC;
import sample.Model.Appointment;
import sample.Model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

public class ViewReport implements Initializable {
    public Button viewReportCancelButton;
    public TableView<theMonth> reportMonthBreakdownTable;
    public TableColumn<theMonth, String> reportMonthBreakdownTableMonthCol;
    public TableColumn<theMonth, Integer> reportMonthBreakdownTableTotalCol;
    public TableView<theType> reportTypeBreakdownTable;
    public TableColumn<theType, String> reportTypeBreakdownTableTypeCol;
    public TableColumn<theType, Integer> reportTypeBreakdownTableTotalCol;
    public ComboBox selectContactCombo;
    public TableView<Appointment> ContactAppointmentTable;
    public TableColumn<Appointment, Integer> ContactAppointmentTableAppointmentIDCol;
    public TableColumn<Appointment, String> ContactAppointmentTableTitleCol;
    public TableColumn<Appointment, String> ContactAppointmentTableDescriptionCol;
    public TableColumn<Appointment, String> ContactAppointmentTableLocationCol;
    public TableColumn<Appointment, String> ContactAppointmentTableContactCol;
    public TableColumn<Appointment, String> ContactAppointmentTableTypeCol;
    public TableColumn<Appointment, String> ContactAppointmentTableStartDateAndTimeCol;
    public TableColumn<Appointment, String> ContactAppointmentTableEndDateAndTimeCol;
    public TableColumn<Appointment, Integer> ContactAppointmentTableCustomerIDCol;
    public ComboBox selectCustomerCombo;
    public TableView<Appointment> CustomerAppointmentTable;
    public TableColumn<Appointment, String> CustomerAppointmentTableAppointmentIDCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableTitleCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableDescriptionCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableLocationCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableContactCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableTypeCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableStartDateAndTimeCol;
    public TableColumn<Appointment, String> CustomerAppointmentTableEndDateAndTimeCol;
    public TableColumn<Appointment, Integer> CustomerAppointmentTableCustomerIDCol;
    public TableColumn<Appointment, Integer> ContactAppointmentTableUserIDCol;
    public TableColumn<Appointment, Integer> CustomerAppointmentTableUserIDCol;
    public TableView<theSet> AsOneBreakdownTable;
    public TableColumn<theSet, String> AsOneBreakdownTableMonthCol;
    public TableColumn<theSet, String> AsOneBreakdownTableTypeCol;
    public TableColumn<theSet, Integer> AsOneBreakdownTableTotalCol;

    /**
     * Closes the View Reports view and calls a method to return to the MainForm view
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of FXML file interaction issues
     */
    public void onViewReportCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }


    public static ObservableList<Appointment> emptyAppointmentView = FXCollections.observableArrayList();

    /**
     * Clears report tables so that the User's selected Contact or Customer may have their associated Appointments shown
     *
     * @param event tab selection action by User
     */
    public void onReportTabSelectionAction(Event event) {
        ContactAppointmentTable.setVisible(false);
        CustomerAppointmentTable.setVisible(false);
        if (emptyAppointmentView.isEmpty()) {
            Appointment temp = new Appointment(0, "", "", "", "", "", "", "", 0, 0);
            emptyAppointmentView.add(temp);
        }
        selectCustomerCombo.setValue("");
        selectContactCombo.setValue("");
    }

    public static class theType {
        public String typeName = null;
        public int TotalApps = 0;

        /**
         * Data structure used for compiling the breakdown of Appointments by Appointment type
         *
         * @param typeName  The Type String existing associated with an Appointment
         * @param TotalApps The count of instances of the Type String
         */
        public theType(String typeName, int TotalApps) {
            this.typeName = typeName;
            this.TotalApps = TotalApps;
        }

        /**
         * Gets the typeName String for an instance of theType
         *
         * @return The String value stored within typeName
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         * Sets the typeName String for an instance of theType
         *
         * @param typeName the String value to be set in an instance of theType
         */
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        /**
         * gets the TotalApps integer for an instance of theType
         *
         * @return The integer value stored within TotalApps
         */
        public int getTotalApps() {
            return TotalApps;
        }

        /**
         * Sets the TotalApps integer value for an instance of theType
         *
         * @param TotalApps the integer value to be set in an instance of theType
         */
        public void setTotalApps(int TotalApps) {
            this.TotalApps = TotalApps;
        }

        /**
         * Iterates the value of found instances of a Type by one each time it is called
         */
        public void found() {
            TotalApps++;
        }
    }

    private static ObservableList<theType> allTypes = FXCollections.observableArrayList();


    public static class theMonth {
        public String monthName = null;
        public int TotalApps = 0;

        /**
         * Creates an instance of data structure theMonth that includes a String signifying the Month and an integer
         * signifying the amount of Appointments associated with that month name
         *
         * @param monthName The String representing the Month's Name
         * @param TotalApps The integer representing the amount of Appointments associated with the Month
         */
        public theMonth(String monthName, int TotalApps) {
            this.monthName = monthName;
            this.TotalApps = TotalApps;
        }

        /**
         * Gets the String value within monthName in the instance of theMonth
         *
         * @return the String monthName
         */
        public String getMonthName() {
            return monthName;
        }

        /**
         * Sets the String value within monthName
         *
         * @param monthName the String to be placed within the instance of theMonth
         */
        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }

        /**
         * gets the TotalApps integer for an instance of theMonth
         *
         * @return The integer value stored within TotalApps
         */
        public int getTotalApps() {
            return TotalApps;
        }

        /**
         * Sets the TotalApps integer value for an instance of theMonth
         *
         * @param TotalApps the integer value to be set in an instance of theMonth
         */
        public void setTotalApps(int TotalApps) {
            this.TotalApps = TotalApps;
        }
    }

    private static ObservableList<theMonth> allMonths = FXCollections.observableArrayList();

    /**
     * Populates the allTypes ObservableList with Distinct Type String instances and their Count from the SQL database
     * of existing appointments
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateTypes() throws SQLException {
        String typeName = null;
        int typeCount = 0;
        String logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments GROUP BY type";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs2 = checkQuery.getResultSet();
        while (rs2.next()) {
            typeName = rs2.getString("type");
            typeCount = rs2.getInt("typeCount");
            theType fillerType = new theType(typeName, typeCount);
            allTypes.add(fillerType);
        }
    }

    /**
     * Populates the allMonths ObservableList with String values of all of the month names and counts through the
     * month values of all existing Appointments associated with those month names
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateMonths() throws SQLException {
        int January = 0;
        int February = 0;
        int March = 0;
        int April = 0;
        int May = 0;
        int June = 0;
        int July = 0;
        int August = 0;
        int September = 0;
        int October = 0;
        int November = 0;
        int December = 0;
        for (Appointment appointment : Appointment.appointmentPopulation()) {
            String aptStart = appointment.getAppointmentStartDateTime();
            String handler1 = aptStart.substring(5, 7);
            if (handler1.equals("01")) {
                January++;
            }
            if (handler1.equals("02")) {
                February++;
            }
            if (handler1.equals("03")) {
                March++;
            }
            if (handler1.equals("04")) {
                April++;
            }
            if (handler1.equals("05")) {
                May++;
            }
            if (handler1.equals("06")) {
                June++;
            }
            if (handler1.equals("07")) {
                July++;
            }
            if (handler1.equals("08")) {
                August++;
            }
            if (handler1.equals("09")) {
                September++;
            }
            if (handler1.equals("10")) {
                October++;
            }
            if (handler1.equals("11")) {
                November++;
            }
            if (handler1.equals("12")) {
                December++;
            }
        }
        allMonths.add(new theMonth("January", January));
        allMonths.add(new theMonth("February", February));
        allMonths.add(new theMonth("March", March));
        allMonths.add(new theMonth("April", April));
        allMonths.add(new theMonth("May", May));
        allMonths.add(new theMonth("June", June));
        allMonths.add(new theMonth("July", July));
        allMonths.add(new theMonth("August", August));
        allMonths.add(new theMonth("September", September));
        allMonths.add(new theMonth("October", October));
        allMonths.add(new theMonth("November", November));
        allMonths.add(new theMonth("December", December));
    }

    /**
     * Populates the Contacts ComboBox with all Contacts existing within the contacts table in the SQL database
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateContacts() throws SQLException {
        String logQuery = "SELECT * FROM contacts";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Contact_ID");
            selectContactCombo.getItems().add(ire);
        }
    }

    /**
     * Populates the Customers ComboBox with all Customers existing within the customers table in the SQL database
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateCustomers() throws SQLException {
        String logQuery = "SELECT * FROM customers";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Customer_ID");
            selectCustomerCombo.getItems().add(ire);
        }
    }

    /**
     * When called, loads in MainForm.fxml as the current User-facing scene
     *
     * @param event Initiating call from other method
     * @throws IOException thrown in case of FXML file interaction issues
     */
    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/View/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Populates and displays a table view of all appointments and their details associated with the Contact
     * selected by the User
     *
     * @param actionEvent Contact ComboBox selection action by User
     */
    public void onSelectContactComboAction(ActionEvent actionEvent) {
        boolean selected;
        try {
            Integer.parseInt(selectContactCombo.getValue().toString());
            selected = true;
        } catch (NumberFormatException e) {
            selected = false;
        }
        if (selected) {
            int selectedContact = Integer.parseInt(selectContactCombo.getValue().toString());
            ContactAppointmentTable.setVisible(true);
            try {
                ContactAppointmentTable.setItems(Appointment.ContactAppointmentPopulation(selectedContact));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            ContactAppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
            ContactAppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
            ContactAppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
            ContactAppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
            ContactAppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
            ContactAppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
            ContactAppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
            ContactAppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
            ContactAppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
            ContactAppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
            ContactAppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
            ContactAppointmentTable.getSortOrder().add(ContactAppointmentTableAppointmentIDCol);
            ContactAppointmentTable.sort();
        }
    }

    /**
     * Populates and displays a table view of all appointments and their details associated with the Customer
     * selected by the User
     *
     * @param actionEvent Customer ComboBox selection action by User
     */
    public void onSelectCustomerComboAction(ActionEvent actionEvent) {
        boolean selected;
        try {
            Integer.parseInt(selectCustomerCombo.getValue().toString());
            selected = true;
        } catch (NumberFormatException e) {
            selected = false;
        }
        if (selected) {
            CustomerAppointmentTable.setVisible(true);
            int selectedCustomer = Integer.parseInt(selectCustomerCombo.getValue().toString());
            try {
                CustomerAppointmentTable.setItems(Appointment.CustomerAppointmentPopulation(selectedCustomer));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            CustomerAppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
            CustomerAppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
            CustomerAppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
            CustomerAppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
            CustomerAppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
            CustomerAppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
            CustomerAppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
            CustomerAppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
            CustomerAppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
            CustomerAppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
            CustomerAppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
            CustomerAppointmentTable.getSortOrder().add(CustomerAppointmentTableAppointmentIDCol);
            CustomerAppointmentTable.sort();
        }
    }

    private static ObservableList<theSet> allSets = FXCollections.observableArrayList();

    public static class theSet {
        public String monthName = null;
        public String typeName = null;
        public int TotalApps = 0;

        /**
         * Creates an instance of data structure theSet that includes a String signifying the Month, a String signifying
         * the Appointment type and an integer signifying the amount of Appointments associated with that month name
         *
         * @param monthName The String representing the Month's Name
         * @param typeName The String representing the Type descriptor
         * @param TotalApps The integer representing the amount of Appointments associated with the Month
         */
        public theSet(String monthName, String typeName, int TotalApps) {
            this.monthName = monthName;
            this.typeName = typeName;
            this.TotalApps = TotalApps;
        }

        /**
         * Gets the String value within monthName in the instance of theSet
         *
         * @return the String monthName
         */
        public String getMonthName() {
            return monthName;
        }

        /**
         * Sets the String value within monthName
         *
         * @param monthName the String to be placed within the instance of theSet
         */
        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }

        /**
         * Gets the String value within monthName in the instance of theSet
         *
         * @return the String typeName
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         * Sets the String value within monthName
         *
         * @param typeName the String to be placed within the instance of theSet
         */
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        /**
         * gets the TotalApps integer for an instance of theSet
         *
         * @return The integer value stored within TotalApps
         */
        public int getTotalApps() {
            return TotalApps;
        }

        /**
         * Sets the TotalApps integer value for an instance of theSet
         *
         * @param TotalApps the integer value to be set in an instance of theSet
         */
        public void setTotalApps(int TotalApps) {
            this.TotalApps = TotalApps;
        }
    }

    /**
     * Populates the ObservableList allSets with instances of theSet based upon the returned value of a SQL query of
     * all entries in the appointments table broken down by month and then type of Appointment
     * @throws SQLException in case of Database interaction issues
     */
    public void asOnePopulation() throws SQLException {
        {
            String monthName = null;
            String typeName = null;
            int typeCount = 0;
            String logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=1 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            ResultSet rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "January";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=2 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "February";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=3 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "March";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=4 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "April";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=5 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "May";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=6 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "June";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=7 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "July";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=8 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "August";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=9 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "September";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=10 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "October";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=11 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "November";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
            logQuery = "SELECT type, COUNT(*) AS typeCount FROM appointments where extract(month from Start)=12 GROUP BY type";
            JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
            checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(logQuery);
            rs2 = checkQuery.getResultSet();
            while (rs2.next()) {
                monthName = "December";
                typeName = rs2.getString("type");
                typeCount = rs2.getInt("typeCount");
                theSet fillerSet = new theSet(monthName, typeName, typeCount);
                allSets.add(fillerSet);
            }
        }
    }

    /**
     * Initializer for View Reports screen, loads in Combo Boxes with existing values from database and
     * populates Summary tables with breakdown of Appointments by month and by type
     *
     * @param url            resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactAppointmentTable.setVisible(false);
        CustomerAppointmentTable.setVisible(false);
        allMonths.clear();
        allTypes.clear();
        allSets.clear();
        try {
            populateTypes();
        } catch (SQLException throwables) {
            System.out.println("Error loading Appointments by Type from Database");
        }
        try {
            populateMonths();
        } catch (SQLException throwables) {
            System.out.println("Error loading Appointments by Month from Database");
        }
        try {
            asOnePopulation();
        } catch (SQLException throwables) {
            System.out.println("Error loading Appointments by Month and Type from Database");
        }
        reportTypeBreakdownTable.setItems(allTypes);
        reportTypeBreakdownTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        reportTypeBreakdownTableTotalCol.setCellValueFactory(new PropertyValueFactory<>("TotalApps"));
        reportMonthBreakdownTable.setItems(allMonths);
        reportMonthBreakdownTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("monthName"));
        reportMonthBreakdownTableTotalCol.setCellValueFactory(new PropertyValueFactory<>("TotalApps"));
        AsOneBreakdownTable.setItems(allSets);;
        AsOneBreakdownTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("monthName"));;
        AsOneBreakdownTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        AsOneBreakdownTableTotalCol.setCellValueFactory(new PropertyValueFactory<>("TotalApps"));
        try {
            populateContacts();
        } catch (SQLException throwables) {
            System.out.println("Error loading Contacts from Database");
        }
        try {
            populateCustomers();
        } catch (SQLException throwables) {
            System.out.println("Error loading Customers from Database");
        }

        selectCustomerCombo.setValue("");
        selectContactCombo.setValue("");
    }
}
