package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
/**
 * ResultsFragment displays the final quiz score.
 */
public class ResultsFragment extends Fragment {
    private long quizId;
    /**
     * Creates a new instance of ResultsFragment.
     *
     * @param id database ID of the saved quiz
     * @return configured ResultsFragment
     */
    public static ResultsFragment newInstance(long id) {
        ResultsFragment f = new ResultsFragment();
        Bundle b = new Bundle();
        b.putLong("quiz_id", id);
        f.setArguments(b);
        return f;
    }
    /**
     * Inflates the fragment layout.
     *
     * @param i LayoutInflater
     * @param c container ViewGroup
     * @param s saved instance state
     * @return root view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle s) {
        return i.inflate(R.layout.fragment_results, c, false);
    }
    /**
     * Called after the view has been created.
     *
     * This method retrieves quiz ID from arguments,
     * gets the final score from the activity,
     * displays the score, sets up button to return to main screen.
     *
     * @param view fragment root view
     * @param savedInstanceState previously saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) quizId = getArguments().getLong("quiz_id");

        QuizActivity activity = (QuizActivity) getActivity();
        if (activity != null) {
            int score = activity.getTotalScore();
            double percentage = (score * 100.0) / 6;
            String percentString = String.format("%.1f%%", percentage);
            ((TextView)view.findViewById(R.id.scoreTextView)).setText("Final Score: " + score + "/6 = " + percentString);
        }
        Button homeBtn = view.findViewById(R.id.button3);
        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }
}