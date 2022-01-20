package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;

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

    public void addAssociatedAppointment(Appointment appointment) {
        associatedAppointments.add(appointment);
    }

    public boolean deleteAssociatedAppointments(Appointment appointment) {
        if (associatedAppointments.contains(appointment)) {
            associatedAppointments.remove(appointment);
            return true;
        }
        else return false;
    }

    public ObservableList<Appointment> getAssociatedAppointments() {
        return associatedAppointments;
    }

    private static int customerIDGenerated = -1;

    public static Vector<Integer> usedIDs = new Vector<>(0);

    public static int uniqueCustomerID() {
        customerIDGenerated++;
        while (usedIDs.contains(customerIDGenerated)) {
            customerIDGenerated++;
        }
        usedIDs.add(customerIDGenerated);
        return customerIDGenerated;
    }

    public Customer(int CustomerID, String CustomerName, String CustomerPhone, String CustomerCountry, String CustomerPostal, String CustomerState, String CustomerAddress) {
        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.CustomerPhone = CustomerPhone;
        this.CustomerCountry = CustomerCountry;
        this.CustomerPostal = CustomerPostal;
        this.CustomerState = CustomerState;
        this.CustomerAddress = CustomerAddress;
    }

    public static boolean addCustomerToDatabase(Customer givenCustomer) throws SQLException {
        try {
            int division = 0;
            String divQuery = "SELECT Division_ID from first_level_divisions join countries on first_level_divisions.Country_ID=countries.Country_ID WHERE Country='"+givenCustomer.getCustomerCountry()+"' AND Division='"+givenCustomer.getCustomerState()+"'";
            JDBC.makePreparedStatement(divQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(divQuery);
            ResultSet rs = checkQuery.getResultSet();
            while (rs.next()) {
                division = rs.getInt("Division_ID");
            }
            String addCustomerQuery = "INSERT INTO customers VALUES ("+givenCustomer.getCustomerID()+", '"+givenCustomer.getCustomerName()+"', '"+givenCustomer.getCustomerAddress()+"', '"+givenCustomer.getCustomerPostal()+"', '"+givenCustomer.getCustomerPhone()+"', CURRENT_TIMESTAMP, 'program', CURRENT_TIMESTAMP, 'program', "+division+")";
            JDBC.makePreparedStatement(addCustomerQuery, JDBC.getConnection());
            Statement checkQuery2 = JDBC.getPreparedStatement();
            checkQuery2.execute(addCustomerQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteCustomerFromDatabase(Customer givenCustomer) {
        try {
            String deleteCustomerQuery = "DELETE FROM customers WHERE Customer_ID="+givenCustomer.getCustomerID()+"";
            JDBC.makePreparedStatement(deleteCustomerQuery, JDBC.getConnection());
            Statement checkQuery = JDBC.getPreparedStatement();
            checkQuery.execute(deleteCustomerQuery);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }



    public int getCustomerID() {
        return CustomerID;
    }
    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }
    public void setCustomerPhone(String CustomerPhone) {
        this.CustomerPhone = CustomerPhone;
    }

    public String getCustomerCountry() {
        return CustomerCountry;
    }
    public void setCustomerCountry(String CustomerCountry) {
        this.CustomerCountry = CustomerCountry;
    }

    public String getCustomerPostal() {
        return CustomerPostal;
    }
    public void setCustomerPostal(String CustomerPostal) {
        this.CustomerPostal = CustomerPostal;
    }

    public String getCustomerState() {
        return CustomerState;
    }
    public void setCustomerState(String CustomerState) {
        this.CustomerState = CustomerState;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }
    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<Customer> customerPopulation() {
        return allCustomers;
    }
    public static void addCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }
    public static Customer lookupCustomer(int customerID) {
        for (Customer customer : allCustomers) {
            if (customer.getCustomerID() == customerID) {
                return customer;
            }
        }
        return null;
    }
    public static ObservableList<Customer> lookupCustomer(String customerName) {
        ObservableList<Customer> matches = FXCollections.observableArrayList();
        for (Customer customer : allCustomers) {
            if (customer.getCustomerName().toUpperCase().contains((String.valueOf(customer)).toUpperCase())) {
                matches.add(customer);
            }
        }
        return matches;
    }
    public static void updateCustomer(int index, Customer selectedCustomer) {
        allCustomers.set(index, selectedCustomer);
    }
    public static boolean deleteCustomer(Customer selectedCustomer) {
        if (allCustomers.contains(selectedCustomer)) {
            if (usedIDs.contains(selectedCustomer.getCustomerID())) {
                usedIDs.remove(selectedCustomer.getCustomerID());
            }
            allCustomers.remove(selectedCustomer);
            return true;
        }
        return false;
    }
}
