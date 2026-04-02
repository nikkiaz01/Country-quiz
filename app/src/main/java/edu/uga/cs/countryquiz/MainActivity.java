package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitialDbHelper initailDbHelper = new InitialDbHelper(this);
        initailDbHelper.copyDatabaseIfNeeded();

        CountryQuizDbHelper dbHelper = CountryQuizDbHelper.getInstance(this);

        ArrayList<Country> countries = dbHelper.getAllCountries();
        ArrayList<QuizResult> resultsBefore = dbHelper.getAllQuizResults();
        boolean inserted = dbHelper.insertQuizResult("2026-04-01 18:00", 4);
        ArrayList<QuizResult> resultsAfter = dbHelper.getAllQuizResults();

        String message = "Countries loaded: " + countries.size()
                + "\nResults before insert: " + resultsBefore.size()
                + "\nInsert success: " + inserted
                + "\nResults after insert: " + resultsAfter.size();

        if (!countries.isEmpty()) {
            message += "\nFirst country: " + countries.get(0).getCountryName();
        }

        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(18f);
        setContentView(textView);
    }
}