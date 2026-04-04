package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
/**
 * MainActivity is the entry screen of the Country Quiz app.
 */
public class MainActivity extends AppCompatActivity {

    Button button2;
    Button historyBtn;
    /**
     * Called when the activity is first created.
     * This method sets the layout, copies the database if needed,
     * and sets up button click listeners for navigation.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This ensures the file is moved from assets to the phone BEFORE the quiz starts
        new InitialDbHelper(this).copyDatabaseIfNeeded();

        //to start a new quiz
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        });

        //to handle going to prior quizzes view
        historyBtn = findViewById(R.id.button);
        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}