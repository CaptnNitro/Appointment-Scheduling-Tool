package controller;

import helper.AppointmentsQuery;
import helper.CustomerQuery;
import helper.MessageInterface;
import helper.Utilities;
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
import javafx.stage.Stage;
import model.Appointments;
import model.Contacts;
import model.Customer;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.*;

/** Class controls the Appointments Screen.  Allows user to add, update, and delete appointments.
 * <br>
 * <br>
 * <b>There are two lambdas in this class.  The first is messageInterface and I use it to generate error alerts throughout the class.  It saves me from having to code each one individually.
 * <br>
 * <br>
 * The second lambda is in the form of a listener.  It allows me autopopulate my text and combo boxes when the user selects an appointment from the tableview.  I found this with a search on stackoverflow. </b>*/
public class AppointmentsScreen implements Initializable {
    public TableView<Appointments> tblAppointments;
    public TableColumn<Appointments, Integer> clmnId;
    public TableColumn<Appointments, String> clmnTitle;
    public TableColumn<Appointments, String> clmnDescription;
    public TableColumn<Appointments, String> clmnLocation;
    public TableColumn<Appointments, Integer> clmnContact;
    public TableColumn<Appointments, String>clmnType;
    public TableColumn<Appointments, LocalDateTime> clmnStartTime;
    public TableColumn<Appointments, LocalDateTime> clmnEndTime;
    public TableColumn<Appointments, Integer> clmnCustId;
    public TableColumn<Appointments, Integer> clmnUserId;
    public ComboBox<Contacts> cbContact;
    public ComboBox<LocalTime> cbStartTime;
    public ComboBox<LocalTime> cbEndTime;
    public TextField tbTitle;
    public TextField tbDesc;
    public TextField tbLocation;
    public TextField tbType;
    public DatePicker spDatePicker;
    public ComboBox<Customer> cbCustId;
    public ComboBox<User> cbUserId;
    public TextField tbAppId;
    public ToggleGroup viewScope;
    public RadioButton rbAll;
    public RadioButton rbThirty;
    public RadioButton rbSeven;
    public ComboBox<Month> cbMonthSelector;
    public ComboBox<String> cbTypeSelector;
    public Label lblReport;
    public Label lblCustReport;
    public ComboBox<Contacts> cbContactIdForApps;

