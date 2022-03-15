package model;

/**
 * Getters and setters for Country class
 */
public class Country {
    String country;
    int countryId;

    public Country(String country, int countryId) {
        this.country = country;
        this.countryId = countryId;
    }

    /**
     * toString
     * @return returns a String
     */
    @Override
    public String toString() {
        return countryId + " " + country;
    }

    /**
     * Getter
     * @return Country ID
     */
    public int getCountryId() {
        return countryId;
    }
}
