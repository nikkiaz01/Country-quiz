package edu.uga.cs.countryquiz;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Represents a Quiz session in the Country Quiz app.
 * A Quiz object stores the quiz id (from database),
 * the date the quiz was taken, the final score,
 * the list of questions for that quiz,
 * and the current question index.
 */
public class Quiz implements Serializable {

    private long id;
    private String date;
    private int score;

    private ArrayList<Question> questions;
    private int currentQuestion;
    /**
     * Constructor used when retrieving quiz results from the database.
     *
     * @param id quiz id
     * @param date quiz date
     * @param score quiz score
     */
    public Quiz(long id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.currentQuestion = 0;
    }
    /**
     * Constructor used when creating a new quiz (before storing in DB).
     *
     * @param date quiz date
     * @param score quiz score
     */
    public Quiz(String date, int score) {
        this.date = date;
        this.score = score;
    }
    /**
     * Gets the quiz id.
     *
     * @return quiz id
     */
    public long getId() {
        return id;
    }
    /**
     * Sets the list of questions for this quiz.
     *
     * @param questionList list of Question objects
     */
    public void setQuestions(ArrayList<Question> questionList) {
        this.questions = questionList;
    }
     /**
     * Gets the list of questions in this quiz.
     *
     * @return list of questions
     */
    public ArrayList<Question> getQuestions() {
        return this.questions;
    }
    /**
     * Sets the current question index.
     * Used to track progress through the quiz.
     *
     * @param num index of current question
     */
    public void setCurrentQuestion(int num){
        this.currentQuestion = num;
    }

    /**
     * Gets the current question index.
     *
     * @return current question number
     */
    public int getCurrentQuestion() {
        return  this.currentQuestion;
    }
    /**
     * Sets the quiz id (usually after inserting into database).
     *
     * @param id generated database id
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * Sets the quiz score.
     *
     * @param score final score
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * Gets the quiz date.
     *
     * @return date string
     */
    public String getDate() {
        return date;
    }
    /**
     * Gets the quiz score.
     *
     * @return score value
     */
    public int getScore() {
        return score;
    }
    /**
     * Returns a string representation of the quiz.
     * Useful for displaying quiz results in lists.
     *
     * @return formatted string with date and score
     */
    @Override
    public String toString() {
        return date + " | Score: " + score;
    }
}