    int contactToChoose = -1;
    int customerToChoose = -1;
    int userToChoose = -1;
    ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
    ObservableList<LocalTime> timeList = FXCollections.observableArrayList();
    ObservableList<String> types = FXCollections.observableArrayList();
    ArrayList<String> appTypes = new ArrayList<>();
    Appointments appointment = new Appointments();
    LocalDateTime selectedStart;
    LocalTime adjustedStartTime;
    LocalTime adjustedEndTime;
    ZoneId localTime = ZoneId.systemDefault();
    private final ObservableList<Month> months = FXCollections.observableArrayList(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER,
                                                                                    Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
    private static int contactIdChoice;

    MessageInterface messageInterface = (title, text) -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.showAndWait();
    };
    /** Initializes the appointment screen.  Loads the tableview and sets the combo boxes.  Contains a lambda expression that uses a listener to populate text boxes when the user selects an appointment.
     * @param url accepts a URL
     * @param resourceBundle accepts a ResourceBundle
     * <br><br>
     * <b>Lambda used here to monitor the appointment screen and autopopulate the text and combo boxes when the user makes a selection from the tableview. I found this solution when searching stackoverflow
     * for a way to autopopulate my text boxes and it seemed like a good use of a lambda </b>*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize the Contact combo box
            cbContact.setItems(AppointmentsQuery.getAllContacts());
            cbContactIdForApps.setItems(AppointmentsQuery.getAllContacts());
            // Initialize the Customer_ID box
            cbCustId.setItems(CustomerQuery.getAllCustomers());
            // Initialize the User_ID box
            cbUserId.setItems(AppointmentsQuery.getAllUsers());
            // Build OL
            allAppointments.addAll(AppointmentsQuery.getAllAppointments());
            // Build combo box Months Selector
            cbMonthSelector.setItems(months);

            for (Appointments allAppointment : allAppointments) {
                String type = allAppointment.getType();
                appTypes.add(type);
            }
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(appTypes);
            ArrayList<String> appTypesNoDupes = new ArrayList<>(hashSet);

            types.addAll(appTypesNoDupes);
            cbTypeSelector.setItems(types);
            selectedStart = LocalDateTime.now();

            cbContact.setPromptText("Select a contact");
            cbUserId.setPromptText("Select the user");
            cbCustId.setPromptText("Select the customer");


            LocalDate startDate = LocalDate.now();
            ZoneId estTime = ZoneId.of("America/New_York");
            //Start with LocalDateTime
            LocalTime startTime = LocalTime.of(8,0);
            LocalTime endTime = LocalTime.of(22,0);
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(startDate, endTime);
            // Convert to ZonedDateTime at original time zone
            ZonedDateTime startTimeEastern = ZonedDateTime.of(startDateTime, estTime);
            ZonedDateTime endTimeEastern = ZonedDateTime.of(endDateTime, estTime);
            // Convert to target time zone
            ZonedDateTime startTimeLocal = startTimeEastern.withZoneSameInstant(localTime);
            ZonedDateTime endTimeLocal = endTimeEastern.withZoneSameInstant(localTime);
            // Convert back to LocalDateTime
            LocalDateTime adjustedStartDateTime = startTimeLocal.toLocalDateTime();
            LocalDateTime adjustedEndDateTime = endTimeLocal.toLocalDateTime();

            adjustedStartTime = adjustedStartDateTime.toLocalTime();
            adjustedEndTime = adjustedEndDateTime.toLocalTime();

            // Build the observable list for start and end time combo boxes
            for(int i = 0; i < 96;i++){
                timeList.add(adjustedStartTime);
                adjustedStartTime = adjustedStartTime.plusMinutes(15);
            }

            cbStartTime.setItems(timeList);
            cbEndTime.setItems(timeList);
            spDatePicker.setPromptText("Select the date");
            cbStartTime.setPromptText("Select a start time");
            cbEndTime.setPromptText("Select an end time");
        } catch (Exception e) {
            System.out.println("Exception generated.");
        }

        tblAppointments.setItems(allAppointments);
        clmnId.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        clmnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        clmnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        clmnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        clmnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clmnStartTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        clmnEndTime.setCellValueFactory(new PropertyValueFactory<>("end"));
        clmnCustId.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        clmnUserId.setCellValueFactory(new PropertyValueFactory<>("user_ID"));
        clmnContact.setCellValueFactory(new PropertyValueFactory<>("contact_ID"));

        // Lambda expression to add Listener and set text and combo boxes when the user selects an appointment.
        tblAppointments.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->{
            if (newVal != null) {
                tbAppId.setText(String.valueOf(tblAppointments.getSelectionModel().getSelectedItem().getAppointment_ID()));
                tbTitle.setText(tblAppointments.getSelectionModel().getSelectedItem().getTitle());
                tbDesc.setText(tblAppointments.getSelectionModel().getSelectedItem().getDescription());
                tbLocation.setText(tblAppointments.getSelectionModel().getSelectedItem().getLocation());
                // Get the contact ID from the table
                contactToChoose = tblAppointments.getSelectionModel().getSelectedItem().getContact_ID();
                // Set the combobox
                for(Contacts c: cbContact.getItems()){
                    if(contactToChoose == c.getId()){
                        cbContact.setValue(c);
                    }
                }

                // Get the customer ID from the table
                customerToChoose = tblAppointments.getSelectionModel().getSelectedItem().getCustomer_ID();
                // Set the combo box
                for (Customer c: cbCustId.getItems()) {
                    if (customerToChoose == c.getId()) {
                        cbCustId.setValue(c);
                    }
                }

                tbType.setText(tblAppointments.getSelectionModel().getSelectedItem().getType());
                userToChoose = tblAppointments.getSelectionModel().getSelectedItem().getUser_ID();
                for ( User u: cbUserId.getItems()){
                    if(userToChoose == u.getId()){
                        cbUserId.setValue(u);
                    }
                }

                spDatePicker.setValue(tblAppointments.getSelectionModel().getSelectedItem().getStart().toLocalDate());
                cbStartTime.setValue(tblAppointments.getSelectionModel().getSelectedItem().getStart().toLocalTime());
                cbEndTime.setValue(tblAppointments.getSelectionModel().getSelectedItem().getEnd().toLocalTime());
            }
        });
    }

    /** Adds an appointment to the database.  When the add button is presses the values in the text and combo boxes are used to create a new appointment after appropriate error checking has been completed.
     * @param actionEvent accepts an ActionEvent.
     * <br><br>
     * <b>Lambda messageInterface used to generate Error alerts in this method.  See class description for more information. </b>*/
    public void onAdd(ActionEvent actionEvent) throws Exception {
        //Lambda to create an alert message if user tries to create an appointment before present time and day.

        Appointments newAppointment = new Appointments();
        // Validate data from text boxes
        if (!(tbTitle.getText().isBlank() || tbDesc.getText().isBlank() || tbLocation.getText().isBlank() || tbType.getText().isBlank())) {
            try {
                // Get the time from Start time combo box to be used with the date picker value.
                LocalTime selectedStartTime = cbStartTime.getSelectionModel().getSelectedItem();
                LocalTime selectedEndTime = cbEndTime.getSelectionModel().getSelectedItem();
                if (spDatePicker.getValue().isBefore(LocalDate.now()) || (spDatePicker.getValue().isEqual(LocalDate.now()) && cbStartTime.getValue().isBefore(LocalTime.now()))) {
                    messageInterface.message("Date Problem", "Please select a current or future date and or time.");
                } else {
                    if (!Utilities.appointmentAvailable(spDatePicker.getValue(), selectedStartTime, selectedEndTime, cbCustId.getSelectionModel().getSelectedItem().getId(), -1)) {
                        messageInterface.message("Error", "Overlapping appointments");
                    } else {
                        // Error checking for Start and End combo boxes
                        // Check to see if start time is before office hours
                        if (selectedStartTime.isBefore(adjustedStartTime) || selectedStartTime.isAfter(adjustedEndTime)) {
                            messageInterface.message("Scheduling Error", "Your meeting start time is not within office hours.  Please try again.  Office hours are " + adjustedStartTime +
                                    " to " + adjustedEndTime + " in your time zone.");
                        } else {
                            // Check to verify the start time is before the end time
                            if (selectedStartTime.isAfter(selectedEndTime)) {
                                messageInterface.message("Scheduling Error", "Your meeting start time begins after your meeting end time.");
                            } else {
                                // Verify the end time is between the open and close of business
                                if (selectedEndTime.isBefore(adjustedStartTime) || selectedEndTime.isAfter(adjustedEndTime)) {
                                    messageInterface.message("Scheduling Error", "Your meeting end time is not within office hours.  Please try again.  Office hours are " +
                                            adjustedStartTime + " to " + adjustedEndTime + " in your time zone.");
                                } else {
                                    // ID will be set correctly inside the insert method
                                    newAppointment.setAppointment_ID(-1);
                                    newAppointment.setTitle(tbTitle.getText());
                                    newAppointment.setDescription(tbDesc.getText());
                                    newAppointment.setLocation(tbLocation.getText());

                                    // Create Contact object to use to pull value from the combo box.
                                    Contacts selectedContact = cbContact.getSelectionModel().getSelectedItem();
                                    newAppointment.setContact_ID(selectedContact.getId());
                                    newAppointment.setType(tbType.getText());

                                    // Get the date from date picker to be used in the LocalDateTime
                                    LocalDate selectedDate = spDatePicker.getValue();

                                    // Create the LocalDateTime for the meeting start
                                    LocalDateTime startTime = LocalDateTime.of(selectedDate, selectedStartTime);
                                    newAppointment.setStart(startTime);

                                    // Get the value from the End Time combo box
                                    selectedEndTime = cbEndTime.getValue();
                                    // Create the LocalDateTime for the end of the meeting
                                    LocalDateTime endTime = LocalDateTime.of(selectedDate, selectedEndTime);
                                    newAppointment.setEnd(endTime);

                                    // Get the ID
                                    newAppointment.setCustomer_ID(cbCustId.getSelectionModel().getSelectedItem().getId());

                                    // Get the ID
                                    newAppointment.setUser_ID(cbUserId.getSelectionModel().getSelectedItem().getId());

                                    // Create the Appointment in the database
                                    AppointmentsQuery.addAppointment(newAppointment);

                                    // Update tableview
                                    allAppointments.setAll(AppointmentsQuery.getAllAppointments());
                                    tblAppointments.setItems(AppointmentsQuery.getAllAppointments());

                                    populateCbTypeBox();

                                    if (rbAll.isSelected()) {
                                        allSelected();
                                    }
                                    if (rbThirty.isSelected()) {
                                        thirtySelected();
                                    }
                                    if (rbSeven.isSelected()) {
                                        sevenSelected();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                messageInterface.message("Error", "Please be sure to make a selection with all combo boxes.");
            }
        } else {
            messageInterface.message("Data Validation", "Please be supply information for all fields.");
        }
    }

    /** Updates an exsisting appointment. On clicking the update button the data values in the text and combo boxes are pulled to update the appointment that was selected by the user.
     * @param actionEvent  accepts ActionEvent.
     * <br><br>
     * <b>Lambda messageInterface used to generate Error alerts in this method.  See class description for more information. </b>*/
    public void onUpdate(ActionEvent actionEvent) throws Exception {
            appointment = tblAppointments.getSelectionModel().getSelectedItem();
            // Make sure an appointment has been selected
            if (appointment == null) {
                messageInterface.message("Error", "Please select an appointment to update.");
            } else {
                if (spDatePicker.getValue().isBefore(LocalDate.now())) {
                    messageInterface.message("Date Problem", "Please select a current or future date.");
                } else {
                    if (!(tbTitle.getText().isBlank() || tbDesc.getText().isBlank() || tbLocation.getText().isBlank() || tbType.getText().isBlank())) {
                        LocalTime selectedStartTime = cbStartTime.getSelectionModel().getSelectedItem();
                        LocalTime selectedEndTime = cbEndTime.getSelectionModel().getSelectedItem();
                        // Error checking for Start and End combo boxes
                        // Check to see if start time is before office hours
                        if (selectedStartTime.isBefore(adjustedStartTime) || selectedStartTime.isAfter(adjustedEndTime)) {
                            messageInterface.message("Scheduling Error", "Your meeting start time is not within office hours.  Please try again.  Office hours are " + adjustedStartTime +
                                    " to " + adjustedEndTime + " in your time zone.");
                        } else {
                            // Check to verify the start time is before the end time
                            if (selectedStartTime.isAfter(selectedEndTime)) {
                                messageInterface.message("Scheduling Error", "Your meeting start time begins after your meeting end time.");
                            } else {
                                // Verify the end time is between the open and close of business
                                if (selectedEndTime.isBefore(adjustedStartTime) || selectedEndTime.isAfter(adjustedEndTime)) {
                                    messageInterface.message("Scheduling Error", "Your meeting end time is not within office hours.  Please try again.  Office hours are " +
                                            adjustedStartTime + " to " + adjustedEndTime + " in your time zone.");
                                } else {
                                    // Call method to check for appointment availability
                                    if ((Utilities.appointmentAvailable(spDatePicker.getValue(), cbStartTime.getValue(), cbEndTime.getValue(), cbCustId.getSelectionModel().getSelectedItem().getId(), appointment.getAppointment_ID()))) {
                                        // ID will be set correctly inside the addCustomer method
                                        appointment.setAppointment_ID(tblAppointments.getSelectionModel().getSelectedItem().getAppointment_ID());
                                        appointment.setTitle(tbTitle.getText());
                                        appointment.setDescription(tbDesc.getText());
                                        appointment.setLocation(tbLocation.getText());

                                        // Create Contact object to use to pull value from the combo box.
                                        Contacts selectedContact = cbContact.getSelectionModel().getSelectedItem();
                                        appointment.setContact_ID(selectedContact.getId());
                                        appointment.setType(tbType.getText());

                                        // Get the date from date picker to be used in the LocalDateTime
                                        LocalDate selectedDate = spDatePicker.getValue();

                                        // Get the time from Start time combo box to be used with the date picker value.
                                        selectedStartTime = cbStartTime.getSelectionModel().getSelectedItem();
                                        // Create the LocalDateTime for the meeting start
                                        LocalDateTime startTime = LocalDateTime.of(selectedDate, selectedStartTime);
                                        appointment.setStart(startTime);

                                        // Get the value from the End Time combo box
                                        selectedEndTime = cbEndTime.getValue();
                                        // Create the LocalDateTime for the end of the meeting
                                        LocalDateTime endTime = LocalDateTime.of(selectedDate, selectedEndTime);
                                        appointment.setEnd(endTime);

                                        // Get the ID
                                        appointment.setCustomer_ID(cbCustId.getSelectionModel().getSelectedItem().getId());

                                        // Get the ID
                                        appointment.setUser_ID(cbUserId.getSelectionModel().getSelectedItem().getId());

                                        // Call the DAO method
                                        AppointmentsQuery.updateAppointment(appointment);
                                        allAppointments.setAll(AppointmentsQuery.getAllAppointments());
                                        tblAppointments.setItems(AppointmentsQuery.getAllAppointments());

                                        populateCbTypeBox();
                                        clearData();
                                    }else {
                                            messageInterface.message("Error", "Overlapping appointments");
                                    }


                                    }
                                }
                           }
                    } else {
                        messageInterface.message("Data Validation", "Please be sure to enter information in all fields.");
                    }
                }
            }
        appointment = null;
                    if (rbAll.isSelected()) {
            allSelected();
        }
                    if (rbThirty.isSelected()) {
            thirtySelected();
        }
                    if (rbSeven.isSelected()) {
            sevenSelected();

        }
    }

    /** Deletes the selected appointment from the database.
     * @param actionEvent accepts an ActionEvent <br><br>
     * <b>Lambda messageInterface used to generate Error alerts in this method.  See class description for more information. </b>*/
    public void onDelete(ActionEvent actionEvent) throws Exception {
        appointment = tblAppointments.getSelectionModel().getSelectedItem();
        if (appointment == null){

            messageInterface.message("Error", "Please select an appointment to delete.");
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete?");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // Delete the customer record and the appointments associated with it.
                AppointmentsQuery.deleteAppointment(appointment);

                tblAppointments.setItems(AppointmentsQuery.getAllAppointments());
                allAppointments.setAll(AppointmentsQuery.getAllAppointments());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Confirmation");
                alert1.setContentText("Appointment ID: " + appointment.getAppointment_ID() + " of type: " + appointment.getType() + " has been deleted.");
                tbAppId.clear();
                tbTitle.clear();
                tbDesc.clear();
                tbLocation.clear();
                cbContact.getSelectionModel().clearSelection();
                tbType.clear();
                spDatePicker.setValue(LocalDate.now());
                cbStartTime.getSelectionModel().clearSelection();
                cbEndTime.getSelectionModel().clearSelection();
                cbCustId.getSelectionModel().clearSelection();
                cbUserId.getSelectionModel().clearSelection();

                populateCbTypeBox();

                if (rbAll.isSelected()){
                    allSelected();
                }
                if (rbThirty.isSelected()){
                    thirtySelected();
                }
                if (rbSeven.isSelected()){
                    sevenSelected();
                }

                alert1.show();
                appointment = null;
            }
        }
    }
    /** Loads the Contact Appointment screen.
     * @param actionEvent accepts an ActionEvent */
    public void onContactApp(ActionEvent actionEvent) throws IOException {
        contactIdChoice = cbContactIdForApps.getSelectionModel().getSelectedItem().getId();

        Parent root = FXMLLoader.load(getClass().getResource("/view/ContactAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /** Method allows the transfer of an ObservableList of Appointment objects to another class controller.
     * @param contactId the contact ID to filter for
     * @return returns an observable list of filtered appointments. */
    public static ObservableList<Appointments> appointmentTransfer(int contactId) throws Exception {
        ObservableList<Appointments> selectedAppointments = FXCollections.observableArrayList();
        selectedAppointments.setAll(AppointmentsQuery.getAllAppointments());
        selectedAppointments.removeIf(appointments -> !(appointments.getContact_ID() == contactId));
        return selectedAppointments;
    }

    /** Returns the contact ID
     * @return returns an int */
    public static int getSelectedContact(){
        return contactIdChoice;
    }

    /** Action Event for radio button to show all appointments.
     * @param actionEvent accepts ActionEvent */
    public void switchToAll(ActionEvent actionEvent) throws Exception {
       allSelected();
    }

    /** Action Event for radio button to show thirty days of appointments.
     * @param actionEvent accepts ActionEvent */
    public void switchToThirty(ActionEvent actionEvent) throws Exception {
        thirtySelected();
    }

    /** Action Event for radio button to show seven days of appointments.
     * @param actionEvent accepts ActionEvent */
    public void switchToSeven(ActionEvent actionEvent) throws Exception {
        sevenSelected();
    }

    private void allSelected() throws Exception {
        allAppointments.setAll(AppointmentsQuery.getAllAppointments());
        tblAppointments.setItems(AppointmentsQuery.getAllAppointments());
    }

    private void thirtySelected() throws Exception {
        ObservableList<Appointments> thirtyDayList = FXCollections.observableArrayList();
        LocalDate targetDate = LocalDate.now().plusDays(30);

        allAppointments.setAll(AppointmentsQuery.getAllAppointments());
        for (int i = 0; i < allAppointments.size(); i++){
            LocalDate dateToCheck = allAppointments.get(i).getStart().toLocalDate();

            if(dateToCheck.isBefore(targetDate)){
                thirtyDayList.add(allAppointments.get(i));
            }
        }
        tblAppointments.setItems(thirtyDayList);
    }

    private void sevenSelected() throws Exception {
        ObservableList<Appointments> sevenDayList = FXCollections.observableArrayList();
        allAppointments.setAll(AppointmentsQuery.getAllAppointments());

        for (int i = 0; i < allAppointments.size(); i++){
            if(allAppointments.get(i).getStart().toLocalDate().isBefore(LocalDate.now().plusDays(7))){
                sevenDayList.add(allAppointments.get(i));
            }
        }
        tblAppointments.setItems(sevenDayList);
    }

    private void populateCbTypeBox() throws Exception {
        appTypes.clear();
        types.clear();

        for (Appointments allAppointment : allAppointments) {
            String type = allAppointment.getType();
            appTypes.add(type);
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(appTypes);
        ArrayList<String> appTypesNoDupes = new ArrayList<>(hashSet);

        types.addAll(appTypesNoDupes);

        cbTypeSelector.setItems(types);
    }

    private void clearData(){
        tbAppId.clear();
        tbTitle.clear();
        tbDesc.clear();
        tbLocation.clear();
        cbContact.setValue(null);
        tbType.clear();
        spDatePicker.setValue(null);
        cbStartTime.setValue(null);
        cbEndTime.setValue(null);
        cbCustId.setValue(null);
        cbUserId.setValue(null);
    }

    /** Loads the Customer screen
     * @param actionEvent accepts an ActionEvent */
    public void toCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** Generates a report based on months and appointment types that is displayed on a label.
     * @param actionEvent accepts an ActionEvent */
    public void generateReport(ActionEvent actionEvent) {
        int count = 0;
        Month selectedMonth = cbMonthSelector.getSelectionModel().getSelectedItem();
        String selectedString = cbTypeSelector.getSelectionModel().getSelectedItem();

        for (Appointments appointments : allAppointments){
            if(appointments.getType().equals(selectedString) && selectedMonth == appointments.getStart().getMonth()){
                count = count + 1;
            }
        }
        lblReport.setText(String.valueOf(count));
    }

    /** Generates a report of how many appointments a customer has booked.
     * @param actionEvent accepts ActionEvent */
    public void customerReport(ActionEvent actionEvent) {
        int appCount = 0;

        if (!(cbCustId.getSelectionModel().getSelectedItem() == null)) {
            int custId = cbCustId.getSelectionModel().getSelectedItem().getId();
            for (Appointments appointments : allAppointments) {
                if (appointments.getCustomer_ID() == custId) {
                    appCount++;
                }
            }
        }
        lblCustReport.setText(appCount + " appointments scheduled for the customer.");
    }
}
