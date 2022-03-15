package model;

/**
 * Getters, Setters, and Constructors for Division
 */
public class Division {
    private int id;
    private String name;

    /**
     * toString used for data formatting in combo box
     * @return String
     */
    @Override
    public String toString(){
        return name;
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
     * @param name Set name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param id Set id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Constructor
     * @param id id of Division
     * @param name name of Division
     */
    public Division(int id, String name){
        this.id = id;
        this.name = name;
    }
}
