package sample.Controller;

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

public class AddCustomer implements Initializable {
    public TextField addCustomerNameBox;
    public TextField addCustomerPhoneBox;
    public ComboBox addCustomerCountryCombo;
    public TextField addCustomerPostalBox;
    public ComboBox addCustomerStateCombo;
    public TextField addCustomerCityBox;
    public TextField addCustomerAddressBox;
    public Button addCustomerSaveButton;
    public Button addCustomerCancelButton;
    public TextArea errorMessageBox;
    public TextField addCustomerIDBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateCountry();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
    }

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

    public void onAddCustomerSaveButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    public void onAddCustomerCancelButtonAction(ActionEvent actionEvent) throws IOException {
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
