package sample.Controller;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            if (ChronoUnit.MINUTES.between(LocalDateTime.now(), existingAppointment) <= 15) {
                upcomingUserAppointments.add(CheckAppointment);
                UserHasAppointment = true;
            }
        }
        if (UserHasAppointment) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Upcoming Appointments!");
            for (Appointment upcoming : upcomingUserAppointments) {
                errorMessageBox.appendText("\n ID: " + upcoming.getAppointmentUserID() + " at" + upcoming.getAppointmentStartDateTime());
            }
        }
        else {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("No Appointments Upcoming in next 15 minutes!");
        }

    }

    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public Customer selectedCustomer = null;
    public Appointment selectedAppointment = null;

    public static Customer customerPasser = null;
    public static Appointment appointmentPasser = null;

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

    public void onCustomerAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

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

    public void onAppointmentAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onConfirmButtonAction(ActionEvent actionEvent) throws SQLException {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
        if (selectedCustomer != null) {
            if (Customer.deleteCustomerFromDatabase(selectedCustomer)) {
                Customer.deleteCustomer(selectedCustomer);
            }
            else {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error connecting to database, check connection");
            }
        }
        if (selectedAppointment != null) {
            if (Appointment.deleteAppointmentFromDatabase(selectedAppointment)) {
                Appointment.deleteAppointment(selectedAppointment);
                for (Customer customer : Customer.customerPopulation()) {
                    if (customer.getCustomerID() == selectedAppointment.getAppointmentCustomerID()) {
                        customer.deleteAssociatedAppointments(selectedAppointment);
                        if (appointmentsWeeklyRadio.isSelected()) {
                            FilterByWeek();
                        }
                        if (appointmentsMonthlyRadio.isSelected()) {
                            FilterByMonth();
                        }
                    }
                }
            }
            else {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error connecting to database, check connection");
            }
        }
        selectedCustomer = null;
        selectedAppointment = null;
    }

    public void onDenyButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
        selectedCustomer = null;
        selectedAppointment = null;
    }

    public void onReportsButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/ViewReport.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onWeekOrMonthSelectAction(ActionEvent actionEvent) throws SQLException {
        if (appointmentsWeeklyRadio.isSelected()) {
            FilterByWeek();
        }
        if (appointmentsMonthlyRadio.isSelected()) {
            FilterByMonth();
        }
    }

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


    public void onAppointmentsWeeklyRadioAction(ActionEvent actionEvent) throws SQLException {
        FilterByWeek();
    }
    public void FilterByWeek() throws SQLException {
        if (WeekOrMonthSelect.getValue() != null) {
            ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
            LocalDate wantedWeek = WeekOrMonthSelect.getValue();
            System.out.println(wantedWeek);
            for (Appointment appointment : Appointment.appointmentPopulation()) {
                System.out.println(appointment.getAppointmentStartDateTime());
                String aptStart = appointment.getAppointmentStartDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate handler1 = LocalDate.parse(aptStart.substring(0,10), formatter);
                if ((ChronoUnit.DAYS.between(wantedWeek.atStartOfDay(ZoneId.systemDefault()),handler1.atStartOfDay(ZoneId.systemDefault())) <= 7) && (ChronoUnit.DAYS.between(wantedWeek.atStartOfDay(ZoneId.systemDefault()),handler1.atStartOfDay(ZoneId.systemDefault())) >= 0)) {
                    System.out.println("Added an Appointment: " + appointment.getAppointmentStartDateTime());
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

    public void onAppointmentsMonthlyRadioAction(ActionEvent actionEvent) throws SQLException {
        FilterByMonth();
    }
    public void FilterByMonth() throws SQLException {
        if (WeekOrMonthSelect.getValue() != null) {
            ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
            LocalDate wantedMonth = WeekOrMonthSelect.getValue();
            System.out.println(wantedMonth);
            for (Appointment appointment : Appointment.appointmentPopulation()) {
                System.out.println(appointment.getAppointmentStartDateTime());
                String aptStart = appointment.getAppointmentStartDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate handler1 = LocalDate.parse(aptStart.substring(0,10), formatter);
                if (wantedMonth.getMonth() == handler1.getMonth()) {
                    System.out.println("Added an Appointment: " + appointment.getAppointmentStartDateTime());
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
