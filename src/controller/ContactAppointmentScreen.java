package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** This class is a controller for the Contact Appointment screen.  Which displays the appointments for a given contact.*/
public class ContactAppointmentScreen implements Initializable {
    public TableView<Appointments> tblAppointments;
    public TableColumn<Appointments, Integer> columnId;
    public TableColumn<Appointments, String> columnTitle;
    public TableColumn<Appointments, String> columnDescription;
    public TableColumn<Appointments, String> columnLocation;
    public TableColumn<Appointments, Integer> columnContact;
    public TableColumn<Appointments, String> columnType;
    public TableColumn<Appointments, LocalDateTime> columnStartTime;
    public TableColumn<Appointments, LocalDateTime> columnEndTime;
    public TableColumn<Appointments, Integer> columnCustomerId;
    public TableColumn<Appointments, Integer> columnUserId;

    /** Initialize generates a table view by using the appointmentTransfer method from the AppointmentScreen.
     * @param url accepts URL
     * @param resourceBundle accepts ResourceBundle */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            tblAppointments.setItems(AppointmentsScreen.appointmentTransfer(AppointmentsScreen.getSelectedContact()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        columnId.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnStartTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnEndTime.setCellValueFactory(new PropertyValueFactory<>("end"));
        columnCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        columnUserId.setCellValueFactory(new PropertyValueFactory<>("user_ID"));
        columnContact.setCellValueFactory(new PropertyValueFactory<>("contact_ID"));
    }

    /** Loads the appointments screen.
     * @param actionEvent ActionEvent*/
    public void returnToAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
