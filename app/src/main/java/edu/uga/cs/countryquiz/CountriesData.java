package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * This class provides database operations for the Country Quiz app.
 *
 * This class is responsible for
 * opening and closing the database,
 * retrieving all countries from the countries table,
 * storing completed quiz results,
 * and retrieving past quiz results.
 *
 * It acts as a bridge between the SQLite database and Java objects
 * such as Country and Quiz.
 */
public class CountriesData {

    public static final String DEBUG_TAG = "CountriesData";

    /** Reference to the actual SQLite database. */
    private SQLiteDatabase db;
    /** Database helper used to create/open the database. */
    private SQLiteOpenHelper dbHelper;
    /** Column list used when reading from the countries table. */
    private static final String[] allColumns = {
            CountryQuizDbHelper.COUNTRIES_COLUMN_ID,
            CountryQuizDbHelper.COUNTRIES_COLUMN_NAME,
            CountryQuizDbHelper.COUNTRIES_COLUMN_CAPITAL,
            CountryQuizDbHelper.COUNTRIES_COLUMN_CONTINENT
    };
    /** Column list used when reading from the quiz results table. */
    private static final String[] resultColumns = {
            CountryQuizDbHelper.RESULTS_COLUMN_ID,
            CountryQuizDbHelper.RESULTS_COLUMN_DATE,
            CountryQuizDbHelper.RESULTS_COLUMN_SCORE
    };
    /**
     * Creates a CountriesData object and connects it to the database helper.
     *
     * @param context the application or activity context
     */
    public CountriesData(Context context) {
        dbHelper = CountryQuizDbHelper.getInstance(context);
    }

    /**
     * Opens the database so it can be read from and written to.
     */
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "CountriesData: db open");
    }

    /**
     * Closes the database helper when database work is finished.
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(DEBUG_TAG, "CountriesData: db closed");
        }
    }
    /**
     * Checks whether the database is currently open.
     *
     * @return true if the database exists and is open; false otherwise
     */
    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    /**
     * Retrieves all countries from the countries table and converts
     * each database row into a Country object.
     *
     * @return an ArrayList containing all Country objects in the database
     */
    public ArrayList<Country> retrieveAllCountries() {

        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Query the countries table and sort results by country name
            cursor = db.query(
                    CountryQuizDbHelper.TABLE_COUNTRIES,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    CountryQuizDbHelper.COUNTRIES_COLUMN_NAME
            );
            // Make sure the cursor has data before reading from it
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    // Read the country id from the current row
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_ID);
                    long id = cursor.getLong(columnIndex);
                    // Read the country name
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_NAME);
                    String name = cursor.getString(columnIndex);
                    // Read the capital
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_CAPITAL);
                    String capital = cursor.getString(columnIndex);
                    // Read the continent
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COUNTRIES_COLUMN_CONTINENT);
                    String continent = cursor.getString(columnIndex);

                    // Create a Country object from this row and add it to the list
                    Country country = new Country(id, name, capital, continent);
                    countries.add(country);

                    Log.d(DEBUG_TAG, "Retrieved country: " + name);
                }
            }
            // Log how many country records were found
            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of country records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of country records from DB: 0");

        } catch (Exception e) {
            // Catch any database or cursor related errors
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return countries;
    }

    /**
     * Stores a completed quiz result in the results table.
     *
     * The quiz date and score are inserted into the database, and the
     * generated database id is then stored back into the Quiz object.
     *
     * @param quiz the completed quiz result to store
     * @return the same Quiz object with its generated id set
     */
    public Quiz storeQuizResult(Quiz quiz) {
        // ContentValues stores column and value pairs for insertion
        ContentValues values = new ContentValues();
        values.put(CountryQuizDbHelper.RESULTS_COLUMN_DATE, quiz.getDate());
        values.put(CountryQuizDbHelper.RESULTS_COLUMN_SCORE, quiz.getScore());
        // Insert the quiz result into the results table
        long id = db.insert(CountryQuizDbHelper.TABLE_RESULTS, null, values);
        // Save the generated database id back into the object
        quiz.setId(id);

        Log.d(DEBUG_TAG, "Stored quiz result with id: " + quiz.getId());

        return quiz;
    }

    /**
     * Retrieves all completed quiz results from the database.
     *
     * Results are returned in descending order by id so the newest quiz
     * results appear first.
     *
     * @return an ArrayList containing all stored Quiz results
     */
    public ArrayList<Quiz> retrieveAllQuizResults() {

        ArrayList<Quiz> results = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Query the results table and sort so most recent entries come first
            cursor = db.query(
                    CountryQuizDbHelper.TABLE_RESULTS,
                    resultColumns,
                    null,
                    null,
                    null,
                    null,
                    CountryQuizDbHelper.RESULTS_COLUMN_ID + " DESC"
            );
            // Only read rows if the query returned data
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    // Read the quiz result id
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_ID);
                    long id = cursor.getLong(columnIndex);
                    // Read the quiz date
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_DATE);
                    String date = cursor.getString(columnIndex);
                    // Read the quiz score
                    columnIndex = cursor.getColumnIndexOrThrow(CountryQuizDbHelper.RESULTS_COLUMN_SCORE);
                    int score = cursor.getInt(columnIndex);
                    // Create a Quiz object from the row and add it to the list
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
                // Always close the cursor after using it
                cursor.close();
            }
        }

        return results;
    }
}