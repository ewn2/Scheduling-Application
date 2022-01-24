package sample.Controller;
/**
 * addCustomer.fxml related Controller
 *
 * @author Erwin Uppal
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.JDBC;
import sample.Model.Customer;
import sample.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddCustomer implements Initializable {
    public TextField addCustomerNameBox;
    public TextField addCustomerPhoneBox;
    public ComboBox addCustomerCountryCombo;
    public TextField addCustomerPostalBox;
    public ComboBox addCustomerStateCombo;
    public TextField addCustomerAddressBox;
    public Button addCustomerSaveButton;
    public Button addCustomerCancelButton;
    public TextArea errorMessageBox;
    public TextField addCustomerIDBox;

    /**
     * Initializer for add Customer screen, loads in Combo Boxes with existing values from database
     *
     * @param url            resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateCountry();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
    }

    /**
     * Populates the Country ComboBox from SQL database for User selection
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateCountry() throws SQLException {
        String logQuery = "SELECT * FROM countries";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Country");
            addCustomerCountryCombo.getItems().add(ire);
        }
    }

    /**
     * Upon the User updating the Country selection ComboBox, will retrieve the ID value of that Country from the
     * SQL database
     *
     * @param actionEvent User making a selection with the Country ComboBox
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onAddCustomerCountryComboAction(ActionEvent actionEvent) throws SQLException {
        String chosenCountry = (String) addCustomerCountryCombo.getValue();
        String logQuery = "SELECT * FROM countries WHERE Country='" + chosenCountry + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Country_ID");
            populateStates(ire);
        }
    }

    interface exceptionLambda {
        void apple();
    }

    /**
     * Populates the first division State/Province ComboBox from SQL database for User selection based upon all
     * divisions associated with the selected Country_ID value of the option selected in the Country ComboBox
     *
     * @param Country_ID the integer matching the selected Country ComboBox option's Country_ID within the SQL Database
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateStates(String Country_ID) throws SQLException {
        addCustomerStateCombo.getItems().clear();
        String logQuery = "SELECT * FROM first_level_divisions WHERE Country_ID='" + Country_ID + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire2 = rs.getString("Division");
            addCustomerStateCombo.getItems().add(ire2);
        }
    }

    /**
     * Reaction to User pressing Save button, attempts to validate all User input Customer detail values and save them
     * into both the current ObservableList of all Customers and into the connected SQL database
     * <p>
     * Lambda exceptionLambda improved code by removing the need to incorporate individual try and catch blocks for every
     * single User entered String, ComboBox, or Date and Time value. Instead, every value may be placed into an all
     * encompassing try block and the Lambda function can handle every validity check and error message display at once
     * exceptionLambda: Lines 178 through 184
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of SQL database interaction issues
     */
    public void onAddCustomerSaveButtonAction(ActionEvent actionEvent) throws IOException {
        boolean validEntries = true;
        boolean addedCustomer = false;
        try {
            int id = 0;
            String CustomerName = addCustomerNameBox.getText();
            if (CustomerName == null || CustomerName.trim().isEmpty() || CustomerName.length() > 50) {
                throw new Exception("Invalid Name Entry, must not be empty and must be less than 50 characters");
            }
            String CustomerPhone = addCustomerPhoneBox.getText();
            if (CustomerPhone == null || CustomerPhone.trim().isEmpty() || CustomerPhone.length() > 50) {
                throw new Exception("Invalid Phone Entry, must not be empty and must be less than 50 characters");
            }
            try {
                String CustomerCountryComboString = addCustomerCountryCombo.getValue().toString();
            } catch (Exception e) {
                throw new Exception("Invalid Country Entry, must not be empty");
            }
            String CustomerCountry = addCustomerCountryCombo.getValue().toString();
            if (CustomerCountry == null || CustomerCountry.trim().isEmpty()) {
                throw new Exception("Invalid Country Entry, must not be empty");
            }
            String CustomerPostal = addCustomerPostalBox.getText();
            if (CustomerPostal == null || CustomerPostal.trim().isEmpty() || CustomerPostal.length() > 50) {
                throw new Exception("Invalid Postal/Zip Code Entry, must not be empty and must be less than 50 characters");
            }
            try {
                String CustomerCountryComboString = addCustomerStateCombo.getValue().toString();
            } catch (Exception e) {
                throw new Exception("Invalid State/Province Entry, must not be empty");
            }
            String CustomerState = addCustomerStateCombo.getValue().toString();
            if (CustomerState == null || CustomerState.trim().isEmpty()) {
                throw new Exception("Invalid State/Province Entry, must not be empty");
            }
            String CustomerAddress = addCustomerAddressBox.getText();
            if (CustomerAddress == null || CustomerAddress.trim().isEmpty() || CustomerAddress.length() > 100) {
                throw new Exception("Invalid Address Entry, must not be empty and must be less than 100 characters");
            }
        } catch (Exception e) {
            errorMessageBox.setVisible(true);
            AddAppointment.exceptionLambda errorMaker = () -> {
                errorMessageBox.setText("Unknown error has occurred! Possible issues with database connectivity");
            };
            if (e.getMessage() != null) {
                errorMaker = () -> errorMessageBox.setText("Error: " + e.getMessage());
            }
            errorMaker.apple();
            validEntries = false;
        }
        int id = 0;
        String CustomerName = addCustomerNameBox.getText();
        String CustomerPhone = addCustomerPhoneBox.getText();
        String CustomerCountry = addCustomerCountryCombo.getValue().toString();
        String CustomerPostal = addCustomerPostalBox.getText();
        String CustomerState = addCustomerStateCombo.getValue().toString();
        String CustomerAddress = addCustomerAddressBox.getText();
        if (validEntries) {
            try {
                Customer newCustomer = new Customer(id, CustomerName, CustomerPhone, CustomerCountry, CustomerPostal, CustomerState, CustomerAddress);
                newCustomer.setCustomerID(Customer.uniqueCustomerID());
                Customer.addCustomer(newCustomer);
                if (Customer.addCustomerToDatabase(newCustomer)) {
                    addedCustomer = true;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error: Cannot add Customer to database, check connection");
            }
        }

        if (addedCustomer) {
            returnToMainScreen(actionEvent);
        }
    }

    /**
     * Closes the Add Customer view and calls a method to return to the MainForm view
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of FXML file interaction issues
     */
    public void onAddCustomerCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
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

}
