package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * This is a SQLiteOpenHelper class for the Country Quiz app.
 * It is responsible for opening the database and creating/upgrading
 * tables as needed.
 *
 * This class follows the Singleton Design Pattern, so only one
 * instance of this helper exists in the app.
 */
public class CountryQuizDbHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "CountryQuizDbHelper";

    private static final String DB_NAME = "countryquiz.db";
    private static final int DB_VERSION = 1;

    // Table and column names for countries table
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COUNTRIES_COLUMN_ID = "id";
    public static final String COUNTRIES_COLUMN_NAME = "country";
    public static final String COUNTRIES_COLUMN_CAPITAL = "capital";
    public static final String COUNTRIES_COLUMN_CONTINENT = "continent";
    public static final String COUNTRIES_COLUMN_CODE = "code";

    // Table and column names for results table
    public static final String TABLE_RESULTS = "results";
    public static final String RESULTS_COLUMN_ID = "id";
    public static final String RESULTS_COLUMN_DATE = "quiz_date";
    public static final String RESULTS_COLUMN_SCORE = "score";

    // Singleton instance
    private static CountryQuizDbHelper helperInstance;

    // SQL statement to create countries table
    private static final String CREATE_COUNTRIES =
            "create table if not exists " + TABLE_COUNTRIES + " ("
                    + COUNTRIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRIES_COLUMN_NAME + " TEXT NOT NULL, "
                    + COUNTRIES_COLUMN_CAPITAL + " TEXT NOT NULL, "
                    + COUNTRIES_COLUMN_CONTINENT + " TEXT NOT NULL, "
                    + COUNTRIES_COLUMN_CODE + " TEXT"
                    + ")";

    // SQL statement to create results table
    private static final String CREATE_RESULTS =
            "create table if not exists " + TABLE_RESULTS + " ("
                    + RESULTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RESULTS_COLUMN_DATE + " TEXT NOT NULL, "
                    + RESULTS_COLUMN_SCORE + " INTEGER NOT NULL"
                    + ")";

    /**
     * Private constructor so no other class can directly create
     * a new helper object.
     *
     * @param context application context
     */
    private CountryQuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Returns the single instance of the database helper.
     *
     * @param context application context
     * @return the single CountryQuizDbHelper instance
     */
    public synchronized static CountryQuizDbHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new CountryQuizDbHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    /**
     * Creates the database tables if they do not already exist.
     *
     * @param db SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTRIES);
        db.execSQL(CREATE_RESULTS);
        Log.d(DEBUG_TAG, "Tables " + TABLE_COUNTRIES + " and " + TABLE_RESULTS + " created");
    }

    /**
     * Upgrades the database schema if the database version changes.
     *
     * @param db SQLite database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_COUNTRIES);
        db.execSQL("drop table if exists " + TABLE_RESULTS);
        onCreate(db);
        Log.d(DEBUG_TAG, "Database upgraded");
    }

    /**
     * Returns all countries from the countries table.
     *
     * @return list of all countries
     */
    public ArrayList<Country> getAllCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_COUNTRIES,
                new String[]{
                        COUNTRIES_COLUMN_ID,
                        COUNTRIES_COLUMN_NAME,
                        COUNTRIES_COLUMN_CAPITAL,
                        COUNTRIES_COLUMN_CONTINENT
                },
                null,
                null,
                null,
                null,
                COUNTRIES_COLUMN_NAME
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String country = cursor.getString(1);
                String capital = cursor.getString(2);
                String continent = cursor.getString(3);

                countries.add(new Country(id, country, capital, continent));
            }
            cursor.close();
        }

        Log.d(DEBUG_TAG, "Retrieved " + countries.size() + " countries");
        return countries;
    }

    /**
     * Inserts a completed quiz result into the results table.
     *
     * @param date date and time of the quiz
     * @param score user score
     * @return true if insert was successful, false otherwise
     */
    public boolean insertQuizResult(String date, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RESULTS_COLUMN_DATE, date);
        values.put(RESULTS_COLUMN_SCORE, score);

        long rowId = db.insert(TABLE_RESULTS, null, values);
        Log.d(DEBUG_TAG, "Inserted quiz result row id: " + rowId);

        return rowId != -1;
    }

    /**
     * Returns all previously completed quiz results.
     *
     * @return list of quiz results
     */
    public ArrayList<QuizResult> getAllQuizResults() {
        ArrayList<QuizResult> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RESULTS,
                new String[]{
                        RESULTS_COLUMN_ID,
                        RESULTS_COLUMN_DATE,
                        RESULTS_COLUMN_SCORE
                },
                null,
                null,
                null,
                null,
                RESULTS_COLUMN_ID + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                int score = cursor.getInt(2);

                results.add(new QuizResult(id, date, score));
            }
            cursor.close();
        }

        Log.d(DEBUG_TAG, "Retrieved " + results.size() + " quiz results");
        return results;
    }
}