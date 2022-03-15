package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Class is used to create Appointment objects and get and set information in an appointment. */
public class Appointments {
    private int appointment_ID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customer_ID;
    private int user_ID;
    private int contact_ID;

    /** Construction for an Appointment */
    public Appointments(int appointment_ID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customer_ID, int user_ID, int contact_ID) {
        this.appointment_ID = appointment_ID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_ID = customer_ID;
        this.user_ID = user_ID;
        this.contact_ID = contact_ID;
    }

    /** Empty constructor */
    public Appointments() {
    }

    /** Getter for appointment ID
     * @return returns the ID as an int*/
    public int getAppointment_ID() {
        return appointment_ID;
    }

    /** Setter for appointment_ID
     * @param appointment_ID the value to set */
    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    /** Getter
     * @return title */
    public String getTitle() {
        return title;
    }

    /** Setter
     * @param title the value to set for title */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Getter
     * @return returns description */
    public String getDescription() {
        return description;
    }

    /** Setter
     * @param description sets description */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter
     * @param location the setter value
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter
     * @param type the value to set type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter
     * @return returns the start LocalDateTime
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Setter for start converts from a local time to UTC time
     * @param start a LocalDateTime
     */
    public void setStart(LocalDateTime start) {
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZoneId localZone = ZoneId.systemDefault();

        ZonedDateTime localZDT = ZonedDateTime.of(start, localZone);
        ZonedDateTime startTimeUTC = localZDT.withZoneSameInstant(utcZone);

        this.start = startTimeUTC.toLocalDateTime();
    }

    /**
     * Getter
     * @return end LocalDateTime
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Setter for end.  Accepts a LocalDateTime and converts it into ZonedDateTime in UTC time then converts back to
     * LocalDateTime
     * @param end LocalDateTime
     */
    public void setEnd(LocalDateTime end) {
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZoneId localZone = ZoneId.systemDefault();

        ZonedDateTime localZDT = ZonedDateTime.of(end, localZone);
        ZonedDateTime utcZDT = localZDT.withZoneSameInstant(utcZone);

        this.end = utcZDT.toLocalDateTime();
    }

    /**
     * Getter
     * @return customer_ID
     */
    public int getCustomer_ID() {
        return customer_ID;
    }

    /**
     * Setter
     * @param customer_ID the value to set
     */
    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    /**
     * Getter
     * @return user_ID
     */
    public int getUser_ID() {
        return user_ID;
    }

    /**
     * Setter
     * @param user_ID value for user_ID to be set to
     */
    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    /**
     * Getter
     * @return contact_ID
     */
    public int getContact_ID() {
        return contact_ID;
    }

    /**
     * Setter
     * @param contact_ID the value for contact_ID
     */
    public void setContact_ID(int contact_ID){
        this.contact_ID = contact_ID;
    }
}
