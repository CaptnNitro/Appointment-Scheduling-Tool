package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Division;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class is used for CRUD operations on the Countries and Divisions tables. */
public abstract class DivisionQuery {

    /** Retrieves an ObservableList of Country objects from the Countries table.
     * @throws SQLException sql exception
     * @return an ObservableList of Country objects from the Countries table */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM countries;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();
        Country countryResult = null;
        while (result.next()){
            String name = result.getString("Country");
            int countryId = result.getInt("Country_ID");
            int divId = -1;

            countryResult = new Country(name, countryId);
            countries.add(countryResult);
        }
        JDBC.closeConnection();
        return countries;
    }
    /** Overloaded method of getAllCountries this accepts a country ID and only returns the Country with that ID.
     * @param countryId the ID of the country you want
     * @throws SQLException an sql exception
     * @return the country object matching the ID */
    public static Country getAllCountries(int countryId) throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM countries WHERE Country_ID= " + countryId + ";" ;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();
        Country countryResult = null;
        while (result.next()){
            String name = result.getString("Country");
            countryId = result.getInt("Country_ID");
            countryResult = new Country(name, countryId);
        }
        JDBC.closeConnection();
        return countryResult;
    }

    /** Method retrieves a list of Divisions from the database that match the Country_ID submitted.
     * @param countryCode the ID of the country to select for
     * @throws SQLException an exception
     * @return List of all the divisions associated with that country ID. */
    public static ObservableList<Division> getAllDivision(int countryCode) throws SQLException {
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.first_level_divisions where Country_ID= " + countryCode + ";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();
        Division divisionResult = null;
        while (result.next()){
            String name = result.getString("Division");
            int divId = result.getInt("Division_ID");
            int countryId = result.getInt("Country_ID");
            divisionResult = new Division(divId, name);
            divisions.add(divisionResult);
        }
        JDBC.closeConnection();
        return divisions;
    }

     /** Retrieves the division id when sent the name of the division
      * @param divisionId the division name
      * @throws SQLException an sql exception
      * @return the division ID */
    public static Division getDivision(int divisionId) throws SQLException {
        String name;
        JDBC.openConnection();
        Division division = null;
        String sqlStatement = "SELECT * FROM client_schedule.first_level_divisions where Division_ID= " + divisionId + ";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet result = ps.executeQuery();

        while (result.next()){
            name = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            division = new Division(divisionId, name);
        }
        JDBC.closeConnection();
        return division;
    }

    /** Method accepts a division id and returns the division name in the form of a String.
     * @param id the division id
     * @throws SQLException an sql exception
     * @return the name of the division in String format */
    public static String getDivisionName(int id) throws SQLException {
        String divisionName = null;
        JDBC.openConnection();
        String sqlStatement = "Select * From first_level_divisions where Division_ID=" + id +";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        divisionName = resultSet.getString("Division");


        return divisionName;
    }

    /** Method accepts a division ID and returns the country ID associated with it.
     * @param divId the division id being used
     * @throws SQLException an sql exception
     * @return returns the country ID in int form */
    public static int getCountryId(int divId) throws SQLException{
        JDBC.openConnection();
        String sqlStatement = "select * from client_schedule.first_level_divisions Where Division_ID=" + divId + ";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        return resultSet.getInt("Country_ID");
    }
}
