package edu.uga.cs.countryquiz;

import java.io.Serializable;
/**
 * Represents a Country object used in the Country Quiz app.
 * This is a simple POJO (Plain Old Java Object) that stores
 * information about a country such as its name, capital, and continent.
 */
public class Country implements Serializable {
    /** Unique id for the country (from the database). */
    private long id;
    /** Name of the country. */
    private String countryName;
    /** Capital city of the country. */
    private String capital;
    /** Continent where the country is located. */
    private String continent;
    /**
     * Constructs a Country object with all fields initialized.
     *
     * @param id unique database id
     * @param countryName name of the country
     * @param capital capital city
     * @param continent continent name
     */
    public Country(long id, String countryName, String capital, String continent) {
        this.id = id;
        this.countryName = countryName;
        this.capital = capital;
        this.continent = continent;
    }
    /**
     * Gets the country id.
     *
     * @return country id
     */
    public long getId() {
        return id;
    }
    /**
     * Sets the country id (used when updating or inserting into DB).
     *
     * @param id new id value
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * Gets the country name.
     *
     * @return country name
     */
    public String getCountryName() {
        return countryName;
    }
    /**
     * Gets the capital city.
     *
     * @return capital name
     */
    public String getCapital() {
        return capital;
    }
    /**
     * Gets the continent name.
     *
     * @return continent
     */
    public String getContinent() {
        return continent;
    }
}