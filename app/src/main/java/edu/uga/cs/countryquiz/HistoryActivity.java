package edu.uga.cs.countryquiz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<String> historyList;
    private CountriesData countriesData;

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

        // Initialize our NEW external adapter
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        // Load data
        new LoadHistoryTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class LoadHistoryTask extends AsyncTask<Void, List<Quiz>> {
        @Override
        protected List<Quiz> doInBackground(Void... voids) {
            countriesData.open();
            List<Quiz> quizzes = countriesData.retrieveAllQuizResults();
            countriesData.close();
            return quizzes;
        }

        @Override
        protected void onPostExecute(List<Quiz> quizzes) {
            if (quizzes != null) {
                historyList.clear();
                for (Quiz q : quizzes) {
                    historyList.add("Date: " + q.getDate() + "\nScore: " + q.getScore() + " / 6");
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
