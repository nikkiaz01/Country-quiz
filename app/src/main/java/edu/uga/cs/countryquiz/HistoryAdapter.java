package edu.uga.cs.countryquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter for the RecyclerView to display past quiz results.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<String> historyData;
    /**
     * Constructor to initialize adapter with data.
     *
     * @param historyData list of formatted quiz result strings
     */
    public HistoryAdapter(List<String> historyData) {
        this.historyData = historyData;
    }
    /**
     * Called when RecyclerView needs a new ViewHolder and
     * inflates the layout for each row (quiz_history_item.xml).
     *
     * @param parent parent view group
     * @param viewType type of view (not used here)
     * @return a new HistoryViewHolder
     */
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_history_item, parent, false);
        return new HistoryViewHolder(view);
    }
    /**
     * Binds data to the ViewHolder at a specific position.
     *
     * @param holder ViewHolder containing row views
     * @param position position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        // Bind the string data to the TextView
        String currentResult = historyData.get(position);
        holder.quizNumHolder.setText("Quiz " + (position+1) + ":");
        holder.historyTextView.setText(currentResult);
    }
    /**
     * Returns the number of items in the dataset.
     *
     * @return size of historyData list
     */
    @Override
    public int getItemCount() {
        return historyData != null ? historyData.size() : 0;
    }

    /**
     * ViewHolder class to hold references to the UI components for each row.
     */
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView historyTextView;
        TextView quizNumHolder;

        /**
         * Constructor that initializes view references.
         *
         * @param itemView the layout view for one row
         */
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views inside the row layout
            historyTextView = itemView.findViewById(R.id.historyText);
            quizNumHolder = itemView.findViewById(R.id.textView2);
        }
    }
}