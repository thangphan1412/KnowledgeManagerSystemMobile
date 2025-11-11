package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;

// ✅ IMPORT ĐÚNG MODEL ENTITY TỪ PACKAGE MODEL
import com.abc.knowledgemanagersystems.model.Experiment;

import java.util.List;

public class ExperimentAdapter extends RecyclerView.Adapter<ExperimentAdapter.ExperimentViewHolder> {

    private List<Experiment> experimentList;
    private final ExperimentClickListener listener;

    public interface ExperimentClickListener {
        // ✅ PHƯƠNG THỨC NÀY PHẢI DÙNG MODEL.EXPERIMENT
        void onExperimentClicked(Experiment experiment);
    }

    public ExperimentAdapter(List<Experiment> experimentList, ExperimentClickListener listener) {
        this.experimentList = experimentList;
        this.listener = listener;
    }

    // Phương thức mới để cập nhật danh sách
    public void updateList(List<Experiment> newList) {
        this.experimentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExperimentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Giả định bạn có layout item_experiment_card.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_experiment_card, parent, false);
        return new ExperimentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperimentViewHolder holder, int position) {
        // ✅ PHƯƠNG THỨC NÀY PHẢI DÙNG MODEL.EXPERIMENT
        Experiment experiment = experimentList.get(position);

        holder.titleTextView.setText(experiment.getTitle());
        // ... (Thêm các trường khác nếu có)

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> listener.onExperimentClicked(experiment));
    }

    @Override
    public int getItemCount() {
        return experimentList != null ? experimentList.size() : 0;
    }

    public static class ExperimentViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public ExperimentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_experiment_title);
        }
    }
}