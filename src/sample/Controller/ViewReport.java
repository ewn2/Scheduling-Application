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
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewReport implements Initializable {
    public Button viewReportCancelButton;
    public TableView<theMonth> reportMonthBreakdownTable;
    public TableColumn<theMonth, String> reportMonthBreakdownTableMonthCol;
    public TableColumn<theMonth, Integer> reportMonthBreakdownTableTotalCol;
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

    public void onViewReportCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    public static class theMonth {
        public String monthName = null;
        public int TotalApps = 0;

        public theMonth(String monthName, int TotalApps) {
            this.monthName = monthName;
            this.TotalApps = TotalApps;
        }
        public String getMonthName() {
            return monthName;
        }
        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }
        public int getTotalApps() {
            return TotalApps;
        }
        public void setTotalApps(int TotalApps) {
            this.TotalApps = TotalApps;
        }
    }

    private static ObservableList<theMonth> allMonths = FXCollections.observableArrayList();

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
        ContactAppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
        ContactAppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
        ContactAppointmentTable.getSortOrder().add(ContactAppointmentTableAppointmentIDCol);
        ContactAppointmentTable.sort();
    }

    public void onSelectCustomerComboAction(ActionEvent actionEvent) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allMonths.clear();
        try {
            populateMonths();
        } catch (SQLException throwables) {
            System.out.println("Error loading Appointments by Month from Database");
        }
        reportMonthBreakdownTable.setItems(allMonths);
        reportMonthBreakdownTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("monthName"));
        reportMonthBreakdownTableTotalCol.setCellValueFactory(new PropertyValueFactory<>("TotalApps"));
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
