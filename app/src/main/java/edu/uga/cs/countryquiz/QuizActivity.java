package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
/**
 * QuizActivity manages the full quiz lifecycle.
 * This activity is responsible for loading country data from
 * database, generating quiz questions, displaying questions using ViewPager2,
 * tracking user answers and calculating score, saving quiz results to database,
 * and handling configuration changes (e.g., screen rotation)
 */
public class QuizActivity extends AppCompatActivity  {

    private int[] scores = new int[6];
    private Quiz currentQuiz;
    private long savedQuizId = -1; //quizid from storing
    private QuizPagerAdapter avpAdapter;
    private CountriesData countriesData; //db helper
    private ViewPager2 pager;

    /**
     * Called when activity is created.
     * Handles restoring state OR loading new quiz.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quiz_activity);

        countriesData = new CountriesData(this);
        pager = findViewById(R.id.viewpager2);

        // To check if we are restoring
        if (savedInstanceState != null) {
            scores = savedInstanceState.getIntArray("saved_scores");
            currentQuiz = (Quiz) savedInstanceState.getSerializable("saved_quiz");
            int currentPage = savedInstanceState.getInt("current_page");

            // Re-setup the UI with existing data
            avpAdapter = new QuizPagerAdapter(this);
            pager.setAdapter(avpAdapter);
            pager.setCurrentItem(currentPage, false);
            setupPageController();

        } else {
            // No saved state? Load fresh from DB
            new InitialQuizLoader().execute();
        }
    }

    /**
     * Called when the activity is no longer visible to the user.
     * This override ensures that the {@link CountriesData} database connection
     * is safely closed.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (countriesData != null) {
            countriesData.close();
        }
    }

    /**
     * Saves quiz state before activity is destroyed (e.g., rotation).
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the progress
        outState.putIntArray("saved_scores", scores);
        outState.putSerializable("saved_quiz", currentQuiz);
        outState.putInt("current_page", pager.getCurrentItem());

        Log.d("QuizActivity", "State Saved. Current Page: " + pager.getCurrentItem());
    }
    /** Returns the current quiz. */
    public Quiz getCurrentQuiz() { return currentQuiz; }
    /**
     * Updates score for a specific question.
     *
     * @param index question index (0–5)
     * @param score 1 if correct, 0 if incorrect
     */
    public void updateMainScore(int index, int score) {
        if (index >= 0 && index < 6) {
            scores[index] = score;
            Log.d("QuizActivity", "Question " + index + " updated to: " + score);
        }
    }
    /**
     * Calculates total score.
     *
     * @return sum of all question scores
     */
    public int getTotalScore() {
        int total = 0;
        for (int s : scores) total += s;
        return total;
    }

    /**
     * Loads countries from database and generates quiz questions.
     * Runs in background thread.
     */
    private class InitialQuizLoader extends AsyncTask<Void, Quiz> {
        @Override
        protected Quiz doInBackground(Void... voids) {
            countriesData.open();
            // Retrieve all countries
            ArrayList<Country> allCountries = countriesData.retrieveAllCountries();
            // Generate quiz questions
            Quiz generatedQuiz = createQuestionsLogic(allCountries);
            countriesData.close(); //makes sure it is closed
            return generatedQuiz;
        }

        @Override
        protected void onPostExecute(Quiz quiz) { //creates adapter using new quiz
            currentQuiz = quiz;
            avpAdapter = new QuizPagerAdapter(QuizActivity.this);
            pager.setAdapter(avpAdapter);
            setupPageController();
        }
    }
    /**
     * Controls page navigation behavior.
     *
     * - Locks swipe during questions
     * - Enables swipe on results page
     * - Saves quiz when reaching results
     */
    private void setupPageController() {
        // Lock initially
        pager.setUserInputEnabled(false);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean isSaved = false;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 6) { // able to navigate wherever once at end
                    pager.setUserInputEnabled(true);
                    if (!isSaved && currentQuiz != null) {
                        currentQuiz.setCurrentQuestion(position);
                        currentQuiz.setScore(getTotalScore());
                        new SaveQuizTask().execute(currentQuiz); //save quiz in db
                        isSaved = true;
                    }
                } else {
                    // Lock swipe for all question pages (until radio button clicked)
                    pager.setUserInputEnabled(false);
                }
            }
        });
    }

    /**
     * Saves quiz result to database in background.
     */
    private class SaveQuizTask extends AsyncTask<Quiz, Long> {
        @Override
        protected Long doInBackground(Quiz... quizzes) {
            try {
                countriesData.open();
                return countriesData.storeQuizResult(quizzes[0]).getId(); //db returned id
            } catch (Exception e) {
                return -1L; //error so return -1
            } finally {
                countriesData.close(); // close db no matter what
            }
        }

        @Override
        protected void onPostExecute(Long id) {
            savedQuizId = id;
            //triggers result fragment to be generated using the correct quiz id
            if (avpAdapter != null) {
                avpAdapter.setSavedQuizId(id);
                avpAdapter.notifyItemChanged(6); // Refresh results page with ID
            }
        }
    }

    /**
     * Generates 6 unique quiz questions.
     *
     * Ensures:
     * - No duplicate answers in a question
     * - No duplicate main countries across questions
     *
     * @param countries list of all countries
     * @return generated Quiz object
     */
    public Quiz createQuestionsLogic(ArrayList<Country> countries) {
        ArrayList<Question> questionList = new ArrayList<>();
        Country[] countryCheck = new Country[6];
        int totalCountries = countries.size();

        for (int i = 0; i < 6; i++) {
            Country main = null, country1 = null, country2 = null;
            boolean duplicateFound = true;

            while (duplicateFound) {
                duplicateFound = false;
                main = countries.get(ThreadLocalRandom.current().nextInt(totalCountries));
                country1 = countries.get(ThreadLocalRandom.current().nextInt(totalCountries));
                country2 = countries.get(ThreadLocalRandom.current().nextInt(totalCountries));

                // Check for duplicate countries in the same question
                if (main.getCountryName().equals(country1.getCountryName()) ||
                        main.getCountryName().equals(country2.getCountryName()) ||
                        country1.getCountryName().equals(country2.getCountryName())) {
                    duplicateFound = true;
                    continue;
                }

                // Check if main country was used in a previous question
                for (int x = 0; x < i; x++) {
                    if (countryCheck[x] != null && countryCheck[x].getCountryName().equals(main.getCountryName())) {
                        duplicateFound = true;
                        break;
                    }
                }
            }
            countryCheck[i] = main;
            Question newQuestion = new Question(main.getCountryName(), main.getCapital(), country1.getCapital(), country2.getCapital());
            questionList.add(newQuestion);
        }

        Quiz newQuiz = new Quiz(11111, String.format("%tF", new Date()), 0); //create new quiz with a fake id until stored in db
        newQuiz.setQuestions(questionList);
        return newQuiz;
    }
}