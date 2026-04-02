package edu.uga.cs.countryquiz;

import java.io.Serializable;

public class Country {

    private int id;
    private String countryName;
    private String capital;
    private String continent;

    public Country(int id, String countryName, String capital, String continent) {
        this.id = id;
        this.countryName = countryName;
        this.capital = capital;
        this.continent = continent;
    }

    public int getId() {
        return id;
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