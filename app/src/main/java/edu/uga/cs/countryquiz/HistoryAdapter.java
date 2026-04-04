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

    public HistoryAdapter(List<String> historyData) {
        this.historyData = historyData;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        // Bind the string data to the TextView
        String currentResult = historyData.get(position);
        holder.quizNumHolder.setText("Quiz " + (position+1) + ":");
        holder.historyTextView.setText(currentResult);
    }

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

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyTextView = itemView.findViewById(R.id.historyText);
            quizNumHolder = itemView.findViewById(R.id.textView2);
        }
    }
}