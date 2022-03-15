package model;

/**
 * Getters and setters and constructors for Customer class.  Also has toString method
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int division;

    /**
     * Constructor for a Customer object
     * @param id the customer ID
     * @param name the customer name
     * @param address the customer address
     * @param phone the customer phone number
     * @param postalCode the customer's postal code
     * @param division the division id associated with the customer
     */
    public Customer(int id, String name, String address,String phone, String postalCode, int division){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
        this.division = division;
    }

    /**
     * Empty customer constructor
     */
    public Customer() {    }

    /**
     * toString method for Customer
     * @return a String
     */
    @Override
    public String toString(){
        return id + " (" + name + ")";
    }

    /**
     * Getter
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     * Getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter
     * @return division of customer
     */
    public int getDivision() {
        return division;
    }

    /**
     * Setter
     * @param id Sets id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter
     * @param name Sets name
     */
    public void setName(String name) {
        this.name = name;
    }
}
