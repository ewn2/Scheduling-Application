package sample.Controller;

import javafx.event.ActionEvent;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewReport implements Initializable {
    public Button viewReportCancelButton;
    public TableView reportMonthBreakdownTable;
    public TableColumn reportMonthBreakdownTableMonthCol;
    public TableColumn reportMonthBreakdownTableTotalCol;
    public TableView reportTypeBreakdownTable;
    public TableColumn reportTypeBreakdownTableTypeCol;
    public TableColumn reportTypeBreakdownTableTotalCol;
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
    public TableView<Customer> CustomerAppointmentTable;
    public TableColumn<Customer, String> CustomerAppointmentTableAppointmentIDCol;
    public TableColumn<Customer, String> CustomerAppointmentTableTitleCol;
    public TableColumn<Customer, String> CustomerAppointmentTableDescriptionCol;
    public TableColumn<Customer, String> CustomerAppointmentTableLocationCol;
    public TableColumn<Customer, String> CustomerAppointmentTableContactCol;
    public TableColumn<Customer, String> CustomerAppointmentTableTypeCol;
    public TableColumn<Customer, String> CustomerAppointmentTableStartDateAndTimeCol;
    public TableColumn<Customer, String> CustomerAppointmentTableEndDateAndTimeCol;
    public TableColumn<Customer, Integer> CustomerAppointmentTableCustomerIDCol;

    public void onViewReportCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }


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

    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/View/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onSelectContactComboAction(ActionEvent actionEvent) {
        int selectedContact = Integer.parseInt(selectContactCombo.getValue().toString());
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
        ContactAppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
        ContactAppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
        ContactAppointmentTable.getSortOrder().add(ContactAppointmentTableAppointmentIDCol);
        ContactAppointmentTable.sort();
    }

    public void onSelectCustomerComboAction(ActionEvent actionEvent) {
        int selectedCustomer = Integer.parseInt(selectCustomerCombo.getValue().toString());
        try {
            ContactAppointmentTable.setItems(Appointment.ContactAppointmentPopulation(selectedCustomer));
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
        CustomerAppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
        CustomerAppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
        CustomerAppointmentTable.getSortOrder().add(CustomerAppointmentTableAppointmentIDCol);
        CustomerAppointmentTable.sort();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }
}
