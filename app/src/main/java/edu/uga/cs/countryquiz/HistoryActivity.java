package edu.uga.cs.countryquiz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
/**
 * HistoryActivity displays a list of past quiz results.
 *
 * This activity retrieves saved quiz results from the database,
 * displays them in a RecyclerView, and allows the user to return to
 * the previous screen using the Action Bar back button.
 */
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<String> historyList;
    private CountriesData countriesData;
    /**
     * Called when the activity is first created.
     *
     * This method sets the layout,
     * enables the Action Bar back button,
     * initializes the RecyclerView and adapter,
     * and starts loading quiz history from the database.
     *
     * @param savedInstanceState previously saved state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Enable back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Past Quizzes");
        }

        // Initialize data list and database
        historyList = new ArrayList<>();
        countriesData = new CountriesData(this);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize external adapter
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        // Load data
        new LoadHistoryTask().execute();
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
     * Handles Action Bar back button behavior.
     *
     * @return true after navigating back
     */
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
    /**
     * AsyncTask that loads quiz history from the database in the background.
     * This prevents database work from blocking the UI thread.
     */
    private class LoadHistoryTask extends AsyncTask<Void, List<Quiz>> {
        /**
         * Runs in the background thread.
         *
         * Opens the database, retrieves all saved quiz results,
         * and then closes the database.
         *
         * @param voids unused parameters
         * @return list of saved Quiz objects
         */
        @Override
        protected List<Quiz> doInBackground(Void... voids) {
            countriesData.open();
            // Retrieve all past quiz results from the database
            List<Quiz> quizzes = countriesData.retrieveAllQuizResults();
            countriesData.close();
            return quizzes;
        }
        /**
         * Runs on the UI thread after background work is complete.
         *
         * Converts Quiz objects into display strings and updates the RecyclerView.
         *
         * @param quizzes list of retrieved quiz results
         */
        @Override
        protected void onPostExecute(List<Quiz> quizzes) {
            if (quizzes != null) {
                // Remove old displayed items before adding updated data
                historyList.clear();
                // Format each quiz result as a readable string
                for (Quiz q : quizzes) {
                    double percentage = (q.getScore() * 100.0) / 6;
                    String percentString = String.format("%.1f%%", percentage); //so score is a %
                    historyList.add("Date: " + q.getDate() + "\nScore: " + q.getScore() + " / 6 = " + percentString);
                }
                // Notify adapter that the data changed so RecyclerView refreshes
                adapter.notifyDataSetChanged();
            }
        }
    }
}
