package edu.uga.cs.countryquiz;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {

    private long id;
    private String date;
    private int score;

    private ArrayList<Question> questions;
    private int currentQuestion;

    public Quiz(long id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.currentQuestion = 0;
    }

    public Quiz(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setQuestions(ArrayList<Question> questionList) {
        this.questions = questionList;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void setCurrentQuestion(int num){
        this.currentQuestion = num;
    }

    public int getCurrentQuestion() {
        return  this.currentQuestion;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
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