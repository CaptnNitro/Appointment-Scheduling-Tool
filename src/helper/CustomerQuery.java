package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/** Class handles crud operations for customer operations between the program and the database. */
public abstract class CustomerQuery {

    static int customerID = 0;

    /** Creates a new customer and inserts it into the database.
     * @param newCustomer a Customer object*/
    public static void insert(Customer newCustomer) {
        String name = newCustomer.getName();
        String address = newCustomer.getAddress();
        String pCode = newCustomer.getPostalCode();
        String phone = newCustomer.getPhone();
        int division = newCustomer.getDivision();

        JDBC.openConnection();
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Max(Customer_ID) FROM customers;");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int startingID = rs.getInt(1);
                customerID = startingID + 1;


            }
        } catch (SQLException e){
            System.out.println("Error in last insert");
            System.out.println(customerID);
        }

        String sqlStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (" + customerID + ", " + "'" + name + "'" + ","
                + "'"+address+"'" + ", " + "'" + pCode + "'" + ", " + "'" + phone + "'" + ", " + "'" + division + "'" + ")";

        try{
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println("error");
        }

    }

    /** Updates a record from the Customer table.
     * @param modifiedCustomer a Customer object */
    public static void update(Customer modifiedCustomer){
        int id = modifiedCustomer.getId();
        String name = modifiedCustomer.getName();
        String address = modifiedCustomer.getAddress();
        String phone = modifiedCustomer.getPhone();
        String pcode = modifiedCustomer.getPostalCode();
        int divId = modifiedCustomer.getDivision();

        String sqlStatement = "UPDATE customers SET Customer_Name=" + "'" + name + "'" + ", Address=" + "'" + address + "'" + ", Postal_Code=" + "'" + pcode + "'" + ", Phone="
                + "'" + phone + "'" + ", Division_ID=" + divId + " WHERE Customer_ID=" + id +";";


        try {
            JDBC.openConnection();
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ps.execute();
            JDBC.closeConnection();
        } catch (SQLException e){
            System.out.println("Error in modify customer sqlstatement");
        }

    }
    /** Deletes a record from the Customer table.
     * @param customer Customer object */
    public static void delete(Customer customer){
        int id = customer.getId();

        String sqlStatement = "DELETE FROM customers WHERE Customer_ID=" + id + ";";
        try {
            JDBC.openConnection();
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ps.execute();
            JDBC.closeConnection();
        } catch (SQLException e){
            System.out.println("Error in modify customer sqlstatement");
            System.out.println(sqlStatement);
        }
    }

    /** Returns all of the records from the Customers table in an ObservableList of Customer objects.
     * @return an ObservableList of Customers */
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "select * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();
        Customer customerResult;
        while(result.next()){
            int custId = result.getInt("Customer_ID");
            String custName = result.getString("Customer_Name");
            String custAddress = result.getString("Address");
            String custPhone = result.getString("Phone");
            String postalCode = result.getString("Postal_Code");
            int division = result.getInt("Division_ID");
            customerResult = new Customer(custId, custName, custAddress, custPhone, postalCode, division);
            allCustomers.add(customerResult);
        }
        JDBC.closeConnection();
        return allCustomers;
    }

    /** Pulls all data from the Users table and compares the suppliedName and password agaisnt the information in the table.
     * If it matches returned value is true.
     * @param suppliedName the username
     * @param password the password
     * @return returns true or false based on matching */
    public static boolean userLogin(String suppliedName, String password) throws SQLException {
        boolean okToLogIn = false;
        String sqlStatement = "select * FROM users";

        JDBC.openConnection();
        //run my query on the users table
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            if (suppliedName.equals(result.getString(2))) {
                if (Objects.equals(password, result.getString(3))) {
                    //launch main screen
                    okToLogIn = true;
                }
            }
        }
    return okToLogIn;
    }
}