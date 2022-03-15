package model;

/**
 * Getters, Setters, and Constructors associated mostly with manipulating the Division table from the database
 */
public class User {
    private int id;
    private String name;

    /**
     * toString method for combobox
     * @return a String
     */
    @Override
    public String toString() {
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
     * @param id Set ID
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
     * Constructor for User object
     * @param id the User id
     * @param name the User name
     * @param password the User password
     */
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
    }

    /**
     * Empty constructor for User
     */
    public User() {
    }
}
