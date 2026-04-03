package edu.uga.cs.countryquiz;

import java.io.Serializable;

public class Question implements Serializable {
    private String countryName;
    private String capital;
    private String otherCity1;
    private String otherCity2;

    public Question(String countryName, String capital, String otherCity1, String otherCity2) {
        this.countryName = countryName;
        this.capital = capital;
        this.otherCity1 = otherCity1;
        this.otherCity2 = otherCity2;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCapital() {
        return capital;
    }

    public String getOtherCity1() {
        return otherCity1;
    }
    public String getOtherCity2() {
        return otherCity2;
    }
}