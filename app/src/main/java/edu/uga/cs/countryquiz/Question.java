package edu.uga.cs.countryquiz;

import java.io.Serializable;

/**
 * Represents a single quiz question in the Country Quiz app.
 * Each question contains the country name, the correct capital,
 * and two incorrect answer choices
 */
public class Question implements Serializable {
    private String countryName;
    private String capital;
    private String otherCity1;
    private String otherCity2;

    /**
     * Constructs a Question object with all answer choices.
     *
     * @param countryName the country being asked about
     * @param capital the correct capital city
     * @param otherCity1 first incorrect option
     * @param otherCity2 second incorrect option
     */
    public Question(String countryName, String capital, String otherCity1, String otherCity2) {
        this.countryName = countryName;
        this.capital = capital;
        this.otherCity1 = otherCity1;
        this.otherCity2 = otherCity2;
    }
    /**
     * Gets the country name (question prompt).
     *
     * @return country name
     */
    public String getCountryName() {
        return countryName;
    }
    /**
     * Gets the correct capital city.
     *
     * @return correct answer
     */

    public String getCapital() {
        return capital;
    }
    /**
     * Gets the first incorrect answer choice.
     *
     * @return incorrect option
     */
    public String getOtherCity1() {
        return otherCity1;
    }
    /**
     * Gets the second incorrect answer choice.
     *
     * @return incorrect option
     */
    public String getOtherCity2() {
        return otherCity2;
    }
}