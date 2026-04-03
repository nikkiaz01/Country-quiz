package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.Collections;

public class QuizFragment extends Fragment {
    private int questionNum;
    private Quiz currentQuiz;

    public static QuizFragment newInstance(int questionNum) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNum", questionNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) questionNum = getArguments().getInt("questionNum");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Link to the Activity's Quiz
        QuizActivity activity = (QuizActivity) getActivity();
        if (activity != null) {
            currentQuiz = activity.getCurrentQuiz();
            if (currentQuiz != null) displayQuestion(view);
        }

        RadioGroup rg = view.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && currentQuiz != null) {
                RadioButton selected = view.findViewById(checkedId);
                String selectedText = selected.getText().toString();
                String correct = currentQuiz.getQuestions().get(questionNum).getCapital();

                // FIX: Check if the string ends with the correct answer
                // This avoids "Mali" matching "Somalia"
                int score = selectedText.endsWith(correct) ? 1 : 0;

                if (activity != null) {
                    activity.updateMainScore(questionNum, score);
                    androidx.viewpager2.widget.ViewPager2 myPager = activity.findViewById(R.id.viewpager2);
                    // FIX: Null check for pager
                    if (myPager != null) {
                        myPager.setUserInputEnabled(true);
                    }
                }
            }
        });
    }

    private void displayQuestion(View view) {
        Question q = currentQuiz.getQuestions().get(questionNum);
        TextView questionNumber = view.findViewById(R.id.textView3);
        questionNumber.setText("Question " + (questionNum + 1));
        ((TextView)view.findViewById(R.id.questionTitleText)).setText("Name the capital city of " + q.getCountryName());

        ArrayList<String> options = new ArrayList<>();
        options.add(q.getCapital());
        options.add(q.getOtherCity1());
        options.add(q.getOtherCity2());
        Collections.shuffle(options);

        // Labels A, B, C
        ((RadioButton)view.findViewById(R.id.radioButton)).setText("A. " + options.get(0));
        ((RadioButton)view.findViewById(R.id.radioButton2)).setText("B. " + options.get(1));
        ((RadioButton)view.findViewById(R.id.radioButton3)).setText("C. " + options.get(2));
    }
}