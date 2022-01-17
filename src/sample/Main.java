package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        primaryStage.setTitle("Meeting Scheduling System");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        JDBC.makeConnection();
        String tableName = "users";
        String selectStatement = "SELECT * FROM";
        String allContacts = selectStatement + " " + tableName;
        JDBC.makePreparedStatement(allContacts, JDBC.getConnection());
        Statement selectContactsQuery = JDBC.getPreparedStatement();
        System.out.println(JDBC.getPreparedStatement());
        selectContactsQuery.execute(allContacts);
        ResultSet rs = selectContactsQuery.getResultSet();
        System.out.println(rs);
        while(rs.next())
        {
            System.out.println(rs.getString("User_ID") + " " + rs.getString("User_Name") + " " + rs.getString("Password"));
        }
        ZoneId systemZone = ZoneId.systemDefault();
        Locale location = Locale.getDefault();
        String EnFr = location.getLanguage();
        ResourceBundle propertyChoice = ResourceBundle.getBundle("sample/Language", Locale.getDefault());
        System.out.println(systemZone);
        System.out.println(location);
        System.out.println(EnFr);
        launch(args);
        JDBC.closeConnection();
    }
}
