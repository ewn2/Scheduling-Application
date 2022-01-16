package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AddCustomer {
    public TextField addCustomerNameBox;
    public TextField addCustomerPhoneBox;
    public ComboBox addCustomerCountryCombo;
    public TextField addCustomerPostalBox;
    public ComboBox addCustomerStateCombo;
    public TextField addCustomerCityBox;
    public TextField addCustomerAddressBox;
    public Button addCustomerSaveButton;
    public Button addCustomerCancelButton;
    public TextArea errorMessageBox;
    public TextField addCustomerIDBox;

    public void onAddCustomerSaveButtonAction(ActionEvent actionEvent) throws IOException {
        returnToMainScreen(actionEvent);
    }

    public void onAddCustomerCancelButtonAction(ActionEvent actionEvent) throws IOException {
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
