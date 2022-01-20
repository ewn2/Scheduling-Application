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

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerTable.setItems(Customer.customerPopulation());
        customerTableCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerTableCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerTableCustomerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("CustomerPhone"));
        customerTableCustomerCountryCol.setCellValueFactory(new PropertyValueFactory<>("CustomerCountry"));
        customerTableCustomerPostalCol.setCellValueFactory(new PropertyValueFactory<>("CustomerPostal"));
        customerTableCustomerStateCol.setCellValueFactory(new PropertyValueFactory<>("CustomerState"));
        customerTableCustomerAddressCol.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
        AppointmentTable.setItems(Appointment.appointmentPopulation());
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
    }

    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public Customer selectedCustomer = null;
    public Appointment selectedAppointment = null;

    public static Customer customerPasser = null;
    public static Appointment appointmentPasser = null;

    public void onCustomerDeleteButtonAction(ActionEvent actionEvent) {
        //selectedAppointment = null;
        selectedCustomer = null;
        selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Delete this Customer?");
            confirmButton.setVisible(true);
            denyButton.setVisible(true);
        }
        //Switch Delete Customer to this after appointments implemented
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
            CustomerTable.setItems(Customer.customerPopulation());
        }
        /*
        if (selectedAppointment != null) {
            Appointment.deleteAppointment(selectedAppointment);
            AppointmentTable.setItems(Appointment.appointmentPopulation());
        }
         */
        selectedCustomer = null;
        //selectedAppointment = null;
    }

    public void onDenyButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
        selectedCustomer = null;
        //selectedAppointment = null;
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
