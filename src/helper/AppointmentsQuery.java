package helper;


import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Contacts;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Class handles appointment interaction with sql database. */
public abstract class AppointmentsQuery {

    /** Returns an observable list of all contacts from the database.
     * @throws Exception throws an exception
     * @return returns an observable list. */
    public static ObservableList<Contacts> getAllContacts() throws Exception{
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "select * from contacts;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        //Query.makeQuery(sqlStatement);
        ResultSet resultSet = ps.executeQuery();
        Contacts contacts;

        while(resultSet.next()){
            int id = resultSet.getInt("Contact_ID");
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");
            contacts = new Contacts(id, name, email);
            allContacts.add(contacts);
        }

        return allContacts;
    }
    /** Returns an observable list of all users from the database.
     * @throws Exception throws an exception
     * @return returns an observable list. */
    public static ObservableList<User> getAllUsers() throws Exception{
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "select * from users;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        //Query.makeQuery(sqlStatement);
        ResultSet resultSet = ps.executeQuery();
        User user;

        while(resultSet.next()){
            int id = resultSet.getInt("User_ID");
            String name = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            user = new User(id, name, password);
            allUsers.add(user);
        }

        return allUsers;
    }

    /** Returns an observable list of all appointments from the database.
     * @throws Exception throws an exception
     * @return returns an observable list. */
    public static ObservableList<Appointments> getAllAppointments() throws Exception {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement = "select * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet resultSet = ps.executeQuery();
        Appointments appointmentsResult;
        while(resultSet.next()){
            int id = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
            int custId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            ZoneId localTime = ZoneId.systemDefault();
            ZoneId UTCTime = ZoneId.of("UTC");
            // Convert to ZonedDateTime at original time zone
            ZonedDateTime startTimeUTC = ZonedDateTime.of(start, UTCTime);
            ZonedDateTime endTimeUTC = ZonedDateTime.of(end, UTCTime);

            ZonedDateTime startTimeLocal = startTimeUTC.withZoneSameInstant(localTime);
            ZonedDateTime endTimeLocal = endTimeUTC.withZoneSameInstant(localTime);

            start = startTimeLocal.toLocalDateTime();
            end = endTimeLocal.toLocalDateTime();

            appointmentsResult = new Appointments(id, title, description, location, type, start, end, custId, userId, contactId);
            allAppointments.add(appointmentsResult);
        }
        JDBC.closeConnection();
        return allAppointments;
    }

    /** Deletes all appointments that match a customer ID passed to the method.
     * @param customerId the customer ID passed to the method. */
    public static void deleteAllAppointments(int customerId){
        String sqlStatement = "DELETE FROM appointments WHERE Customer_ID=" + customerId + ";";

        try {
            JDBC.openConnection();
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ps.execute();
            JDBC.closeConnection();
        } catch (SQLException e){
            System.out.println("Error in modify customer sqlstatement");
        }
    }

    /** Method adds an appointment from the Appointment screen to the database.
     * @param newAppointment an Appointment
     * @throws SQLException an SQL exception. */
    public static void addAppointment(Appointments newAppointment) throws SQLException {

        int appointmentId = -1;
        String title = "'" + newAppointment.getTitle() + "'";
        String desc = "'" + newAppointment.getDescription() + "'";
        String location = "'" + newAppointment.getLocation() + "'";
        int contactId = newAppointment.getContact_ID();
        String type = "'" + newAppointment.getType() + "'";

        LocalDateTime ldtStart = newAppointment.getStart();
        Timestamp start = Timestamp.valueOf(ldtStart);

        LocalDateTime ldtEnd = newAppointment.getEnd();
        Timestamp end = Timestamp.valueOf(ldtEnd);

        int custId = newAppointment.getCustomer_ID();
        int userId = newAppointment.getUser_ID();

        /* Determine the correct appointment id to use */
        JDBC.openConnection();
        try{
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Max(Appointment_ID) FROM appointments;");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int startingID = rs.getInt(1);
                appointmentId = startingID + 1;
            }
        } catch (SQLException e){
            System.out.println("Error in last insert");
        }

        String sqlStatement = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES " +
                "(" + appointmentId + ", " + title + ", " + desc + ", " + location + ", " + type + ", " + "'" + start + "'" + ", " + "'" + end + "'" + ", " + custId + ", " + userId + ", " + contactId + ");"  ;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.execute();
    }

    /** Updates an appointment in the database.
     * @param appointment the appointment to be updated
     * @throws SQLException an sql exception */
    public static void updateAppointment(Appointments appointment) throws SQLException {
        int id = appointment.getAppointment_ID();
        String title = "'" + appointment.getTitle() + "'";
        String desc = "'" + appointment.getDescription() + "'";
        String location = "'" + appointment.getLocation() + "'";
        int contactId = appointment.getContact_ID();
        String type = "'" + appointment.getType() + "'";

        LocalDateTime ldtStart = appointment.getStart();
        Timestamp start = Timestamp.valueOf(ldtStart);

        LocalDateTime ldtEnd = appointment.getEnd();
        Timestamp end = Timestamp.valueOf(ldtEnd);

        int custId = appointment.getCustomer_ID();
        int userId = appointment.getUser_ID();

        String sqlStatement = "UPDATE appointments SET Title=" + title + ", Description=" + desc + ", Location=" + location + ", Type=" + type + ", Start=" + "'" + start + "'" + ", End=" + "'" + end + "'" +
                ", Customer_ID=" + custId + ", User_ID=" + userId + ", Contact_ID=" + contactId + " WHERE Appointment_ID=" + id + ";";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.execute();
        JDBC.closeConnection();
    }

    /** Deletes and appointment from the database.
     * @param appointment the appointment to be deleted
     * @throws SQLException an sql exception */
    public static void deleteAppointment(Appointments appointment) throws SQLException {
        int id = appointment.getAppointment_ID();

        String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID= " + id + ";";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.execute();
        JDBC.closeConnection();
    }
}
