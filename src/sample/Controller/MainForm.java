package sample.Controller;
/**
 * MainForm.fxml related Controller
 *
 * @author Erwin Uppal
 */
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Model.Appointment;
import sample.Model.Customer;
import sample.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.ResourceBundle;
import java.time.*;

public class MainForm implements Initializable {
    public Label mainScreenTitleLabel;
    public Button ExitButton;
    public AnchorPane CustomerAnchorPane;
    public TableView<Customer> CustomerTable;
    public TableColumn<Customer, Integer> customerTableCustomerIDCol;
    public TableColumn<Customer, String> customerTableCustomerNameCol;
    public Button customerDeleteButton;
    public Button customerModifyButton;
    public Button customerAddButton;
    public AnchorPane AppointmentAnchorPane;
    public TableView<Appointment> AppointmentTable;
    public TableColumn<Appointment, Integer> AppointmentTableAppointmentIDCol;
    public TableColumn<Appointment, String> AppointmentTableTitleCol;
    public TableColumn<Appointment, String> AppointmentTableDescriptionCol;
    public TableColumn<Appointment, String> AppointmentTableLocationCol;
    public TableColumn<Appointment, String> AppointmentTableContactCol;
    public TableColumn<Appointment, String> AppointmentTableTypeCol;
    public TableColumn<Appointment, String> AppointmentTableStartDateAndTimeCol;
    public TableColumn<Appointment, String> AppointmentTableEndDateAndTimeCol;
    public TableColumn<Appointment, Integer> AppointmentTableCustomerIDCol;
    public TableColumn<Appointment, Integer> AppointmentTableUserIDCol;
    public Button AppointmentDeleteButton;
    public Button AppointmentModifyButton;
    public Button AppointmentAddButton;
    public RadioButton appointmentsWeeklyRadio;
    public ToggleGroup WeekOrMonth;
    public RadioButton appointmentsMonthlyRadio;
    public TextArea errorMessageBox;
    public Button confirmButton;
    public Button denyButton;
    public Button reportsButton;
    public DatePicker WeekOrMonthSelect;
    public RadioButton appointmentsNoFilterRadio;
    public TableColumn<Customer, String> customerTableCustomerPhoneCol;
    public TableColumn<Customer, String> customerTableCustomerCountryCol;
    public TableColumn<Customer, String> customerTableCustomerPostalCol;
    public TableColumn<Customer, String> customerTableCustomerStateCol;
    public TableColumn<Customer, String> customerTableCustomerAddressCol;
    public Label timeZoneLabel;

    /**
     * Initializer for Main form screen, checks User's system information on timezone and populates TableViews of all
     * Appointments and Customers with start and end times adjusted to be shown at the User's local time
     * @param url resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UpcomingAppointmentWarning();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        timeZoneLabel.setText("User's Timezone: " + loginForm.usersTimezone);
        CustomerTable.setItems(Customer.customerPopulation());
        customerTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerTableCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerTableCustomerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("CustomerPhone"));
        customerTableCustomerCountryCol.setCellValueFactory(new PropertyValueFactory<>("CustomerCountry"));
        customerTableCustomerPostalCol.setCellValueFactory(new PropertyValueFactory<>("CustomerPostal"));
        customerTableCustomerStateCol.setCellValueFactory(new PropertyValueFactory<>("CustomerState"));
        customerTableCustomerAddressCol.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
        customerTableCustomerIDCol.setSortType(TableColumn.SortType.ASCENDING);
        CustomerTable.getSortOrder().add(customerTableCustomerIDCol);
        CustomerTable.sort();
        try {
            AppointmentTable.setItems(Appointment.appointmentPopulation());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        AppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        AppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
        AppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
        AppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
        AppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
        AppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
        AppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
        AppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
        AppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
        AppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
        AppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
        AppointmentTable.getSortOrder().add(AppointmentTableAppointmentIDCol);
        AppointmentTable.sort();
    }

    /**
     * Shows a detailed message to the User in case of any Appointments upcoming for that User within 15 minutes of
     * that User logging into the application, otherwise informs them there are no upcoming Appointments
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void UpcomingAppointmentWarning() throws SQLException {
        boolean UserHasAppointment = false;
        int loggedInUser = User.getUser_ID();

        ObservableList<Appointment> existingUserAppointments = FXCollections.observableArrayList();
        for (Appointment CheckAppointment : Appointment.appointmentPopulation()) {
            if (CheckAppointment.getAppointmentUserID() == loggedInUser) {
                existingUserAppointments.add(CheckAppointment);
            }
        }
        ObservableList<Appointment> upcomingUserAppointments = FXCollections.observableArrayList();
        for (Appointment CheckAppointment : existingUserAppointments) {
            DateTimeFormatter CheckFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime existingAppointment = LocalDateTime.parse(CheckAppointment.getAppointmentStartDateTime(), CheckFormatter);
            if (ChronoUnit.MINUTES.between(LocalDateTime.now(), existingAppointment) <= 15 && ChronoUnit.MINUTES.between(LocalDateTime.now(), existingAppointment) >= 0) {
                upcomingUserAppointments.add(CheckAppointment);
                UserHasAppointment = true;
            }
        }
        if (UserHasAppointment) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Upcoming Appointments within 15 minutes of Login!");
            for (Appointment upcoming : upcomingUserAppointments) {
                errorMessageBox.appendText("\n ID: " + upcoming.getAppointmentID() + " at " + upcoming.getAppointmentStartDateTime());
            }
        } else {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("No appointments upcoming within 15 minutes of Login");
        }
    }

    /**
     * Reaction to User clicking Exit button, closes the application
     * @param actionEvent User initiating button press
     */
    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public Customer selectedCustomer = null;
    public Appointment selectedAppointment = null;

