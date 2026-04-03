package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ResultsFragment extends Fragment {
    private long quizId;

    public static ResultsFragment newInstance(long id) {
        ResultsFragment f = new ResultsFragment();
        Bundle b = new Bundle();
        b.putLong("quiz_id", id);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle s) {
        return i.inflate(R.layout.fragment_results, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) quizId = getArguments().getLong("quiz_id");

        QuizActivity activity = (QuizActivity) getActivity();
        if (activity != null) {
            int score = activity.getTotalScore();
            ((TextView)view.findViewById(R.id.scoreTextView)).setText("Final Score: " + score + "/6");
            // Optionally show the quizId from the database
        }
    }
}