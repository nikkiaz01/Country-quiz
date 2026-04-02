package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * This class handles retrieving Country objects from the database.
 * It acts as a bridge between the database and Java objects (POJOs).
 */
public class CountriesData {

    public static final String DEBUG_TAG = "CountriesData";

    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    private static final String[] allColumns = {
            CountryQuizDbHelper.COUNTRIES_COLUMN_ID,
            CountryQuizDbHelper.COUNTRIES_COLUMN_NAME,
            CountryQuizDbHelper.COUNTRIES_COLUMN_CAPITAL,
            CountryQuizDbHelper.COUNTRIES_COLUMN_CONTINENT
    };

    private static final String[] resultColumns = {
            CountryQuizDbHelper.RESULTS_COLUMN_ID,
            CountryQuizDbHelper.RESULTS_COLUMN_DATE,
            CountryQuizDbHelper.RESULTS_COLUMN_SCORE
    };

    public CountriesData(Context context) {
        dbHelper = CountryQuizDbHelper.getInstance(context);
    }

    // Open DB
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "CountriesData: db open");
    }

    // Close DB
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(DEBUG_TAG, "CountriesData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    /**
     * Retrieve all countries from DB and convert to Country objects
     */
    public ArrayList<Country> retrieveAllCountries() {

        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            cursor = db.query(
                    CountryQuizDbHelper.TABLE_COUNTRIES,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    CountryQuizDbHelper.COUNTRIES_COLUMN_NAME
            );

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_ID);
                    long id = cursor.getLong(columnIndex);

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_NAME);
                    String name = cursor.getString(columnIndex);

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_CAPITAL);
                    String capital = cursor.getString(columnIndex);

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_CONTINENT);
                    String continent = cursor.getString(columnIndex);

                    Country country = new Country(id, name, capital, continent);
                    countries.add(country);

                    Log.d(DEBUG_TAG, "Retrieved country: " + name);
                }
            }

            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of country records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of country records from DB: 0");

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return countries;
    }

    /**
     * Store a completed quiz result in the database.
     *
     * @param quiz the completed quiz result
     * @return the stored Quiz with its generated id set
     */
    public Quiz storeQuizResult(Quiz quiz) {

        ContentValues values = new ContentValues();
        values.put(CountryQuizDbHelper.RESULTS_COLUMN_DATE, quiz.getDate());
        values.put(CountryQuizDbHelper.RESULTS_COLUMN_SCORE, quiz.getScore());

        long id = db.insert(CountryQuizDbHelper.TABLE_RESULTS, null, values);

        quiz.setId(id);

        Log.d(DEBUG_TAG, "Stored quiz result with id: " + quiz.getId());

        return quiz;
    }

    /**
     * Retrieve all past completed quiz results from the database.
     *
     * @return list of all completed quiz results
     */
    public ArrayList<Quiz> retrieveAllQuizResults() {

        ArrayList<Quiz> results = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            cursor = db.query(
                    CountryQuizDbHelper.TABLE_RESULTS,
                    resultColumns,
                    null,
                    null,
                    null,
                    null,
                    CountryQuizDbHelper.RESULTS_COLUMN_ID + " DESC"
            );

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_ID);
                    long id = cursor.getLong(columnIndex);

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_DATE);
                    String date = cursor.getString(columnIndex);

                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_SCORE);
                    int score = cursor.getInt(columnIndex);

                    Quiz quizResult = new Quiz(id, date, score);
                    results.add(quizResult);

                    Log.d(DEBUG_TAG, "Retrieved quiz result: " + quizResult);
                }
            }

            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of quiz result records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of quiz result records from DB: 0");

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught while retrieving quiz results: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return results;
    }
}