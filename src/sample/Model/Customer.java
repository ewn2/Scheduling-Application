package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private int CustomerID;
    private String CustomerName;
    private String CustomerPhone;
    private String CustomerCountry;
    private String CustomerPostal;
    private String CustomerState;
    private String CustomerCity;
    private String CustomerAddress;

    private static int customerIDGenerated = -1;

    public static int uniqueCustomerID() {
        customerIDGenerated++;
        return customerIDGenerated;
    }

    public Customer(int CustomerID, String CustomerName, String CustomerPhone, String CustomerCountry, String CustomerPostal, String CustomerState, String CustomerCity, String CustomerAddress) {
        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.CustomerPhone = CustomerPhone;
        this.CustomerCountry = CustomerCountry;
        this.CustomerPostal = CustomerPostal;
        this.CustomerState = CustomerState;
        this.CustomerCity = CustomerCity;
        this.CustomerAddress = CustomerAddress;
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

    public String getCustomerCity() {
        return CustomerCity;
    }
    public void setCustomerCity(String CustomerCity) {
        this.CustomerCity = CustomerCity;
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
            allCustomers.remove(selectedCustomer);
            return true;
        }
        return false;
    }
}
