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

public class ModifyAppointment {
    public TextField ModifyAppointmentIDBox;
    public TextField ModifyAppointmentTitleBox;
    public TextField ModifyAppointmentCustomerIDBox;
    public TextField ModifyAppointmentUserIDBox;
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

    public void onModifyAppointmentStartDatePickerAction(ActionEvent actionEvent) {
    }

    public void onModifyAppointmentEndDatePickerAction(ActionEvent actionEvent) {
    }

    public void onModifyAppointmentSaveButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    public void onModifyAppointmentCancelButtonAction(ActionEvent actionEvent) throws IOException {
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
