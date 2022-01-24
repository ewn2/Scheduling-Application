package sample.Controller;
/**
 * modifyAppointment.fxml related Controller
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.JDBC;
import sample.Model.Appointment;
import sample.Model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ModifyAppointment implements Initializable {
    public TextField ModifyAppointmentIDBox;
    public TextField ModifyAppointmentTitleBox;
    public TextField ModifyAppointmentDescriptionBox;
    public TextField ModifyAppointmentLocationBox;
    public ComboBox ModifyAppointmentContactCombo;
    public DatePicker ModifyAppointmentStartDatePicker;
    public DatePicker ModifyAppointmentEndDatePicker;
    public TextField ModifyAppointmentTypeBox;
    public Button ModifyAppointmentSaveButton;
    public Button ModifyAppointmentCancelButton;
    public TextArea errorMessageBox;
    public ComboBox ModifyAppointmentStartTimeHourCombo;
    public ComboBox ModifyAppointmentEndTimeHourCombo;
    public ComboBox ModifyAppointmentStartTimeMinuteCombo;
    public ComboBox ModifyAppointmentEndTimeMinuteCombo;
    public ComboBox ModifyAppointmentStartTimeMinuteCombo1;
    public Label ModifyAppointmentSelectedStart;
    public ComboBox ModifyAppointmentEndTimeMinuteCombo1;
    public Label ModifyAppointmentSelectedEnd;
    public Label businessHoursOpen;
    public Label businessHoursClosed;
    public ComboBox ModifyAppointmentCustomerIDCombo;
    public ComboBox ModifyAppointmentUserIDCombo;

    /**
     * Interface for error message handling Lambda
     */
    interface exceptionLambda {
        void apple();
    }

    /**
     * Reaction to User pressing Save button, attempts to validate all User input Appointment detail values and save them
     * into both the current ObservableList of all Appointments and into the connected SQL database on the top of
     * the existing entry values for that Appointment.
     * <p>
     * Lambda exceptionLambda improved code by removing the need to incorporate individual try and catch blocks for every
     * single User entered String, ComboBox, or Date and Time value. Instead, every value may be placed into an all
     * encompassing try block and the Lambda function can handle every validity check and error message display at once
     * exceptionLambda: Lines 135 through 141
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of fxml file interaction issues
     */
    public void onModifyAppointmentSaveButtonAction(ActionEvent actionEvent) throws IOException {
        boolean validEntries = true;
        boolean addedAppointment = false;
        if (StartEndAppointmentCheck()) {
            try {
                int id = 0;
                String AppointmentTitle = ModifyAppointmentTitleBox.getText();
                if (AppointmentTitle == null || AppointmentTitle.trim().isEmpty() || AppointmentTitle.length() > 50) {
                    throw new Exception("Invalid Title Entry, must not be empty and must be less than 50 characters");
                }
                String AppointmentDesc = ModifyAppointmentDescriptionBox.getText();
                if (AppointmentDesc == null || AppointmentDesc.trim().isEmpty() || AppointmentDesc.length() > 50) {
                    throw new Exception("Invalid Description Entry, must not be empty and must be less than 50 characters");
                }
                String AppointmentLocation = ModifyAppointmentLocationBox.getText();
                if (AppointmentLocation == null || AppointmentLocation.trim().isEmpty() || AppointmentLocation.length() > 50) {
                    throw new Exception("Invalid Location Entry, must not be empty and must be less than 50 characters");
                }
                try {
                    String AppointmentContact = ModifyAppointmentContactCombo.getValue().toString();
                } catch (Exception e) {
                    throw new Exception("Invalid Contact Entry, must not be empty");
                }
                String AppointmentContact = ModifyAppointmentContactCombo.getValue().toString(); //Pull from DB
                if (AppointmentContact == null || AppointmentContact.trim().isEmpty()) {
                    throw new Exception("Invalid Contact Entry, must not be empty");
                }
                String AppointmentType = ModifyAppointmentTypeBox.getText();
                if (AppointmentType == null || AppointmentType.trim().isEmpty() || AppointmentType.length() > 50) {
                    throw new Exception("Invalid Type Entry, must not be empty and must be less than 50 characters");
                }
                LocalDateTime AppointmentStartDateTime = startDate.atTime(LocalTime.from(meetingStart));
                LocalDateTime AppointmentEndDateTime = endDate.atTime(LocalTime.from(meetingEnd));
                if (AppointmentStartDateTime.isAfter(AppointmentEndDateTime)) {
                    throw new Exception("Invalid Appointment Times, Start cannot be after End");
                }
                try {
                    String AppointmentCustomerIDString = ModifyAppointmentCustomerIDCombo.getValue().toString();
                } catch (Exception e) {
                    throw new Exception("Invalid Customer Entry, must not be empty");
                }
                String AppointmentCustomerIDString = ModifyAppointmentCustomerIDCombo.getValue().toString(); //Pull from DB
                if (AppointmentCustomerIDString == null || AppointmentCustomerIDString.trim().isEmpty()) {
                    throw new Exception("Invalid Customer Entry, must not be empty");
                }
                try {
                    String AppointmentUserIDString = ModifyAppointmentUserIDCombo.getValue().toString();
                } catch (Exception e) {
                    throw new Exception("Invalid User Entry, must not be empty");
                }
                String AppointmentUserIDString = ModifyAppointmentUserIDCombo.getValue().toString(); //Pull from DB
                if (AppointmentUserIDString == null || AppointmentUserIDString.trim().isEmpty()) {
                    throw new Exception("Invalid User Entry, must not be empty");
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
            int id = appointmentToModify.getAppointmentID();
            String AppointmentTitle = ModifyAppointmentTitleBox.getText();
            String AppointmentDesc = ModifyAppointmentDescriptionBox.getText();
            String AppointmentLocation = ModifyAppointmentLocationBox.getText();
            String AppointmentContact = ModifyAppointmentContactCombo.getValue().toString(); //Pull from DB
            String AppointmentType = ModifyAppointmentTypeBox.getText();
            ZonedDateTime convertStartToUTC = meetingStart.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime localStart = meetingStart.withZoneSameInstant(ZoneId.systemDefault());
            LocalDateTime AppointmentStartDateTime = startDate.atTime(LocalTime.from(convertStartToUTC));
            LocalDateTime AppointmentStartDateTimeLocal = startDate.atTime(LocalTime.from(localStart));
            String AptStartDateTime = AppointmentStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            String AptStartDateTimeLocal = AppointmentStartDateTimeLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            ZonedDateTime convertEndToUTC = meetingEnd.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime localEnd = meetingEnd.withZoneSameInstant(ZoneId.systemDefault());
            LocalDateTime AppointmentEndDateTime = endDate.atTime(LocalTime.from(convertEndToUTC));
            LocalDateTime AppointmentEndDateTimeLocal = endDate.atTime(LocalTime.from(localEnd));
            String AptEndDateTime = AppointmentEndDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            String AptEndDateTimeLocal = AppointmentEndDateTimeLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            String AppointmentCustomerIDString = ModifyAppointmentCustomerIDCombo.getValue().toString(); //Pull from DB
            String AppointmentUserIDString = ModifyAppointmentUserIDCombo.getValue().toString(); //Pull from DBs
            int AppointmentContactID = 0;
            int AppointmentCustomerID = Integer.parseInt(AppointmentCustomerIDString);
            int AppointmentUserID = Integer.parseInt(AppointmentUserIDString);
            if (validEntries) {
                try {
                    Appointment newAppointment = new Appointment(id, AppointmentTitle, AppointmentDesc, AppointmentLocation, AppointmentContact, AppointmentType, AptStartDateTimeLocal, AptEndDateTimeLocal, AppointmentCustomerID, AppointmentUserID);

                    ObservableList<Appointment> existingCustomerAppointments = FXCollections.observableArrayList();
                    for (Appointment CheckAppointment : Appointment.appointmentPopulation()) {
                        if (CheckAppointment.getAppointmentCustomerID() == Integer.parseInt(AppointmentCustomerIDString)) {
                            existingCustomerAppointments.add(CheckAppointment);
                        }
                    }
                    for (Appointment CheckAppointment : existingCustomerAppointments) {
                        DateTimeFormatter CheckFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                        LocalDateTime existingStart = LocalDateTime.parse(CheckAppointment.getAppointmentStartDateTime(), CheckFormatter);
                        LocalDateTime existingEnd = LocalDateTime.parse(CheckAppointment.getAppointmentEndDateTime(), CheckFormatter);
                        LocalDateTime newStart = LocalDateTime.parse(newAppointment.getAppointmentStartDateTime(), CheckFormatter);
                        LocalDateTime newEnd = LocalDateTime.parse(newAppointment.getAppointmentEndDateTime(), CheckFormatter);
                        if (CheckAppointment.getAppointmentID() != appointmentToModify.getAppointmentID()) {
                            if ((newStart.isAfter(existingStart) && newEnd.isBefore(existingEnd)) || (newStart.isBefore(existingStart) && newEnd.isAfter(existingStart)) || (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) || (newStart.isBefore(existingStart) && newEnd.isAfter(existingEnd))) {
                                throw new Exception();
                            }
                        }
                    }

                    newAppointment.setAppointmentStartDateTime(AptStartDateTime);
                    newAppointment.setAppointmentEndDateTime(AptEndDateTime);
                    if (Appointment.modifyAppointmentInDatabase(newAppointment)) {
                        for (Customer customer : Customer.customerPopulation()) {
                            if (customer.getCustomerID() == newAppointment.getAppointmentCustomerID()) {
                                if (!customer.getAssociatedAppointments().contains(newAppointment)) {
                                    customer.addAssociatedAppointment(newAppointment);
                                }
                            }
                        }
                        addedAppointment = true;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    errorMessageBox.setVisible(true);
                    errorMessageBox.setText("Error: Appointment cannot overlap with existing Appointments for Customer");
                }
            }
            if (addedAppointment) {
                returnToMainScreen(actionEvent);
            }
        } else {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error: Appointment Start and End times and dates must conform to business hours");
        }
    }

    /**
     * Closes the Modify Appointment view and calls a method to return to the MainForm view
     *
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of FXML file interaction issues
     */
    public void onModifyAppointmentCancelButtonAction(ActionEvent actionEvent) throws IOException {
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

    /**
     * Populates the Appointment Time start and end time ComboBoxes for User selection
     */
    public void populateTimeCombos() {
        int i = 0;
        ObservableList<String> hourBoxes = FXCollections.observableArrayList();
        ObservableList<String> minuteBoxes = FXCollections.observableArrayList();
        ObservableList<String> minuteBoxes1 = FXCollections.observableArrayList();
        while (i < 24) {
            hourBoxes.add(String.valueOf(i));
            i++;
        }
        i = 0;
        while (i <= 5) {
            minuteBoxes.add(String.valueOf(i));
            i++;
        }
        i = 0;
        while (i <= 9) {
            minuteBoxes1.add(String.valueOf(i));
            i++;
        }
        ModifyAppointmentStartTimeHourCombo.getItems().addAll(hourBoxes);
        ModifyAppointmentEndTimeHourCombo.getItems().addAll(hourBoxes);
        ModifyAppointmentStartTimeMinuteCombo.getItems().addAll(minuteBoxes);
        ModifyAppointmentEndTimeMinuteCombo.getItems().addAll(minuteBoxes);
        ModifyAppointmentStartTimeMinuteCombo1.getItems().addAll(minuteBoxes1);
        ModifyAppointmentEndTimeMinuteCombo1.getItems().addAll(minuteBoxes1);
        ModifyAppointmentStartDatePicker.setValue(startDate);
        ModifyAppointmentEndDatePicker.setValue(endDate);
    }

    /**
     * Adjusts the displayed business hour labels of 8:00 to 22:00 US/Eastern to the User's local timezone equivalent
     *
     * @throws ParseException thrown in case of User time String parsing issues
     */
    public void businessHoursDisplayAdjustment() throws ParseException {
        String UserStartTime = startTime.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        String UserEndTime = endTime.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        businessHoursOpen.setText(UserStartTime);
        businessHoursClosed.setText(UserEndTime);
    }

    private ZonedDateTime meetingStart;
    private ZonedDateTime meetingEnd;

    /**
     * Checks if User selected Appointment start and end times conform with Business hours
     *
     * @return boolean value of true if User selected Appointment Start and End times conform with Business hours and false if they do not
     */
    public boolean StartEndAppointmentCheck() {
        int hourValue = Integer.parseInt(ModifyAppointmentStartTimeHourCombo.getValue().toString());
        int minuteValue = Integer.parseInt((ModifyAppointmentStartTimeMinuteCombo.getValue().toString() + ModifyAppointmentStartTimeMinuteCombo1.getValue().toString()));
        ZonedDateTime startToCheck = LocalDate.now().atTime(hourValue, minuteValue).atZone(ZoneId.systemDefault());
        int hourValueEnd = Integer.parseInt(ModifyAppointmentEndTimeHourCombo.getValue().toString());
        int minuteValueEnd = Integer.parseInt((ModifyAppointmentEndTimeMinuteCombo.getValue().toString() + ModifyAppointmentEndTimeMinuteCombo1.getValue().toString()));
        ZonedDateTime endToCheck = LocalDate.now().atTime(hourValueEnd, minuteValueEnd).atZone(ZoneId.systemDefault());
        if (startToCheck.compareTo(startTime.withZoneSameInstant(ZoneId.systemDefault())) >= 0 && endToCheck.compareTo(startTime.withZoneSameInstant(ZoneId.systemDefault())) >= 0) {
            if (endToCheck.compareTo(endTime.withZoneSameInstant(ZoneId.systemDefault())) <= 0 && startToCheck.compareTo(endTime.withZoneSameInstant(ZoneId.systemDefault())) <= 0) {
                if (checkDates()) {
                    meetingStart = startToCheck;
                    meetingEnd = endToCheck;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Populates the Contacts ComboBox with Contacts existing within the SQL database
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateContacts() throws SQLException {
        String logQuery = "SELECT * FROM contacts";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Contact_Name");
            ModifyAppointmentContactCombo.getItems().add(ire);
        }
    }

    /**
     * Populates the Customer ComboBox with Customers existing within the SQL database
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateCustomers() throws SQLException {
        String logQuery = "SELECT * FROM customers";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Customer_ID");
            ModifyAppointmentCustomerIDCombo.getItems().add(ire);
        }
    }

    /**
     * Populates the Users ComboBox with Users existing within the SQL database
     *
     * @throws SQLException thrown in case of SQL database interaction issues
     */
    public void populateUsers() throws SQLException {
        String logQuery = "SELECT * FROM users";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("User_ID");
            ModifyAppointmentUserIDCombo.getItems().add(ire);
        }
    }

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

    /**
     * Captures the User's selected Start Date value
     *
     * @param actionEvent User interaction with DatePicker
     */
    public void onModifyAppointmentStartDatePickerAction(ActionEvent actionEvent) {
        startDate = ModifyAppointmentStartDatePicker.getValue();
    }

    /**
     * Captures the User's selected End Date value
     *
     * @param actionEvent User interaction with DatePicker
     */
    public void onModifyAppointmentEndDatePickerAction(ActionEvent actionEvent) {
        endDate = ModifyAppointmentEndDatePicker.getValue();
    }

    /**
     * Boolean that checks if the selected Start and End Dates is realistic to account for Users whose local equivalent
     * of Eastern Business Hours crosses into more than one day
     *
     * @return boolean value of true if the selected Start and End times are valid
     */
    public boolean checkDates() {
        if (startDate.atTime(LocalTime.from(startTime)).compareTo(endDate.atTime(LocalTime.from(endTime))) <= 0 && startDate != null && endDate != null) {
            Duration duration = Duration.between(startDate.atTime(LocalTime.from(startTime)), endDate.atTime(LocalTime.from(endTime)));
            if (duration.toHours() <= 14) {
                return true;
            }
        }
        return false;
    }


    private ZonedDateTime startTime = LocalDate.now().atTime(8, 0).atZone(ZoneId.of("US/Eastern"));
    private ZonedDateTime endTime = LocalDate.now().atTime(22, 0).atZone(ZoneId.of("US/Eastern"));

    private Appointment appointmentToModify = null;

    /**
     * Initializer for modify Appointment screen, loads in Combo Boxes with existing values from database and
     * initializes all TextBoxes and ComboBoxes with existing values associated with the selected instance of Appointment
     *
     * @param url            resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentToModify = MainForm.appointmentPasser;
        ModifyAppointmentIDBox.setText(String.valueOf(appointmentToModify.getAppointmentID()));
        ModifyAppointmentTitleBox.setText(String.valueOf(appointmentToModify.getAppointmentTitle()));
        ModifyAppointmentDescriptionBox.setText(String.valueOf(appointmentToModify.getAppointmentDesc()));
        ModifyAppointmentLocationBox.setText(String.valueOf(appointmentToModify.getAppointmentLocation()));
        ModifyAppointmentTypeBox.setText(String.valueOf(appointmentToModify.getAppointmentType()));
        ModifyAppointmentContactCombo.setValue(String.valueOf(appointmentToModify.getAppointmentContact()));
        ModifyAppointmentCustomerIDCombo.setValue(String.valueOf(appointmentToModify.getAppointmentCustomerID()));
        ModifyAppointmentUserIDCombo.setValue(String.valueOf(appointmentToModify.getAppointmentUserID()));

        populateTimeCombos();
        try {
            businessHoursDisplayAdjustment();
        } catch (ParseException e) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error setting Business Hours label");
        }
        String aptStart = appointmentToModify.getAppointmentStartDateTime();
        String aptEnd = appointmentToModify.getAppointmentEndDateTime();
        ModifyAppointmentStartTimeHourCombo.setValue(aptStart.substring(11, 13));
        ModifyAppointmentStartTimeMinuteCombo.setValue(aptStart.substring(14, 15));
        ModifyAppointmentStartTimeMinuteCombo1.setValue(aptStart.substring(15, 16));
        ModifyAppointmentEndTimeHourCombo.setValue(aptEnd.substring(11, 13));
        ModifyAppointmentEndTimeMinuteCombo.setValue(aptEnd.substring(14, 15));
        ModifyAppointmentEndTimeMinuteCombo1.setValue(aptEnd.substring(15, 16));
        ModifyAppointmentSelectedStart.setText(ModifyAppointmentStartTimeHourCombo.getValue().toString() + ":" + ModifyAppointmentStartTimeMinuteCombo.getValue().toString() + ModifyAppointmentStartTimeMinuteCombo1.getValue().toString());
        ModifyAppointmentSelectedEnd.setText(ModifyAppointmentEndTimeHourCombo.getValue().toString() + ":" + ModifyAppointmentEndTimeMinuteCombo.getValue().toString() + ModifyAppointmentEndTimeMinuteCombo1.getValue().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate handler1 = LocalDate.parse(aptStart.substring(0, 10), formatter);
        LocalDate handler2 = LocalDate.parse(aptEnd.substring(0, 10), formatter);
        ModifyAppointmentStartDatePicker.setValue(handler1);
        ModifyAppointmentEndDatePicker.setValue(handler2);

        try {
            populateContacts();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
        try {
            populateCustomers();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
        try {
            populateUsers();
        } catch (SQLException throwables) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error populating Combo Boxes");
        }
    }

    /**
     * On User adjustment to any Start Time ComboBox, reflects the selected values in a User-facing label
     *
     * @param actionEvent User initiating ComboBox selection
     */
    public void onStartTimeAdjustedAction(ActionEvent actionEvent) {
        ModifyAppointmentSelectedStart.setText(ModifyAppointmentStartTimeHourCombo.getValue().toString() + ":" + ModifyAppointmentStartTimeMinuteCombo.getValue().toString() + ModifyAppointmentStartTimeMinuteCombo1.getValue().toString());
    }

    /**
     * On User adjustment to any Start Time ComboBox, reflects the selected values in a User-facing label
     *
     * @param actionEvent User initiating ComboBox selection
     */
    public void onEndTimeAdjustedAction(ActionEvent actionEvent) {
        ModifyAppointmentSelectedEnd.setText(ModifyAppointmentEndTimeHourCombo.getValue().toString() + ":" + ModifyAppointmentEndTimeMinuteCombo.getValue().toString() + ModifyAppointmentEndTimeMinuteCombo1.getValue().toString());
    }
}
