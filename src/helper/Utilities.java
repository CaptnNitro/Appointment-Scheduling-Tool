package helper;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointments;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**This class has different error handling and checking methods.*/
public abstract class Utilities {

    /** generates a message.  Method is used to generate error and warning methods from within other classes.
     * @param text accepts a string that will be displayed in the message field.
     * @param title String that will be displayed in the title.*/
    public static void throwErrorAlert(String title, String text) {
        if (isLangFrench() == true) {
            ResourceBundle rb = ResourceBundle.getBundle("bundle", new Locale("fr"));
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle(rb.getString(title));
            alert2.setContentText(rb.getString(text));
            alert2.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle(title);
            alert2.setContentText(text);
            alert2.showAndWait();
        }
    }

    /** generates a message.  Method is used to generate message alerts from within other classes.
     * @param text accepts a string that will be displayed in the message field.
     * @param title String that will be displayed in the title.*/
    public static void throwMessageAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /** Method returns true if the system default language is French. */
    public static boolean isLangFrench(){
        return Locale.getDefault().getLanguage().equals(new Locale("fr").getLanguage());
    }

    /** checks to see if value is an integer.  Method checks testValue to see if it is an int, returns true if so.
     * @param testValue the value to test
     * @return returns true or false */
    public boolean isInt(String testValue) {
        try {
            int isAnInt = Integer.parseInt(testValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** checks to see if value is a double.  Method checks testValue to see if it is a double, returns true if so.
     * @param testValue the value to test
     * @return returns true or false */
    public boolean isDouble(String testValue) {
        try {
            double isADouble = Double.parseDouble(testValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Method checks to see if an appointment is in a valid time slot.  Checks against date, start time, end time, and customer ID.  Will return true is available false if not.
     * @param date The date selected by the user
     * @param startTime The start time selected.
     * @param endTime The end time of the appointment.
     * @param custId The customer ID of the customer involved.
     * @param appId The appointment ID of the appointment selected.  This is used to ignore the selected appointment when checking schedules.
     * @return returns true or false is the appointment slot is open. */
    public static boolean appointmentAvailable(LocalDate date, LocalTime startTime, LocalTime endTime, int custId, int appId ) throws Exception {
        boolean appAvailable = false;
        ObservableList<Appointments> appointments = AppointmentsQuery.getAllAppointments();
        LocalDate dateToCheck;
        LocalTime startTimeToCheck;
        LocalTime endTimeToCheck;

        for (int i = 0;i < appointments.size();i++) {
            dateToCheck = appointments.get(i).getStart().toLocalDate();
            startTimeToCheck = appointments.get(i).getStart().toLocalTime();
            endTimeToCheck = appointments.get(i).getEnd().toLocalTime();


            if (!(appId == appointments.get(i).getAppointment_ID())) {
                // Is appointment on the same day and is the same customer
                if (date.isEqual(dateToCheck) && custId == appointments.get(i).getCustomer_ID()) {
                    // Does the start and end times fall between the start and end times of any other appointments.
                    if ((startTime.isAfter(startTimeToCheck) && startTime.isBefore(endTimeToCheck)) || (endTime.isAfter(startTimeToCheck) && endTime.isBefore(endTimeToCheck))
                            || (startTime.isBefore(startTimeToCheck) && endTime.isAfter(endTimeToCheck)) || (startTime.isAfter(startTimeToCheck) && endTime.isBefore(endTimeToCheck))
                            || (startTime.equals(startTimeToCheck)) || (endTime.equals(endTimeToCheck))) {


                        return false;
                    } else {
                        appAvailable = true;

                    }
                } else {
                    appAvailable = true;

                }
            } else {
                appAvailable = true;

            }
        }
        return appAvailable;
    }
}


