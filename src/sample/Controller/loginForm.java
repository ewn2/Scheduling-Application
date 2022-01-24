package sample.Controller;
/**
 * loginForm.fxml related Controller
 *
 * @author Erwin Uppal
 */

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

    /**
     * Reaction to User clicking Exit button, closes the application
     * @param actionEvent User initiating button press
     */
    public void onExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Reaction to User clicking Confirm button
     * @param actionEvent User initiating button press
     */
    public void onConfirmButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
    }

    /**
     * Reaction to User clicking Deny button
     * @param actionEvent User initiating button press
     */
    public void onDenyButtonAction(ActionEvent actionEvent) {
        errorMessageBox.setVisible(false);
        confirmButton.setVisible(false);
        denyButton.setVisible(false);
    }

    public static String loggedInUser;
    public static String getLoggedInUserID;

    /**
     * Reaction to User clicking Login button, takes in String values from User facing username and
     * password boxes and passes them to method validUser() to receive a boolean value of true and allow
     * the login or of false to restrict the login and display an error message
     * @param actionEvent User initiating button press
     * @throws IOException thrown in case of log file interaction issues
     * @throws SQLException thrown in case of SQL database interaction issues
     */
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

    /**
     * When called, loads in MainForm.fxml as the current User-facing scene
     * @param actionEvent Initiating call from other method
     * @throws IOException thrown in case of FXML file interaction issues
     */
    private void returnToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/View/MainForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static ZoneId usersTimezone = ZoneId.systemDefault();

    /**
     * Initializer for login form screen, checks User's system information on timezone and language and adjusts
     * displayed String values to reflect them. Loads String values from different ResourceBundles if the system
     * default language is set to French, but otherwise defaults to English.
     * @param url resource location pointer
     * @param resourceBundle target ResourceBundle to select key values from
     */
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
