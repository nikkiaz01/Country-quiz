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
/**
 * QuizFragment displays one quiz question at a time.
 *
 * This fragment receives a question number through arguments,
 * gets the current Quiz object from QuizActivity,
 * displays the question and answer choices,
 * checks the selected answer, updates the score in the activity,
 * and enables moving to the next page after an answer is selected.
 */
public class QuizFragment extends Fragment {
    private int questionNum;
    private Quiz currentQuiz;
    /**
     * Creates a new QuizFragment for a specific question.
     *
     * @param questionNum index of the question to display
     * @return configured QuizFragment instance
     */
    public static QuizFragment newInstance(int questionNum) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNum", questionNum);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Called when the fragment is first created.
     * Retrieves the question number from the arguments bundle.
     *
     * @param savedInstanceState previously saved fragment state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) questionNum = getArguments().getInt("questionNum");
    }
    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater used to inflate XML layout
     * @param container parent view group
     * @param savedInstanceState previously saved state
     * @return root view for this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }
    /**
     * Called after the fragment view has been created.
     * This method gets the current quiz from the activity,
     * displays the question, and listens for answer selection.
     *
     * @param view fragment root view
     * @param savedInstanceState previously saved state
     */
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
                //checks on if it is a match since begins with an A. or B. so need endsWith method
                int score = selectedText.endsWith(correct) ? 1 : 0;
                if (activity != null) {
                    activity.updateMainScore(questionNum, score);
                    androidx.viewpager2.widget.ViewPager2 myPager = activity.findViewById(R.id.viewpager2);
                    // null check for pager
                    if (myPager != null) {
                        myPager.setUserInputEnabled(true);
                    }
                }
            }
        });
    }
    /**
     * Displays the question text and shuffled answer choices.
     *
     * @param view fragment root view
     */
    private void displayQuestion(View view) {
        Question q = currentQuiz.getQuestions().get(questionNum);
        TextView questionNumber = view.findViewById(R.id.textView3);
        questionNumber.setText("Question " + (questionNum + 1));
        // Show the question prompt
        ((TextView)view.findViewById(R.id.questionTitleText)).setText("Name the capital city of " + q.getCountryName());
        // Put all answer choices into a list
        ArrayList<String> options = new ArrayList<>();
        options.add(q.getCapital());
        options.add(q.getOtherCity1());
        options.add(q.getOtherCity2());
        // Shuffle choices so the correct answer is not always in same position
        Collections.shuffle(options);

        // set text of all radio buttons
        ((RadioButton)view.findViewById(R.id.radioButton)).setText("A. " + options.get(0));
        ((RadioButton)view.findViewById(R.id.radioButton2)).setText("B. " + options.get(1));
        ((RadioButton)view.findViewById(R.id.radioButton3)).setText("C. " + options.get(2));
    }
}