    public static Customer customerPasser = null;
    public static Appointment appointmentPasser = null;

    /**
     * Passes the currently selected Customer from the Customer TableView to be deleted from the database, but
     * first proffers a deletion confirmation to the User. Will not allow the deletion of a Customer with existing
     * Appointments within the database and informs the User of that restriction if attempted.
     * @param actionEvent User initiating button press
     */
    public void onCustomerDeleteButtonAction(ActionEvent actionEvent) {
        selectedAppointment = null;
        selectedCustomer = null;
        selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            ObservableList<Appointment> emptyCheck = FXCollections.observableArrayList();
            emptyCheck = selectedCustomer.getAssociatedAppointments();
            errorMessageBox.setVisible(true);
            if (emptyCheck.toString() == "[]") {
                errorMessageBox.setText("Delete this Customer?");
                confirmButton.setVisible(true);
                denyButton.setVisible(true);
            } else {
                errorMessageBox.setText("You may not delete a Customer with existing Appointments");
            }
        }
    }

    /**
     * Passes the User's currently selected Customer instance from the TableView to the ModifyCustomer View screen
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onCustomerModifyButtonAction(ActionEvent actionEvent) throws IOException {
        customerPasser = null;
        customerPasser = CustomerTable.getSelectionModel().getSelectedItem();
        if (customerPasser != null) {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/ModifyCustomer.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Opens the AddCustomer screen where the User may populate the details of a new Customer to be added to the
     * list of all Customers
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onCustomerAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Passes the currently selected Appointment from the Appointment TableView to be deleted from the database, but
     * first proffers a deletion confirmation to the User.
     * @param actionEvent User initiating button press
     */
    public void onAppointmentDeleteButtonAction(ActionEvent actionEvent) {
        selectedAppointment = null;
        selectedCustomer = null;
        selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Delete this Appointment?");
            confirmButton.setVisible(true);
            denyButton.setVisible(true);
        }
    }

    /**
     * Passes the User's currently selected Appointment instance from the TableView to the ModifyAppointment View screen
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onAppointmentModifyButtonAction(ActionEvent actionEvent) throws IOException {
        appointmentPasser = null;
        appointmentPasser = AppointmentTable.getSelectionModel().getSelectedItem();
        if (appointmentPasser != null) {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/modifyAppointment.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Opens the AddAppointment screen where the User may populate the details of a new Appointment to be added to the
     * list of all Appointments
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onAppointmentAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Confirms a deletion operation of a selected Customer or Appointment, and shows a confirmation message to the User
     * confirming the deletion along with details of the deleted Customer/Appointment
     * @param actionEvent User initiating button press
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onConfirmButtonAction(ActionEvent actionEvent) throws SQLException {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
        if (selectedCustomer != null) {
            if (Customer.deleteCustomerFromDatabase(selectedCustomer)) {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Deleted Customer with ID: " + selectedCustomer.getCustomerID());
                Customer.deleteCustomer(selectedCustomer);
            } else {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error connecting to database, check connection");
            }
        }
        if (selectedAppointment != null) {
            if (Appointment.deleteAppointmentFromDatabase(selectedAppointment)) {
                Appointment.deleteAppointment(selectedAppointment);
                for (Customer customer : Customer.customerPopulation()) {
                    if (customer.getCustomerID() == selectedAppointment.getAppointmentCustomerID()) {
                        errorMessageBox.setVisible(true);
                        errorMessageBox.setText("Deleted Appointment with ID: " + selectedAppointment.getAppointmentID() + " and type: " + selectedAppointment.getAppointmentType());
                        customer.deleteAssociatedAppointments(selectedAppointment);
                        if (appointmentsWeeklyRadio.isSelected()) {
                            FilterByWeek();
                        }
                        if (appointmentsMonthlyRadio.isSelected()) {
                            FilterByMonth();
                        }
                    }
                }
            } else {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error connecting to database, check connection");
            }
        }
        selectedCustomer = null;
        selectedAppointment = null;
    }

    /**
     * Ends the deletion routine of a Customer or Appointment without performing any deletions
     * @param actionEvent User initiating button press
     */
    public void onDenyButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
        selectedCustomer = null;
        selectedAppointment = null;
    }

    /**
     * When called, loads in viewReport.fxml as the current User-facing scene
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onReportsButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/ViewReport.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Reacts to User selecting the Weekly or Monthly filter Radio Buttons and calls their respective filter methods
     * @param actionEvent User initiating radio button selection
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onWeekOrMonthSelectAction(ActionEvent actionEvent) throws SQLException {
        if (appointmentsWeeklyRadio.isSelected()) {
            FilterByWeek();
        }
        if (appointmentsMonthlyRadio.isSelected()) {
            FilterByMonth();
        }
    }

    /**
     * Repopulates the Appointment TableView with all Appointments, no filter applied
     * @param actionEvent User initiating radio button selection
     */
    public void onAppointmentsNoFilterRadioAction(ActionEvent actionEvent) {
        try {
            AppointmentTable.setItems(Appointment.appointmentPopulation());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        AppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        AppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
        AppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
        AppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
        AppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
        AppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
        AppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
        AppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
        AppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
        AppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
        AppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
        AppointmentTable.getSortOrder().add(AppointmentTableAppointmentIDCol);
        AppointmentTable.sort();
    }

    /**
     * Upon selection of the Weekly Radio Button filter, calls the FilterByWeek() method
     * @param actionEvent User initiating radio button selection
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onAppointmentsWeeklyRadioAction(ActionEvent actionEvent) throws SQLException {
        FilterByWeek();
    }

    /**
     * Checks the value of the WeekOrMonthSelect DatePicker and repopulates the Appointment TableView with all Appointments
     * in the week following that date, inclusive to the date
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void FilterByWeek() throws SQLException {
        if (WeekOrMonthSelect.getValue() != null) {
            ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
            LocalDate wantedWeek = WeekOrMonthSelect.getValue();
            for (Appointment appointment : Appointment.appointmentPopulation()) {
                String aptStart = appointment.getAppointmentStartDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate handler1 = LocalDate.parse(aptStart.substring(0, 10), formatter);
                if ((ChronoUnit.DAYS.between(wantedWeek.atStartOfDay(ZoneId.systemDefault()), handler1.atStartOfDay(ZoneId.systemDefault())) <= 7) && (ChronoUnit.DAYS.between(wantedWeek.atStartOfDay(ZoneId.systemDefault()), handler1.atStartOfDay(ZoneId.systemDefault())) >= 0)) {
                    weekAppointments.add(appointment);
                }
            }

            AppointmentTable.setItems(weekAppointments);
            AppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
            AppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
            AppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
            AppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
            AppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
            AppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
            AppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
            AppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
            AppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
            AppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
            AppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
            AppointmentTable.getSortOrder().add(AppointmentTableAppointmentIDCol);
            AppointmentTable.sort();
        }
    }

    /**
     * Upon selection of the Monthly Radio Button filter, calls the FilterByMonth() method
     * @param actionEvent User initiating radio button selection
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void onAppointmentsMonthlyRadioAction(ActionEvent actionEvent) throws SQLException {
        FilterByMonth();
    }

    /**
     * Checks the value of the WeekOrMonthSelect DatePicker and repopulates the Appointment TableView with all Appointments
     * occurring in the month of the same name as the value selected
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void FilterByMonth() throws SQLException {
        if (WeekOrMonthSelect.getValue() != null) {
            ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
            LocalDate wantedMonth = WeekOrMonthSelect.getValue();
            for (Appointment appointment : Appointment.appointmentPopulation()) {
                String aptStart = appointment.getAppointmentStartDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate handler1 = LocalDate.parse(aptStart.substring(0, 10), formatter);
                if (wantedMonth.getMonth() == handler1.getMonth()) {
                    monthAppointments.add(appointment);
                }
            }

            AppointmentTable.setItems(monthAppointments);
            AppointmentTableAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
            AppointmentTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentTitle"));
            AppointmentTableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDesc"));
            AppointmentTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentLocation"));
            AppointmentTableContactCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentContact"));
            AppointmentTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
            AppointmentTableStartDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentStartDateTime"));
            AppointmentTableEndDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentEndDateTime"));
            AppointmentTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentCustomerID"));
            AppointmentTableUserIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentUserID"));
            AppointmentTableAppointmentIDCol.setSortType(TableColumn.SortType.ASCENDING);
            AppointmentTable.getSortOrder().add(AppointmentTableAppointmentIDCol);
            AppointmentTable.sort();
        }
    }
}
