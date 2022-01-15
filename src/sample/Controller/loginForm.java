package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class loginForm {
    public Label mainScreenTitleLabel;
    public Button ExitButton;
    public TextArea errorMessageBox;
    public Button confirmButton;
    public Button denyButton;
    public TextField usernameTextbox;
    public TextField passwordTextbox;
    public Label usernameLabel;
    public Label passwordLabel;
    public Button loginButton;
    public Label ZoneIDLabel;

    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onConfirmButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
    }

    public void onDenyButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
    }

    public void onLoginButtonAction(ActionEvent actionEvent) {
    }
}
