package sample.Controller;
/**
 * modifyCustomer.fxml related Controller
 *
 * @author Erwin Uppal
 */

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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyCustomer implements Initializable {
    public TextField ModifyCustomerNameBox;
    public TextField ModifyCustomerPhoneBox;
    public ComboBox ModifyCustomerCountryCombo;
    public TextField ModifyCustomerPostalBox;
    public ComboBox ModifyCustomerStateCombo;
    public TextField ModifyCustomerAddressBox;
    public Button ModifyCustomerSaveButton;
    public Button ModifyCustomerCancelButton;
    public TextArea errorMessageBox;
    public TextField ModifyCustomerIDBox;

    private Customer customerToModify = null;

    /**
     * Initializer for modify Customer screen, loads in Combo Boxes with existing values from database
     * and prepopulates all ComboBoxes and TextBoxes with existing values associated with the Customer
     * Instance to be modified
     *
     * @param url            resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerToModify = MainForm.customerPasser;
        ModifyCustomerIDBox.setText(String.valueOf(customerToModify.getCustomerID()));
        ModifyCustomerNameBox.setText(String.valueOf(customerToModify.getCustomerName()));
        ModifyCustomerPhoneBox.setText(String.valueOf(customerToModify.getCustomerPhone()));
        ModifyCustomerCountryCombo.setValue(String.valueOf(customerToModify.getCustomerCountry()));
        try {
            populateCountry();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
        try {
            keepStates();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
        ModifyCustomerPostalBox.setText(String.valueOf(customerToModify.getCustomerPostal()));
        ModifyCustomerStateCombo.setValue(String.valueOf(customerToModify.getCustomerState()));
        ModifyCustomerAddressBox.setText(String.valueOf(customerToModify.getCustomerAddress()));
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
            ModifyCustomerCountryCombo.getItems().add(ire);
        }
    }

    /**
     * Upon the User updating the Country selection ComboBox, will retrieve the ID value of that Country from the
     * SQL database
     *
     * @param actionEvent User making a selection with the Country ComboBox
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onModifyCustomerCountryComboAction(ActionEvent actionEvent) throws SQLException {
        String chosenCountry = (String) ModifyCustomerCountryCombo.getValue();
        ModifyCustomerStateCombo.getItems().clear();
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

    /**
     * Maintains the ComboBox of States so that the preexisting Country's associated States are loaded in
     * during initialization to avoid conflicting State and Country values or leaving ComboBox empty
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void keepStates() throws SQLException {
        String chosenCountry = (String) ModifyCustomerCountryCombo.getValue();
        ModifyCustomerStateCombo.getItems().clear();
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

    /**
     * Populates the first division State/Province ComboBox from SQL database for User selection based upon all
     * divisions associated with the selected Country_ID value of the option selected in the Country ComboBox
     *
     * @param Country_ID the integer matching the selected Country ComboBox option's Country_ID within the SQL Database
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateStates(String Country_ID) throws SQLException {
        String logQuery = "SELECT * FROM first_level_divisions WHERE Country_ID='" + Country_ID + "'";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire2 = rs.getString("Division");
            ModifyCustomerStateCombo.getItems().add(ire2);
        }
    }

    /**
     * Interface for error message handling Lambda
     * Lambda exceptionLambda improved code by removing the need to incorporate individual try and catch blocks for every
     * single User entered String, ComboBox, or Date and Time value. Instead, every value may be placed into an all
     * encompassing try block and the Lambda function can handle every validity check and error message display at once
     * exceptionLambda: Lines 218 through 224
     */
    interface exceptionLambda {
        void apple();
    }

    /**
     * Reaction to User pressing Save button, attempts to validate all User input Customer detail values and save them
     * into both the current ObservableList of all Customers and into the connected SQL database on top of the existing
     * entries, overwriting them
     * <p>
     * Lambda exceptionLambda improved code by removing the need to incorporate individual try and catch blocks for every
     * single User entered String, ComboBox, or Date and Time value. Instead, every value may be placed into an all
     * encompassing try block and the Lambda function can handle every validity check and error message display at once
     * exceptionLambda: Lines 218 through 224
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of SQL database interaction issues
     */
    public void onModifyCustomerSaveButtonAction(ActionEvent actionEvent) throws IOException {
        boolean validEntries = true;
        boolean addedCustomer = false;
        try {
            int id = customerToModify.getCustomerID();
            String CustomerName = ModifyCustomerNameBox.getText();
            if (CustomerName == null || CustomerName.trim().isEmpty() || CustomerName.length() > 50) {
                throw new Exception("Invalid Name Entry, must not be empty and must be less than 50 characters");
            }
            String CustomerPhone = ModifyCustomerPhoneBox.getText();
            if (CustomerPhone == null || CustomerPhone.trim().isEmpty() || CustomerPhone.length() > 50) {
                throw new Exception("Invalid Phone Entry, must not be empty and must be less than 50 characters");
            }
            try {
                String CustomerCountryComboString = ModifyCustomerCountryCombo.getValue().toString();
            } catch (Exception e) {
                throw new Exception("Invalid Country Entry, must not be empty");
            }
            String CustomerCountry = ModifyCustomerCountryCombo.getValue().toString();
            if (CustomerCountry == null || CustomerCountry.trim().isEmpty()) {
                throw new Exception("Invalid Country Entry, must not be empty");
            }
            String CustomerPostal = ModifyCustomerPostalBox.getText();
            if (CustomerPostal == null || CustomerPostal.trim().isEmpty() || CustomerPostal.length() > 50) {
                throw new Exception("Invalid Postal/Zip Code Entry, must not be empty and must be less than 50 characters");
            }
            try {
                String CustomerCountryComboString = ModifyCustomerStateCombo.getValue().toString();
            } catch (Exception e) {
                throw new Exception("Invalid State/Province Entry, must not be empty");
            }
            String CustomerState = ModifyCustomerStateCombo.getValue().toString();
            if (CustomerState == null || CustomerState.trim().isEmpty()) {
                throw new Exception("Invalid State/Province Entry, must not be empty");
            }
            String CustomerAddress = ModifyCustomerAddressBox.getText();
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
        int id = customerToModify.getCustomerID();
        String CustomerName = ModifyCustomerNameBox.getText();
        String CustomerPhone = ModifyCustomerPhoneBox.getText();
        String CustomerCountry = ModifyCustomerCountryCombo.getValue().toString();
        String CustomerPostal = ModifyCustomerPostalBox.getText();
        String CustomerState = ModifyCustomerStateCombo.getValue().toString();
        String CustomerAddress = ModifyCustomerAddressBox.getText();
        if (validEntries) {
            try {
                Customer newCustomer = new Customer(id, CustomerName, CustomerPhone, CustomerCountry, CustomerPostal, CustomerState, CustomerAddress);
                newCustomer.setCustomerID(id);
                Customer.updateCustomer(id, newCustomer);
                if (Customer.modifyCustomerInDatabase(newCustomer)) {
                    addedCustomer = true;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error: Cannot modify Customer in database, check connection");
            }
        }

        if (addedCustomer) {
            returnToMainScreen(actionEvent);
        }
    }

    /**
     * Closes the Modify Customer view and calls a method to return to the MainForm view
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of FXML file interaction issues
     */
    public void onModifyCustomerCancelButtonAction(ActionEvent actionEvent) throws IOException {
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
