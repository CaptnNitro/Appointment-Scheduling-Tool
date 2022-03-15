package model;

/**
 *  Getters and setter for the Contacts class
 */
public class Contacts {
    private int id;
    private String name;
    private String email;

    /**
     * Constructor for Contacts object
     * @param id the Contact ID
     * @param name the Contact Name
     * @param email the Contact Email
     */
    public Contacts(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * toString Method
     * @return returns a String
     */
    @Override
    public String toString(){
        return id + " (" + name + ")";
    }

    /**
     * Getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter
     * @param id accepts ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param name Sets name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter
     * @param email Sets email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
