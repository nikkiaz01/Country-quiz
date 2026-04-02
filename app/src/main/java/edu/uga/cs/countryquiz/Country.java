package edu.uga.cs.countryquiz;

import java.io.Serializable;

public class Country {

    private long id;
    private String countryName;
    private String capital;
    private String continent;

    public Country(long id, String countryName, String capital, String continent) {
        this.id = id;
        this.countryName = countryName;
        this.capital = capital;
        this.continent = continent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCapital() {
        return capital;
    }

    public String getContinent() {
        return continent;
    }
}