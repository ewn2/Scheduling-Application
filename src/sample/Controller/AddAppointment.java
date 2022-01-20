package sample.Controller;

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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.*;

public class AddAppointment implements Initializable {
    public TextField addAppointmentIDBox;
    public TextField addAppointmentTitleBox;
    public TextField addAppointmentDescriptionBox;
    public TextField addAppointmentLocationBox;
    public ComboBox addAppointmentContactCombo;
    public DatePicker addAppointmentStartDatePicker;
    public DatePicker addAppointmentEndDatePicker;
    public TextField addAppointmentTypeBox;
    public Button addAppointmentSaveButton;
    public Button addAppointmentCancelButton;
    public TextArea errorMessageBox;
    public ComboBox addAppointmentStartTimeHourCombo;
    public ComboBox addAppointmentEndTimeHourCombo;
    public ComboBox addAppointmentStartTimeMinuteCombo;
    public ComboBox addAppointmentEndTimeMinuteCombo;
    public ComboBox addAppointmentStartTimeMinuteCombo1;
    public ComboBox addAppointmentEndTimeMinuteCombo1;
    public Label addAppointmentSelectedStart;
    public Label addAppointmentSelectedEnd;
    public Label businessHoursOpen;
    public Label businessHoursClosed;
    public ComboBox addAppointmentUserIDCombo;
    public ComboBox addAppointmentCustomerIDCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeCombos();
        try {
            businessHoursDisplayAdjustment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addAppointmentStartTimeHourCombo.setValue("0");
        addAppointmentStartTimeMinuteCombo.setValue("0");
        addAppointmentStartTimeMinuteCombo1.setValue("0");
        addAppointmentEndTimeHourCombo.setValue("0");
        addAppointmentEndTimeMinuteCombo.setValue("0");
        addAppointmentEndTimeMinuteCombo1.setValue("0");
        try {
            populateContacts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            populateCustomers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            populateUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void populateTimeCombos(){
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
        addAppointmentStartTimeHourCombo.getItems().addAll(hourBoxes);
        addAppointmentEndTimeHourCombo.getItems().addAll(hourBoxes);
        addAppointmentStartTimeMinuteCombo.getItems().addAll(minuteBoxes);
        addAppointmentEndTimeMinuteCombo.getItems().addAll(minuteBoxes);
        addAppointmentStartTimeMinuteCombo1.getItems().addAll(minuteBoxes1);
        addAppointmentEndTimeMinuteCombo1.getItems().addAll(minuteBoxes1);
        addAppointmentStartDatePicker.setValue(startDate);
        addAppointmentEndDatePicker.setValue(endDate);
    }

    private ZonedDateTime startTime = LocalDate.now().atTime(8,0).atZone(ZoneId.of("US/Eastern"));
    private ZonedDateTime endTime = LocalDate.now().atTime(22,0).atZone(ZoneId.of("US/Eastern"));
    public void businessHoursDisplayAdjustment() throws ParseException {
        String UserStartTime = startTime.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        String UserEndTime = endTime.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        businessHoursOpen.setText(UserStartTime);
        businessHoursClosed.setText(UserEndTime);
    }

    private ZonedDateTime meetingStart;
    private ZonedDateTime meetingEnd;
    public boolean StartEndAppointmentCheck() {
        int hourValue = Integer.parseInt(addAppointmentStartTimeHourCombo.getValue().toString());
        int minuteValue = Integer.parseInt((addAppointmentStartTimeMinuteCombo.getValue().toString()+addAppointmentStartTimeMinuteCombo1.getValue().toString()));
        ZonedDateTime startToCheck = LocalDate.now().atTime(hourValue,minuteValue).atZone(ZoneId.systemDefault());
        int hourValueEnd = Integer.parseInt(addAppointmentEndTimeHourCombo.getValue().toString());
        int minuteValueEnd = Integer.parseInt((addAppointmentEndTimeMinuteCombo.getValue().toString()+addAppointmentEndTimeMinuteCombo1.getValue().toString()));
        ZonedDateTime endToCheck = LocalDate.now().atTime(hourValueEnd,minuteValueEnd).atZone(ZoneId.systemDefault());
        if (startToCheck.compareTo(startTime.withZoneSameInstant(ZoneId.systemDefault())) >= 0 && endToCheck.compareTo(startTime.withZoneSameInstant(ZoneId.systemDefault())) >= 0 ) {
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

    public void populateContacts() throws SQLException {
        String logQuery = "SELECT * FROM contacts";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Contact_Name");
            addAppointmentContactCombo.getItems().add(ire);
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
            addAppointmentCustomerIDCombo.getItems().add(ire);
        }
    }
    public void populateUsers() throws SQLException {
        String logQuery = "SELECT * FROM users";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("User_ID");
            addAppointmentUserIDCombo.getItems().add(ire);
        }
    }

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();
    public void onAddAppointmentStartDatePickerAction(ActionEvent actionEvent) {
        startDate = addAppointmentStartDatePicker.getValue();
    }
    public void onAddAppointmentEndDatePickerAction(ActionEvent actionEvent) {
        endDate = addAppointmentEndDatePicker.getValue();
    }

    public boolean checkDates() {
        if (startDate.atTime(LocalTime.from(startTime)).compareTo(endDate.atTime(LocalTime.from(endTime))) <= 0 && startDate != null && endDate != null) {
            Duration duration = Duration.between(startDate.atTime(LocalTime.from(startTime)), endDate.atTime(LocalTime.from(endTime)));
            if (duration.toHours() <= 14) {
                return true;
            }
        }
        return false;
    }

    public void onAddAppointmentSaveButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        boolean validEntries = true;
        boolean addedAppointment = false;
        if (StartEndAppointmentCheck()) {
            try {
                int id = 0;
                String AppointmentTitle = addAppointmentTitleBox.getText();
                if (AppointmentTitle == null || AppointmentTitle.trim().isEmpty() || AppointmentTitle.length() > 50) {
                    throw new Exception();
                }
                String AppointmentDesc = addAppointmentDescriptionBox.getText();
                if (AppointmentDesc == null || AppointmentDesc.trim().isEmpty() || AppointmentDesc.length() > 50) {
                    throw new Exception();
                }
                String AppointmentLocation = addAppointmentLocationBox.getText();
                if (AppointmentLocation == null || AppointmentLocation.trim().isEmpty() || AppointmentLocation.length() > 50) {
                    throw new Exception();
                }
                String AppointmentContact = addAppointmentContactCombo.getValue().toString(); //Pull from DB
                if (AppointmentContact == null || AppointmentContact.trim().isEmpty()) {
                    throw new Exception();
                }
                String AppointmentType = addAppointmentTypeBox.getText();
                if (AppointmentType == null || AppointmentType.trim().isEmpty() || AppointmentType.length() > 50) {
                    throw new Exception();
                }
                LocalDateTime AppointmentStartDateTime = startDate.atTime(LocalTime.from(meetingStart));
                System.out.println(AppointmentStartDateTime);
                LocalDateTime AppointmentEndDateTime = endDate.atTime(LocalTime.from(meetingEnd));
                System.out.println(AppointmentEndDateTime);
                String AppointmentCustomerIDString = addAppointmentCustomerIDCombo.getValue().toString(); //Pull from DB
                if (AppointmentCustomerIDString == null || AppointmentCustomerIDString.trim().isEmpty()) {
                    throw new Exception();
                }
                String AppointmentUserIDString = addAppointmentUserIDCombo.getValue().toString(); //Pull from DB
                if (AppointmentUserIDString == null || AppointmentUserIDString.trim().isEmpty()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                errorMessageBox.setVisible(true);
                errorMessageBox.setText("Error: Please check all boxes are filled and within the 50 Character limit per Box");
                validEntries = false;
            }
            int id = 0;
            String AppointmentTitle = addAppointmentTitleBox.getText();
            String AppointmentDesc = addAppointmentDescriptionBox.getText();
            String AppointmentLocation = addAppointmentLocationBox.getText();
            String AppointmentContact = addAppointmentContactCombo.getValue().toString(); //Pull from DB
            String AppointmentType = addAppointmentTypeBox.getText();
            LocalDateTime AppointmentStartDateTime = startDate.atTime(LocalTime.from(meetingStart));
            String AptStartDateTime = AppointmentStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            LocalDateTime AppointmentEndDateTime = endDate.atTime(LocalTime.from(meetingEnd));
            String AptEndDateTime = AppointmentEndDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            String AppointmentCustomerIDString = addAppointmentCustomerIDCombo.getValue().toString(); //Pull from DB
            String AppointmentUserIDString = addAppointmentUserIDCombo.getValue().toString(); //Pull from DBs
            int AppointmentContactID = 0;
            int AppointmentCustomerID = Integer.parseInt(AppointmentCustomerIDString);
            int AppointmentUserID = Integer.parseInt(AppointmentUserIDString);
            if (validEntries) {
                try {
                    Appointment newAppointment = new Appointment(id, AppointmentTitle,AppointmentDesc,AppointmentLocation,AppointmentContact,AppointmentType,AptStartDateTime,AptEndDateTime,AppointmentCustomerID,AppointmentUserID);
                    newAppointment.setAppointmentID(Appointment.uniqueAppointmentID());
                    Appointment.addAppointment(newAppointment);
                    if (Appointment.addAppointmentToDatabase(newAppointment)) {
                        addedAppointment = true;
                    }
                    else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    errorMessageBox.setVisible(true);
                    errorMessageBox.setText("Error: Cannot add Customer to database, check connection");
                }
            }
            returnToMainScreen(actionEvent);
        }
        else {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Error: Appointment Start and End times and dates must conform to business hours");
        }
    }

    public void onAddAppointmentCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/View/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onStartTimeAdjustedAction(ActionEvent actionEvent) {
        addAppointmentSelectedStart.setText(addAppointmentStartTimeHourCombo.getValue().toString() + ":" + addAppointmentStartTimeMinuteCombo.getValue().toString() + addAppointmentStartTimeMinuteCombo1.getValue().toString());
    }

    public void onEndTimeAdjustedAction(ActionEvent actionEvent) {
        addAppointmentSelectedEnd.setText(addAppointmentEndTimeHourCombo.getValue().toString() + ":" + addAppointmentEndTimeMinuteCombo.getValue().toString() + addAppointmentEndTimeMinuteCombo1.getValue().toString());
    }
}
