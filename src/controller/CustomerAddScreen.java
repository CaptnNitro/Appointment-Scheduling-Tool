package controller;

import helper.CustomerQuery;
import helper.DivisionQuery;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Controller class for the add customer screen. */
public class CustomerAddScreen implements Initializable {
    public TextField tbName;
    public TextField tbAddress;
    public TextField tbPhone;
    public TextField tbPostalCode;
    public ComboBox<Country> cbCountryBox;
    public Label lblDivision;
    public ComboBox<Division> cbDivision;
    public Button toMain;

    /** Initialization. Sets the prompts for the combo boxes.
     * @param resourceBundle accepts resourceBundle
     * @param url accepts URL*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbCountryBox.setPromptText("Select Country");
        ObservableList<Country> countries1;

        try {
            countries1 = DivisionQuery.getAllCountries();
            cbCountryBox.setItems(countries1);
        } catch (SQLException e) {
            System.out.println("Failed call to getAllCountries");
        }

    }
    /** Fires when the add button is clicked.  Checks for proper data formatting and adds the data into a Customer object if valid.
     * @param actionEvent accepts an actionEvent */
    @FXML
    public void onAdd(ActionEvent actionEvent) throws SQLException {
        String name = tbName.getText();
        String address = tbAddress.getText();
        String phone = tbPhone.getText();
        String postalcode = tbPostalCode.getText();
        boolean goodPhone = false;

        if ( phone.matches("[0-9-]*")) {
            goodPhone = true;
        }
        if (!goodPhone) {
            Utilities.throwErrorAlert("Phone Number", "Please be sure to enter a valid phone number to proceed.");
        }else {
            try {
                int div_Id = cbDivision.getSelectionModel().getSelectedItem().getId();
                if (name.isBlank() || address.isBlank() || phone.isBlank() || postalcode.isBlank()) {
                    Utilities.throwErrorAlert("Error", "Please be sure to fill all boxes before proceeding.");
                } else {
                    Customer newCustomer = new Customer(-1, name, address, phone, postalcode, div_Id);
                    CustomerQuery.insert(newCustomer);

                    // Back to customer main screen
                    Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Customer Controls");
                    stage.setScene(new Scene(root));
                    stage.show();
                }

            } catch (NullPointerException | IOException e) {
                Utilities.throwErrorAlert("Error", "Please select and country and a state / province to procede.");
            }
        }
    }
    /** Returns user to the main customer screen.
     * @param actionEvent accepts actionEvent  */
    @FXML
    public void onClick(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Controls");
        stage.setScene(new Scene(root));
        stage.show();
    }
    /** Fires when the country combo box is used.  Causes the state / province combo box to be populated.
     * @param actionEvent accepts an actionEvent */
    @FXML
    public void countryBoxSelected(ActionEvent actionEvent) throws SQLException {
        int countryChoice = cbCountryBox.getValue().getCountryId();
        ObservableList<Division> states = FXCollections.observableArrayList();
        if(!(countryChoice == 1)){
            lblDivision.setText("Postal Code: ");
        }else{
            lblDivision.setText("State: ");
        }
        states = DivisionQuery.getAllDivision(countryChoice);
        cbDivision.setItems(states);
    }
}
