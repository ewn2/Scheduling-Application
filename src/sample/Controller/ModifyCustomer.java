package sample.Controller;

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

    public void onModifyCustomerSaveButtonAction(ActionEvent actionEvent) throws IOException {
        boolean validEntries = true;
        boolean addedCustomer = false;
        try {
            int id = 0;
            String CustomerName = ModifyCustomerNameBox.getText();
            if (CustomerName == null || CustomerName.trim().isEmpty()) {
                throw new Exception();
            }
            String CustomerPhone = ModifyCustomerPhoneBox.getText();
            if (CustomerPhone == null || CustomerPhone.trim().isEmpty()) {
                throw new Exception();
            }
            String CustomerCountry = ModifyCustomerCountryCombo.getValue().toString();
            if (CustomerCountry == null || CustomerCountry.trim().isEmpty()) {
                throw new Exception();
            }
            String CustomerPostal = ModifyCustomerPostalBox.getText();
            if (CustomerPostal == null || CustomerPostal.trim().isEmpty()) {
                throw new Exception();
            }
            String CustomerState = ModifyCustomerStateCombo.getValue().toString();
            if (CustomerState == null || CustomerState.trim().isEmpty()) {
                throw new Exception();
            }
            String CustomerAddress = ModifyCustomerAddressBox.getText();
            if (CustomerAddress == null || CustomerAddress.trim().isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error: Please check all boxes are filled and within the 50 Character limit per Box, 100 for Address");
            validEntries = false;
        }
        int id = 0;
        String CustomerName = ModifyCustomerNameBox.getText();
        String CustomerPhone = ModifyCustomerPhoneBox.getText();
        String CustomerCountry = ModifyCustomerCountryCombo.getValue().toString();
        String CustomerPostal = ModifyCustomerPostalBox.getText();
        String CustomerState = ModifyCustomerStateCombo.getValue().toString();
        String CustomerAddress = ModifyCustomerAddressBox.getText();
        if (validEntries) {
            try {
                id = customerToModify.getCustomerID();
                Customer newCustomer = new Customer(id, CustomerName,CustomerPhone,CustomerCountry,CustomerPostal,CustomerState,CustomerAddress);
                newCustomer.setCustomerID(id);
                Customer.updateCustomer((id - 1) ,newCustomer);
                addedCustomer = true;
            } catch (Exception e) {
                System.out.println(e);
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error: Please check all boxes are filled with valid data");
            }
        }

        if (addedCustomer) {
            returnToMainScreen(actionEvent);
        }
    }

    public void onModifyCustomerCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/View/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
