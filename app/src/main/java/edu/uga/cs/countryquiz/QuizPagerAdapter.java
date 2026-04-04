package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
/**
 * QuizPagerAdapter manages the fragments shown in the ViewPager2.
 *
 * It is responsible for creating QuizFragment instances for each question,
 * creating the ResultsFragment at the end, and passing the saved
 * quiz ID to the results screen.
 *
 * Total pages:
 * - 6 question fragments
 * - 1 results fragment
 */
public class QuizPagerAdapter extends FragmentStateAdapter {
    private long savedQuizId = -1; //keep track of id to pass into resultsfragment
    /**
     * Constructor for the adapter.
     *
     * @param fragmentActivity the activity hosting the ViewPager2
     */
    public QuizPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Called by QuizActivity after the quiz is saved to the database.
     * This allows the ResultsFragment to receive the saved quiz ID.
     *
     * @param id database ID of the saved quiz
     */
    public void setSavedQuizId(long id) {
        this.savedQuizId = id;
    }
    /**
     * Creates the fragment for a given position.
     *
     * Positions:
     * 0–5 → Quiz questions
     * 6   → Results screen
     *
     * @param position index of the page
     * @return corresponding Fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < 6) {
            return QuizFragment.newInstance(position);
        } else {
            // passes the id to new results fragment
            return ResultsFragment.newInstance(savedQuizId);
        }
    }
    /**
     * Returns total number of pages in the ViewPager.
     *
     * @return total page count (6 questions + 1 results page)
     */
    @Override
    public int getItemCount() {
        return 7;
    }
}
