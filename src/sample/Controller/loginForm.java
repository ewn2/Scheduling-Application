package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class loginForm implements Initializable {
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

    public static String loggedInUser;
    public static String getLoggedInUserID;

    public void onLoginButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameTextbox.getText();
        String password = passwordTextbox.getText();
        if (User.validUser(username,password)) {
            loggedInUser = username;
            returnToMainScreen(actionEvent);
        }
        else {
            errorMessageBox.setVisible(true);
        }
    }

    private void returnToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/MainForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static ZoneId usersTimezone = ZoneId.systemDefault();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("sample/Language", Locale.getDefault());
        errorMessageBox.setText(resourceBundle.getString("loginError"));
        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        ExitButton.setText(resourceBundle.getString("ExitButton"));
        loginButton.setText(resourceBundle.getString("loginButton"));
        mainScreenTitleLabel.setText(resourceBundle.getString("mainScreenTitleLabel"));
        String localeLabel = (String.valueOf(usersTimezone));
        String languageLabel = (String.valueOf(Locale.getDefault()));
        ZoneIDLabel.setText(resourceBundle.getString("LanguageLabel") +": "+ languageLabel + ",\n" + resourceBundle.getString("LocationLabel") +": "+ localeLabel);
    }
}
