package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewReport {
    public Button viewReportCancelButton;
    public TableView reportMonthBreakdownTable;
    public TableColumn reportMonthBreakdownTableMonthCol;
    public TableColumn reportMonthBreakdownTableTotalCol;
    public TableView reportTypeBreakdownTable;
    public TableColumn reportTypeBreakdownTableTypeCol;
    public TableColumn reportTypeBreakdownTableTotalCol;
    public ComboBox selectContactCombo;
    public TableView ContactAppointmentTable;
    public TableColumn ContactAppointmentTableAppointmentIDCol;
    public TableColumn ContactAppointmentTableTitleCol;
    public TableColumn ContactAppointmentTableDescriptionCol;
    public TableColumn ContactAppointmentTableLocationCol;
    public TableColumn ContactAppointmentTableContactCol;
    public TableColumn ContactAppointmentTableTypeCol;
    public TableColumn ContactAppointmentTableStartDateAndTimeCol;
    public TableColumn ContactAppointmentTableEndDateAndTimeCol;
    public TableColumn ContactAppointmentTableCustomerIDCol;
    public ComboBox selectCustomerCombo;
    public TableView CustomerAppointmentTable;
    public TableColumn CustomerAppointmentTableAppointmentIDCol;
    public TableColumn CustomerAppointmentTableTitleCol;
    public TableColumn CustomerAppointmentTableDescriptionCol;
    public TableColumn CustomerAppointmentTableLocationCol;
    public TableColumn CustomerAppointmentTableContactCol;
    public TableColumn CustomerAppointmentTableTypeCol;
    public TableColumn CustomerAppointmentTableStartDateAndTimeCol;
    public TableColumn CustomerAppointmentTableEndDateAndTimeCol;
    public TableColumn CustomerAppointmentTableCustomerIDCol;

    public void onViewReportCancelButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/View/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onSelectContactComboAction(ActionEvent actionEvent) {
    }

    public void onSelectCustomerComboAction(ActionEvent actionEvent) {
    }
}
