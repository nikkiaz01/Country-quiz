package edu.uga.cs.countryquiz;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizQuestion implements Serializable {
    private String countryName;
    private String correctCapital;
    private ArrayList<String> options;
    private int correctIndex;
    private int selectedIndex;
    private boolean answered;

    public QuizQuestion(String countryName, String correctCapital, ArrayList<String> options, int correctIndex) {
        this.countryName = countryName;
        this.correctCapital = correctCapital;
        this.options = options;
        this.correctIndex = correctIndex;
        this.selectedIndex = -1;
        this.answered = false;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCorrectCapital() {
        return correctCapital;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}