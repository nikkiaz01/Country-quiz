package edu.uga.cs.countryquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity  {

    private int[] scores = new int[6];
    private Quiz currentQuiz;
    private long savedQuizId = -1;
    private QuizPagerAdapter avpAdapter;
    private CountriesData countriesData;
    private ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quiz_activity);

        countriesData = new CountriesData(this);
        pager = findViewById(R.id.viewpager2);

        // CHECK IF WE ARE RESTORING
        if (savedInstanceState != null) {
            scores = savedInstanceState.getIntArray("saved_scores");
            currentQuiz = (Quiz) savedInstanceState.getSerializable("saved_quiz");
            int currentPage = savedInstanceState.getInt("current_page");

            // Re-setup the UI with existing data
            avpAdapter = new QuizPagerAdapter(this);
            pager.setAdapter(avpAdapter);
            pager.setCurrentItem(currentPage, false);
            setupPageController();

            Log.d("QuizActivity", "State Restored. Score: " + getTotalScore());
        } else {
            // No saved state? Load fresh from DB
            new InitialQuizLoader().execute();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the progress
        outState.putIntArray("saved_scores", scores);
        outState.putSerializable("saved_quiz", currentQuiz);
        outState.putInt("current_page", pager.getCurrentItem());

        Log.d("QuizActivity", "State Saved. Current Page: " + pager.getCurrentItem());
    }

    public Quiz getCurrentQuiz() { return currentQuiz; }

    public void updateMainScore(int index, int score) {
        if (index >= 0 && index < 6) {
            scores[index] = score;
            Log.d("QuizActivity", "Question " + index + " updated to: " + score);
        }
    }

    public int getTotalScore() {
        int total = 0;
        for (int s : scores) total += s;
        return total;
    }

    // --- STEP 1: LOAD THE QUIZ DATA ---
    private class InitialQuizLoader extends AsyncTask<Void, Void, Quiz> {
        @Override
        protected Quiz doInBackground(Void... voids) {
            countriesData.open();
            ArrayList<Country> allCountries = countriesData.retrieveAllCountries();
            Quiz generatedQuiz = createQuestionsLogic(allCountries);
            countriesData.close();
            return generatedQuiz;
        }

        @Override
        protected void onPostExecute(Quiz quiz) {
            currentQuiz = quiz;
            avpAdapter = new QuizPagerAdapter(QuizActivity.this);
            pager.setAdapter(avpAdapter);
            setupPageController();
        }
    }

    private void setupPageController() {
        pager.setUserInputEnabled(false); // Lock initially
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean isSaved = false;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 6) { // Landed on ResultsFragment
                    pager.setUserInputEnabled(true);
                    if (!isSaved && currentQuiz != null) {
                        currentQuiz.setScore(getTotalScore());
                        new SaveQuizTask().execute(currentQuiz);
                        isSaved = true;
                    }
                } else {
                    // Lock swipe for all question pages (until radio button clicked)
                    pager.setUserInputEnabled(false);
                }
            }
        });
    }

    // --- STEP 2: SAVE TO DATABASE ---
    private class SaveQuizTask extends AsyncTask<Quiz, Void, Long> {
        @Override
        protected Long doInBackground(Quiz... quizzes) {
            try {
                countriesData.open();
                // Assuming storeQuizResult returns the 'long' row ID
                return countriesData.storeQuizResult(quizzes[0]).getId();
            } catch (Exception e) {
                Log.e("QuizActivity", "Save error: " + e.getMessage());
                return -1L;
            } finally {
                countriesData.close();
            }
        }

        @Override
        protected void onPostExecute(Long id) {
            savedQuizId = id;
            if (avpAdapter != null) {
                avpAdapter.setSavedQuizId(id);
                avpAdapter.notifyItemChanged(6); // Refresh results page with ID
            }
            Log.d("QuizActivity", "Quiz saved with ID: " + id);
        }
    }

    // --- HELPER: QUIZ GENERATION ---
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

                // Check if 'main' was used in a previous question
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

        Quiz newQuiz = new Quiz(11111, String.format("%tF", new Date()), 0);
        newQuiz.setQuestions(questionList);
        return newQuiz;
    }
}