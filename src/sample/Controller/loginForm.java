package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

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

    public void onLoginButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameTextbox.getText();
        String password = passwordTextbox.getText();
        if (User.validUser(username,password)) {
            returnToMainScreen(actionEvent);
        }
        else {
            errorMessageBox.setVisible(true);
            errorMessageBox.setText("Incorrect Username or Password");
        }
    }

    private void returnToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/MainForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
