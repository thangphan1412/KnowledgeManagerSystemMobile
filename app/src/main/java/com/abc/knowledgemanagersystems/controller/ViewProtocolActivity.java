package com.abc.knowledgemanagersystems.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.StepAdapter;
import com.abc.knowledgemanagersystems.adapter.StepsAdapter;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ViewProtocolActivity extends AppCompatActivity {

    private TextView tvProtocolTitle, tvProtocolIntro, tvWarnings;
    private RecyclerView recyclerViewSteps;
    private TextView btnStartExperiment;
    private ImageButton btnBack;
    private FloatingActionButton fabAddSteps;

    private Sops selectedSop;
    private List<Step> stepsList;
    private StepAdapter stepsAdapter;
    private int currentSopId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_details);

        initViews();
        setupRecyclerView();

        // Lấy protocol ID từ intent
        currentSopId = getIntent().getIntExtra("SOP_ID", -1);
        if (currentSopId != -1) {
            loadSopDetails(currentSopId);
            loadSteps(currentSopId);
        } else {
            Toast.makeText(this, "Error: Invalid protocol", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupListeners();
    }

    private void initViews() {
        tvProtocolTitle = findViewById(R.id.tvProtocolTitle);
        tvProtocolIntro = findViewById(R.id.tvProtocolIntro);
        tvWarnings = findViewById(R.id.tvWarnings);
        recyclerViewSteps = findViewById(R.id.recyclerViewSteps);
        btnStartExperiment = findViewById(R.id.btnStartExperiment);
        btnBack = findViewById(R.id.btnBack);
        fabAddSteps = findViewById(R.id.fabAddSteps);
    }

    private void setupRecyclerView() {
        stepsList = new ArrayList<>();
        stepsAdapter = new StepAdapter(stepsList, this::deleteStep);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSteps.setAdapter(stepsAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnStartExperiment.setOnClickListener(v -> {
            if (selectedSop != null) {
                Intent intent = new Intent(ViewProtocolActivity.this, CreateExperimentActivity.class);
                intent.putExtra("SOP_ID", selectedSop.getId());
                intent.putExtra("SOP_TITLE", selectedSop.getTitle());
                intent.putExtra("SOP_DESCRIPTION", selectedSop.getDescription());
                startActivity(intent);
            }
        });

        fabAddSteps.setOnClickListener(v -> showAddStepDialog());
    }

    private void loadSopDetails(int sopId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            selectedSop = AppDataBase.getInstance(this).sopsDao().getSopById(sopId);

            if (selectedSop != null) {
                runOnUiThread(() -> {
                    tvProtocolTitle.setText(selectedSop.getTitle());
                    tvProtocolIntro.setText(selectedSop.getDescription());
                    tvWarnings.setText(selectedSop.getSafeDataSheet() != null && !selectedSop.getSafeDataSheet().isEmpty()
                            ? selectedSop.getSafeDataSheet()
                            : "No warnings available.");
                });
            }
        });
    }

    private void loadSteps(int sopId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Step> steps = AppDataBase.getInstance(this).stepDao().getStepsBySopId(sopId);

            runOnUiThread(() -> {
                stepsList.clear();
                stepsList.addAll(steps);
                stepsAdapter.notifyDataSetChanged();
            });
        });
    }

    private void showAddStepDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_step);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText edtStepNumber = dialog.findViewById(R.id.edtStepNumber);
        EditText edtStepDescription = dialog.findViewById(R.id.edtStepDescription);
        EditText edtSafetyNote = dialog.findViewById(R.id.edtSafetyNote);
        CardView btnAddStep = dialog.findViewById(R.id.btnAddStep);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);

        // Auto-fill next step number
        edtStepNumber.setText(String.valueOf(stepsList.size() + 1));

        btnAddStep.setOnClickListener(v -> {
            String stepNumberStr = edtStepNumber.getText().toString().trim();
            String description = edtStepDescription.getText().toString().trim();
            String safetyNote = edtSafetyNote.getText().toString().trim();

            // Validation
            if (stepNumberStr.isEmpty()) {
                Toast.makeText(this, "Please enter step number", Toast.LENGTH_SHORT).show();
                return;
            }

            int stepNumber;
            try {
                stepNumber = Integer.parseInt(stepNumberStr);
                if (stepNumber <= 0) {
                    Toast.makeText(this, "Step number must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid step number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter step description", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new step
            Step newStep = new Step();
            newStep.setStepNumber(stepNumber);
            newStep.setDescription(description);
            newStep.setSafetyNote(safetyNote.isEmpty() ? null : safetyNote);
            newStep.setSopId(currentSopId);

            addStep(newStep);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        // Set dialog width
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void addStep(Step step) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                long stepId = AppDataBase.getInstance(this).stepDao().insert(step);
                step.setId((int) stepId);

                runOnUiThread(() -> {
                    stepsList.add(step);
                    // Sort by step number
                    stepsList.sort((s1, s2) -> Integer.compare(s1.getStepNumber(), s2.getStepNumber()));
                    stepsAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "Step added successfully", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error adding step: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void deleteStep(Step step) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Step")
                .setMessage("Are you sure you want to delete Step " + step.getStepNumber() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            AppDataBase.getInstance(this).stepDao().delete(step);

                            runOnUiThread(() -> {
                                stepsList.remove(step);
                                stepsAdapter.notifyDataSetChanged();
                                Toast.makeText(this, "Step deleted", Toast.LENGTH_SHORT).show();
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Error deleting step: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload steps when returning to this activity
        if (currentSopId != -1) {
            loadSteps(currentSopId);
        }
    }
}