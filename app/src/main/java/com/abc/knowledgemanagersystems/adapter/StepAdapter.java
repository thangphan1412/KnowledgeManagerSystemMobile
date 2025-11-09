package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> steps;
    private OnStepActionListener listener;

    public interface OnStepActionListener {
        void onDeleteStep(Step step);
    }

    public StepAdapter(List<Step> steps, OnStepActionListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.tvStepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.tvStepTitle.setText("Step " + step.getStepNumber());
        holder.tvStepDescription.setText(step.getDescription());

        if (step.getSafetyNote() != null && !step.getSafetyNote().isEmpty()) {
            holder.layoutSafetyNote.setVisibility(View.VISIBLE);
            holder.tvSafetyNote.setText(step.getSafetyNote());
        } else {
            holder.layoutSafetyNote.setVisibility(View.GONE);
        }

        holder.btnDeleteStep.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteStep(step);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView tvStepNumber, tvStepTitle, tvStepDescription, tvSafetyNote;
        ImageButton btnDeleteStep;
        View layoutSafetyNote;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tvStepNumber);
            tvStepTitle = itemView.findViewById(R.id.tvStepTitle);
            tvStepDescription = itemView.findViewById(R.id.tvStepDescription);
            tvSafetyNote = itemView.findViewById(R.id.tvSafetyNote);
            btnDeleteStep = itemView.findViewById(R.id.btnDeleteStep);
            layoutSafetyNote = itemView.findViewById(R.id.layoutSafetyNote);
        }
    }
}
