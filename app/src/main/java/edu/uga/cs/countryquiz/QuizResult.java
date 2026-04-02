package edu.uga.cs.countryquiz;
public class QuizResult{
    private int id;
    private String date;
    private int score;
    public QuizResult(int id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
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