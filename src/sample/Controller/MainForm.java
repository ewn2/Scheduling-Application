package sample.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class MainForm {
    public Label mainScreenTitleLabel;
    public Button ExitButton;
    public AnchorPane CustomerAnchorPane;
    public TableView CustomerTable;
    public TableColumn customerTableCustomerIDCol;
    public TableColumn customerTableCustomerNameCol;
    public Button customerDeleteButton;
    public Button customerModifyButton;
    public Button customerAddButton;
    public AnchorPane AppointmentAnchorPane;
    public TableView AppointmentTable;
    public TableColumn AppointmentTableAppointmentIDCol;
    public TableColumn AppointmentTableTitleCol;
    public TableColumn AppointmentTableDescriptionCol;
    public TableColumn AppointmentTableLocationCol;
    public TableColumn AppointmentTableContactCol;
    public TableColumn AppointmentTableTypeCol;
    public TableColumn AppointmentTableStartDateAndTimeCol;
    public TableColumn AppointmentTableEndDateAndTimeCol;
    public TableColumn AppointmentTableCustomerIDCol;
    public TableColumn AppointmentTableUserIDCol;
    public Button AppointmentDeleteButton;
    public Button AppointmentModifyButton;
    public Button AppointmentAddButton;
    public RadioButton appointmentsWeeklyRadio;
    public ToggleGroup WeekOrMonth;
    public RadioButton appointmentsMonthlyRadio;
    public TextArea errorMessageBox;
    public Button confirmButton;
    public Button denyButton;

    public void onExitButtonAction(ActionEvent actionEvent) {
    }

    public void onCustomerDeleteButtonAction(ActionEvent actionEvent) {
    }

    public void onCustomerModifyButtonAction(ActionEvent actionEvent) {
    }

    public void onCustomerAddButtonAction(ActionEvent actionEvent) {
    }

    public void onAppointmentDeleteButtonAction(ActionEvent actionEvent) {
    }

    public void onAppointmentModifyButtonAction(ActionEvent actionEvent) {
    }

    public void onAppointmentAddButtonAction(ActionEvent actionEvent) {
    }

    public void onAppointmentsWeeklyRadioAction(ActionEvent actionEvent) {
    }

    public void onAppointmentsMonthlyRadioAction(ActionEvent actionEvent) {
    }

    public void onConfirmButtonAction(ActionEvent actionEvent) {
    }

    public void onDenyButtonAction(ActionEvent actionEvent) {
    }
}
