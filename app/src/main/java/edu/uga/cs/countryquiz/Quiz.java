package edu.uga.cs.countryquiz;

import java.io.Serializable;

public class Quiz {

    private long id;
    private String date;
    private int score;

    public Quiz(long id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }

    public Quiz(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return date + " | Score: " + score;
    }
}