package controller;

import helper.AppointmentsQuery;
import helper.CustomerQuery;
import helper.JDBC;
import helper.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;


/** This class controls the log in screen.  Allows the user to log in if the username and password are correct*/
public class LoginScreen implements Initializable {
    @FXML
    private TextField tbUserName;
    @FXML
    private TextField tbPassword;
    @FXML
    Label lblUserName;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblTimeZone;
    @FXML
    private Label lblUserLocation;
    @FXML
    private Button btnLogIn;
    @FXML
    private final String file = "login_activity.txt";

    /** Initialization when Log in screen is loaded.  Checks to see what language is being used and adjusts accordingly.
     * @param resourceBundle ResourceBundle
     * @param url URL*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId systemDefault = ZoneId.systemDefault();
        String place = String.valueOf(systemDefault);
        lblTimeZone.setText(place);
        JDBC.openConnection();
        // Are we speaking French?
        if(Utilities.isLangFrench()) {
            ResourceBundle rb = ResourceBundle.getBundle("bundle", new Locale("fr"));
            lblUserName.setText(rb.getString("user name"));
            lblPassword.setText(rb.getString("password"));
            lblUserLocation.setText(rb.getString("user location"));
            btnLogIn.setText(rb.getString("log in"));
            lblTimeZone.setText(String.valueOf(systemDefault));
        }
    }

    /** Executes when the log in button is pressed.  Verifies the username and password fields against the user database.  Loads a popup window for appoinments within 15 minutes or if there are no
     * appointments in the next 15 minutes.
     * @param actionEvent accepts an ActionEvent
     * @throws Exception throws and exception. */
    @FXML
    public void onLogIn(ActionEvent actionEvent) throws Exception {
        try {
            FileWriter fileLogger = new FileWriter(file, true);
            PrintWriter outputFile = new PrintWriter(fileLogger);

            String userName;
            String password;

            LocalDateTime now = LocalDateTime.now();

            userName = tbUserName.getText();
            password = tbPassword.getText();

            String successfulLogIn = now.truncatedTo(ChronoUnit.SECONDS) + " Login success!  Username: " + userName;
            String failedLogIn = now.truncatedTo(ChronoUnit.SECONDS) + " Login failed!   Username: " + userName;

            if (CustomerQuery.userLogin(userName, password)) {
                ObservableList<Appointments> appoints = FXCollections.observableArrayList();
                appoints.setAll(AppointmentsQuery.getAllAppointments());

                //Log a successful login attempt
                outputFile.println(successfulLogIn);
                fileLogger.close();

                boolean upcomingApp = false;

                for (Appointments appoint : appoints) {
                    if (appoint.getStart().isBefore(now.plusMinutes(15)) && appoint.getStart().isAfter(now)) {
                        Utilities.throwMessageAlert("Appointment", "You have an appointment (ID: " + appoint.getAppointment_ID() + ") on " +
                                appoint.getStart());
                        upcomingApp = true;
                    }
                }

                if (!upcomingApp) {
                    Utilities.throwMessageAlert("Appointment", "No upcoming appointments in the next fifteen minutes.");
                }

                Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }else{
                Utilities.throwErrorAlert("error", "Bad Username or Password");
                outputFile.println(failedLogIn);
                fileLogger.close();
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
