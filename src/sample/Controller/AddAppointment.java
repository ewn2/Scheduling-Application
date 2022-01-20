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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    public TextField addAppointmentCustomerIDBox;
    public TextField addAppointmentUserIDBox;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTimeCombos();
        businessHoursDisplayAdjustment();
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
    }

    public void businessHoursDisplayAdjustment() {

        SimpleDateFormat estDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        estDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        //Current Date Time in EST
        System.out.println("Current Date and Time in GMT time zone: " + estDateFormat.format(new Date()));

        Timestamp ESTOpenHours = Timestamp.valueOf("2000-01-01 08:00:00");//yyyy-MM-dd HH:mm:ss.S
        Timestamp ESTCloseHours = Timestamp.valueOf("2000-01-01 22:00:00");
        String adjOpen = ESTOpenHours.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        String adjClose = ESTCloseHours.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
        businessHoursOpen.setText(adjOpen);
        businessHoursClosed.setText(adjClose);
    }

    public void populateCountry() throws SQLException {
        String logQuery = "SELECT * FROM countries";
        JDBC.makePreparedStatement(logQuery, JDBC.getConnection());
        Statement checkQuery = JDBC.getPreparedStatement();
        checkQuery.execute(logQuery);
        ResultSet rs = checkQuery.getResultSet();
        while (rs.next()) {
            String ire = rs.getString("Country");
            //addCustomerCountryCombo.getItems().add(ire);
        }
    }

    private Date startDate;
    public void onAddAppointmentStartDatePickerAction(ActionEvent actionEvent) {
        //startDate = addAppointmentStartDatePicker.getValue().atStartOfDay();
    }

    private LocalDate endDate;
    public void onAddAppointmentEndDatePickerAction(ActionEvent actionEvent) {
        endDate = addAppointmentEndDatePicker.getValue();
    }

    public void onAddAppointmentSaveButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
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
    }

    public void onEndTimeAdjustedAction(ActionEvent actionEvent) {
    }
}
