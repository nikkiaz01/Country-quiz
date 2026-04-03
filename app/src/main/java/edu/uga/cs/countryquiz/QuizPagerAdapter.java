package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizPagerAdapter extends FragmentStateAdapter {
    private long savedQuizId = -1;

    public QuizPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Method for Activity to call once DB save is done
    public void setSavedQuizId(long id) {
        this.savedQuizId = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < 6) {
            return QuizFragment.newInstance(position);
        } else {
            // PASS THE ID TO THE RESULTS FRAGMENT
            return ResultsFragment.newInstance(savedQuizId);
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
