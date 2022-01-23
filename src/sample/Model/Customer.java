package sample.Model;
/**
 *  Customer class data structures and methods
 *
 * @author Erwin Uppal
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Controller.loginForm;
import sample.JDBC;
import sample.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Customer {
    private ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();
    private int CustomerID;
    private String CustomerName;
    private String CustomerPhone;
    private String CustomerCountry;
    private String CustomerPostal;
    private String CustomerState;
    private String CustomerAddress;

    /**
     * Adds the passed instance of Appointment into an ObservableList of appointments associated with a specific instance
     * of Customer
     * @param appointment the Appointment to be added to the ObservableList
     */
    public void addAssociatedAppointment(Appointment appointment) {
        boolean alreadyIn = false;
        for (Appointment i : associatedAppointments) {
            if (i.getAppointmentID() == appointment.getAppointmentID()) {
                alreadyIn = true;
            }
        }
        if (!alreadyIn) {
            associatedAppointments.add(appointment);
        }
    }

    /**
     * Removes the passed instance of Appointment from an ObservableList of appointments associated with a specific instance
     * of Customer and returns a boolean value of true if it has successfully been removed
     * @param appointment the Appointment to be removed from the ObservableList
     * @return boolean indication if the removal operation has successfully been carried out
     */
    public boolean deleteAssociatedAppointments(Appointment appointment) {
        for (Appointment i : associatedAppointments) {
            if (i.getAppointmentID() == appointment.getAppointmentID()) {
                associatedAppointments.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the private ObservableList associatedAppointments which contains all appointments associated with a specific
     * instance of Customer
     * @return the private ObservableList associatedAppointments
     */
    public ObservableList<Appointment> getAssociatedAppointments() {
        return associatedAppointments;
    }

    private static int customerIDGenerated = -1;

    public static Vector<Integer> usedIDs = new Vector<>(0);

    /**
     * Manages the list of integer values already in-use with an instance of Customer and returns an as yet unused integer
     * and adds that provided integer to the Vector of ID integers in use
     * @return an integer that has not already been used with any other instance of Customer
     */
    public static int uniqueCustomerID() {
        customerIDGenerated++;
        while (usedIDs.contains(customerIDGenerated)) {
            customerIDGenerated++;
        }
        usedIDs.add(customerIDGenerated);
        return customerIDGenerated;
    }

    /**
     * Customer data structure
     * @param CustomerID The Customer's ID, unique as used to reference to specific instances of Customer
     * @param CustomerName The Customer's name string
     * @param CustomerPhone The Customer's phone string
     * @param CustomerCountry The Customer's country string
     * @param CustomerPostal The Customer's postal or zip code string
     * @param CustomerState The Customer's first-level division, i.e. State or Province, string
     * @param CustomerAddress The Customer's address string
     */
    public Customer(int CustomerID, String CustomerName, String CustomerPhone, String CustomerCountry, String CustomerPostal, String CustomerState, String CustomerAddress) {
        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.CustomerPhone = CustomerPhone;
        this.CustomerCountry = CustomerCountry;
        this.CustomerPostal = CustomerPostal;
        this.CustomerState = CustomerState;
        this.CustomerAddress = CustomerAddress;
    }

    /**
     * Constructs and executes a prepared Insert sql query as a middle-man translator between instances
     * of the Customer data structure and the SQL database being added to
     *
     * @param givenCustomer the provided instance of Customer whose values are to be entered into the SQL database
     * @return a boolean value indicating a successful sql insertion
     * @throws SQLException thrown in case of SQL database issues
     */
    public static boolean addCustomerToDatabase(Customer givenCustomer) throws SQLException {
        try {
            int division = 0;
            String divQuery = "SELECT Division_ID from first_level_divisions join countries on first_level_divisions.Country_ID=countries.Country_ID WHERE Country='" + givenCustomer.getCustomerCountry() + "' AND Division='" + givenCustomer.getCustomerState() + "'";
            JDBC.makePreparedStatement(divQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(divQuery);
            ResultSet rs = checkQuery.getResultSet();
            while (rs.next()) {
                division = rs.getInt("Division_ID");
            }
            String addCustomerQuery = "INSERT INTO customers VALUES (" + givenCustomer.getCustomerID() + ", '" + givenCustomer.getCustomerName() + "', '" + givenCustomer.getCustomerAddress() + "', '" + givenCustomer.getCustomerPostal() + "', '" + givenCustomer.getCustomerPhone() + "', CURRENT_TIMESTAMP, 'program', CURRENT_TIMESTAMP, 'program', " + division + ")";
            JDBC.makePreparedStatement(addCustomerQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addCustomerQuery);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Makes changes to the SQL database entry that matches the passed Customer
     * @param givenCustomer The changed Customer to overwrite the existing entry within the SQL database
     * @return a boolean indication if the SQL database entry was successfully updated
     * @throws SQLException thrown in case of SQL database issues
     */
    public static boolean modifyCustomerInDatabase(Customer givenCustomer) throws SQLException {
        try {
            int division = 0;
            String divQuery = "SELECT Division_ID from first_level_divisions join countries on first_level_divisions.Country_ID=countries.Country_ID WHERE Country='" + givenCustomer.getCustomerCountry() + "' AND Division='" + givenCustomer.getCustomerState() + "'";
            JDBC.makePreparedStatement(divQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(divQuery);
            ResultSet rs = checkQuery.getResultSet();
            while (rs.next()) {
                division = rs.getInt("Division_ID");
            }

            String addCustomerQuery = "UPDATE customers JOIN first_level_divisions on customers.Division_ID=first_level_divisions.Division_ID join countries on countries.Country_ID=first_level_divisions.Country_ID SET Customer_Name='" + givenCustomer.getCustomerName() + "', Address='" + givenCustomer.getCustomerAddress() + "', Postal_Code='" + givenCustomer.getCustomerPostal() + "', Phone='" + givenCustomer.getCustomerPhone() + "', customers.Last_Update=CURRENT_TIMESTAMP, customers.Last_Updated_By='" + loginForm.loggedInUser + "', customers.Division_ID=" + division + " WHERE Customer_ID=" + givenCustomer.getCustomerID();
            JDBC.makePreparedStatement(addCustomerQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addCustomerQuery);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Deletes the row entry in the SQL database customers table that matches the passed Customer
     *
     * @param givenCustomer The selected Customer to have its existing entry within the SQL database be deleted
     * @return a boolean indication if the SQL database entry was successfully deleted
     */
    public static boolean deleteCustomerFromDatabase(Customer givenCustomer) {
        try {
            String deleteCustomerQuery = "DELETE FROM customers WHERE Customer_ID=" + givenCustomer.getCustomerID() + "";
            JDBC.makePreparedStatement(deleteCustomerQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(deleteCustomerQuery);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public int getCustomerID() {
        return CustomerID;
    }

    /**
     *
     * @param CustomerID
     */
    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return CustomerName;
    }

    /**
     *
     * @param CustomerName
     */
    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone() {
        return CustomerPhone;
    }

    /**
     *
     * @param CustomerPhone
     */
    public void setCustomerPhone(String CustomerPhone) {
        this.CustomerPhone = CustomerPhone;
    }

    /**
     *
     * @return
     */
    public String getCustomerCountry() {
        return CustomerCountry;
    }

    /**
     *
     * @param CustomerCountry
     */
    public void setCustomerCountry(String CustomerCountry) {
        this.CustomerCountry = CustomerCountry;
    }

    /**
     *
     * @return
     */
    public String getCustomerPostal() {
        return CustomerPostal;
    }

    /**
     *
     * @param CustomerPostal
     */
    public void setCustomerPostal(String CustomerPostal) {
        this.CustomerPostal = CustomerPostal;
    }

    /**
     *
     * @return
     */
    public String getCustomerState() {
        return CustomerState;
    }

    /**
     *
     * @param CustomerState
     */
    public void setCustomerState(String CustomerState) {
        this.CustomerState = CustomerState;
    }

    /**
     *
     * @return
     */
    public String getCustomerAddress() {
        return CustomerAddress;
    }

    /**
     *
     * @param CustomerAddress
     */
    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     *
     * @return
     */
    public static ObservableList<Customer> customerPopulation() {
        return allCustomers;
    }

    /**
     *
     * @param newCustomer
     */
    public static void addCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }

    /**
     *
     * @param index
     * @param selectedCustomer
     * @throws SQLException thrown in case of SQL database issues
     */
    public static void updateCustomer(int index, Customer selectedCustomer) throws SQLException {
        for (Customer customer : allCustomers) {
            if (customer.getCustomerID() == index) {
                System.out.println(allCustomers.indexOf(customer));
                int f = allCustomers.indexOf(customer);
                System.out.println(allCustomers.indexOf(customer));
                allCustomers.set(f, selectedCustomer);
            }
        }
    }

    /**
     *
     * @param selectedCustomer
     * @return
     * @throws SQLException thrown in case of SQL database issues
     */
    public static boolean deleteCustomer(Customer selectedCustomer) throws SQLException {
        if (allCustomers.contains(selectedCustomer)) {
            if (usedIDs.contains(selectedCustomer.getCustomerID())) {
                usedIDs.clear();
                int CustomerID = 0;
                String logQuery = "SELECT Customer_ID FROM customers join first_level_divisions on customers.Division_ID=first_level_divisions.Division_ID join countries on countries.Country_ID=first_level_divisions.Country_ID";
                JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
                Statement checkQuery = JDBC.getPreparedStatement();
                checkQuery.execute(logQuery);
                ResultSet rs2 = checkQuery.getResultSet();
                while (rs2.next()) {
                    CustomerID = rs2.getInt("Customer_ID");
                    Customer.usedIDs.add(CustomerID);
                }
                if (!Customer.usedIDs.contains(0)) {
                    Customer.usedIDs.add(0);
                }
            }
            allCustomers.remove(selectedCustomer);
            return true;
        }
        return false;
    }
}
