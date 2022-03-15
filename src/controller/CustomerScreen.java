package controller;

import helper.AppointmentsQuery;
import helper.CustomerQuery;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** CustomerScreen allows the user to add, modify, or delete customers from the database. */
public class CustomerScreen implements Initializable {
    public Button toAddCustomer;
    @FXML
    private TableColumn<Customer, Integer> columnPostalCode;
    @FXML
    private TableColumn<Customer, Integer> columnDivision;
    @FXML
    private TableColumn<Customer, String> columnName;
    @FXML
    private TableColumn<Customer, Integer> columnId;
    @FXML
    private TableColumn<Customer, String> columnAddress;
    @FXML
    private TableColumn<Customer, String> columnPhone;
    @FXML
    private TableView<Customer> tblCustomerTable;

    ObservableList<Customer> Customers = FXCollections.observableArrayList();
    private static Customer modifyCustomer;

    /**
     * Initialization when screen is loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnDivision.setCellValueFactory(new PropertyValueFactory<>("division"));

        try {
            Customers.addAll(CustomerQuery.getAllCustomers());
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        tblCustomerTable.setItems(Customers);
    }
    /** Loads the CustomerAddScreen. */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerAddScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /** Loads the CustomerModifyScreen if a row has been selected on the table. */
    public void onClick(ActionEvent actionEvent) throws IOException {
        modifyCustomer = tblCustomerTable.getSelectionModel().getSelectedItem();
        if (modifyCustomer == null) {
            Utilities.throwErrorAlert("Error", "Please select a customer to modify.");
        } else {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerModifyScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    /** Method allows Customer objects to be sent to other screens.
     * @return Customer
     */
    public static Customer sendToModify() {
        return modifyCustomer;
    }

    /** Deletes a selection from the table.  Throws an error message if no selection has been made. */
    public void onDelete(ActionEvent actionEvent) throws Exception {
        modifyCustomer = tblCustomerTable.getSelectionModel().getSelectedItem();
        if (modifyCustomer == null) {
            Utilities.throwErrorAlert("Error", "Please select a customer to delete.");
        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete?");
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            // Delete the customer record and the appointments associated with it.
            AppointmentsQuery.deleteAllAppointments(modifyCustomer.getId());
            CustomerQuery.delete(modifyCustomer);
            tblCustomerTable.setItems(CustomerQuery.getAllCustomers());
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Confirmation");
            alert1.setContentText("Customer has been deleted.");
            alert1.show();
            }
        }
    }
    /** Loads the AppointmentsScreen. */
    public void toAppointments(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}