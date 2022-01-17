package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
    public Button reportsButton;
    public DatePicker WeekOrMonthSelect;
    public RadioButton appointmentsNoFilterRadio;
    public TableColumn customerTableCustomerPhoneCol;
    public TableColumn customerTableCustomerCountryCol;
    public TableColumn customerTableCustomerPostalCol;
    public TableColumn customerTableCustomerStateCol;
    public TableColumn customerTableCustomerCityCol;
    public TableColumn customerTableCustomerAddressCol;

    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onCustomerDeleteButtonAction(ActionEvent actionEvent) {
    }

    public void onCustomerModifyButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/ModifyCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onCustomerAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onAppointmentDeleteButtonAction(ActionEvent actionEvent) {
    }

    public void onAppointmentModifyButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/modifyAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onAppointmentAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onAppointmentsWeeklyRadioAction(ActionEvent actionEvent) {
    }

    public void onAppointmentsMonthlyRadioAction(ActionEvent actionEvent) {
    }

    public void onConfirmButtonAction(ActionEvent actionEvent) {
    }

    public void onDenyButtonAction(ActionEvent actionEvent) {
    }

    public void onReportsButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/ViewReport.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onWeekOrMonthSelectAction(ActionEvent actionEvent) {
    }

    public void onAppointmentsNoFilterRadioAction(ActionEvent actionEvent) {
    }
}
