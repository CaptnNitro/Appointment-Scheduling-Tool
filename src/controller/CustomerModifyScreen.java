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
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Class controller for the modify customer screen.  Imports the selection made on the main customer screen into this screen and allows updates to be made.*/
public class CustomerModifyScreen implements Initializable {
    public TextField tbId;
    public TextField tbName;
    public TextField tbAddress;
    public TextField tbPhone;
    public TextField tbPostalCode;
    public ComboBox<Country> cbCountry;
    public ComboBox<Division> cbCountryBox;
    public ComboBox<Division> cbDivisionBox;
    public ComboBox<Division> cbDivision;
    public Button toMain;
    public Label lblDivision;
    int custId;
    String name;
    String address;
    String phone;
    String pcode;
    Division divisionName;
    int divId;
    int countryId;
    Customer sentCustomer;

    /** Initialization for screen.  Imports data and builds combo boxes.
     * @param url URL
     * @param resourceBundle ResourceBundle*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> countries1 = FXCollections.observableArrayList();
        // Customer object passed from main screen.
        sentCustomer = CustomerScreen.sendToModify();
        // Populate the countries' combo box
        try {
            countries1 = DivisionQuery.getAllCountries();
            cbCountry.setItems(countries1);
            int countryId = DivisionQuery.getCountryId(sentCustomer.getDivision());
            cbCountry.setValue(DivisionQuery.getAllCountries(countryId));
            cbDivision.setItems(DivisionQuery.getAllDivision(countryId));
            cbDivision.setValue(DivisionQuery.getDivision(sentCustomer.getDivision()));
        } catch (SQLException e) {
            System.out.println("Failed call to getAllCountries");
        }

        // Setting text boxes with values of sentCustomer
        tbId.setText(String.valueOf(sentCustomer.getId()));
        tbName.setText(sentCustomer.getName());
        tbAddress.setText(sentCustomer.getAddress());
        tbPhone.setText(sentCustomer.getPhone());
        tbPostalCode.setText(sentCustomer.getPostalCode());

        custId = sentCustomer.getId();
        name = sentCustomer.getName();
        address = sentCustomer.getAddress();
        phone = sentCustomer.getPhone();
        pcode = sentCustomer.getPostalCode();
        divId = sentCustomer.getDivision();

        try {
            int selectedCountry = DivisionQuery.getCountryId(sentCustomer.getDivision());
            countryId = DivisionQuery.getCountryId(sentCustomer.getDivision());
            cbDivision.setPromptText(DivisionQuery.getDivisionName(sentCustomer.getDivision()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Fires when the save button is pressed. Updates the customer record in the database after doing error checking.
     * @param actionEvent ActionEvent*/
    public void onSave(ActionEvent actionEvent)throws Exception{
        boolean goodphone = false;
        custId = Integer.parseInt(tbId.getText());
        name = tbName.getText();
        address = tbAddress.getText();
        phone = tbPhone.getText();
        pcode = tbPostalCode.getText();
        if ( phone.matches("[0-9-]*")) {
            goodphone = true;
        }
        if (!goodphone) {
            Utilities.throwErrorAlert("Phone Number", "Please be sure your phone number contains only numbers and dashes.");
        }else {

            if (name.isBlank() || address.isBlank() || phone.isBlank() || pcode.isBlank()) {
                Utilities.throwErrorAlert("Error", "Please be sure to fill all boxes before proceeding.");
            } else {
                // get the division string to pass to the get id method
                divisionName = cbDivision.getSelectionModel().getSelectedItem();
                //get the id
                divId = divisionName.getId();

                if (cbDivision.getValue() == null) {
                    divId = sentCustomer.getDivision();
                }
                // create the modified customer
                Customer modifiedCustomer = new Customer(custId, name, address, phone, pcode, divId);
                CustomerQuery.update(modifiedCustomer);
                // Back to customer main screen
                Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Customer Controls");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

    /** Changes lblDivision depending on the country choice.
     * @param actionEvent ActionEvent
     * @throws SQLException sql exception*/
    @FXML
    public void countryBoxSelected(ActionEvent actionEvent) throws SQLException {
        int countryChoice = cbCountry.getValue().getCountryId();
        ObservableList<Division> states = FXCollections.observableArrayList();
        if(!(countryChoice == 1)){
            lblDivision.setText("Postal Code: ");
        }else{
            lblDivision.setText("State: ");
        }
        states = DivisionQuery.getAllDivision(countryChoice);
        cbDivision.setItems(states);
    }

    /** Loads the main customer screen.
     * @param actionEvent ActionEvent
     * @throws Exception an exception */
    public void goToMain(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Controls");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
