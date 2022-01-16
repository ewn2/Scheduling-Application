package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AddAppointment {
    public TextField addAppointmentIDBox;
    public TextField addAppointmentTitleBox;
    public TextField addAppointmentCustomerIDBox;
    public TextField addAppointmentUserIDBox;
    public TextField addAppointmentDescriptionBox;
    public TextField addAppointmentLocationBox;
    public ComboBox addAppointmentContactCombo;
    public DatePicker addAppointmentStartDatePicker;
    public DatePicker addAppointmentEndDatePicker;
    public ComboBox addAppointmentStartTimeCombo;
    public ComboBox addAppointmentEndTimeCombo;
    public TextField addAppointmentTypeBox;
    public Button addAppointmentSaveButton;
    public Button addAppointmentCancelButton;
    public TextArea errorMessageBox;

    public void onAddAppointmentStartDatePickerAction(ActionEvent actionEvent) {
    }

    public void onAddAppointmentEndDatePickerAction(ActionEvent actionEvent) {
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
}
