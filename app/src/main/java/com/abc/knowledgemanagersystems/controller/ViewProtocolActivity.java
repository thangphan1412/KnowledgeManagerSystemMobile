package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.StepsAdapter;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Sops;

import java.util.List;
import java.util.concurrent.Executors;

public class ViewProtocolActivity extends AppCompatActivity {

    private TextView tvProtocolTitle, tvProtocolIntro, tvWarnings;
    private RecyclerView recyclerViewSteps;
    private TextView btnStartExperiment;
    private ImageButton btnBack;

    private Sops selectedSop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_details);

        tvProtocolTitle = findViewById(R.id.tvProtocolTitle);
        tvProtocolIntro = findViewById(R.id.tvProtocolIntro);
        tvWarnings = findViewById(R.id.tvWarnings);
        recyclerViewSteps = findViewById(R.id.recyclerViewSteps);
        btnStartExperiment = findViewById(R.id.btnStartExperiment);
        btnBack = findViewById(R.id.btnBack);

        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));

        // Lấy protocol ID từ intent
        int sopId = getIntent().getIntExtra("SOP_ID", -1);
        if (sopId != -1) {
            loadSopDetails(sopId);
        }

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
    }

    private void loadSopDetails(int sopId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            selectedSop = AppDataBase.getInstance(this).sopsDao().getAllSops()
                    .stream()
                    .filter(sop -> sop.getId() == sopId)
                    .findFirst()
                    .orElse(null);

            if (selectedSop != null) {
                runOnUiThread(() -> {
                    tvProtocolTitle.setText(selectedSop.getTitle());
                    tvProtocolIntro.setText(selectedSop.getDescription());
                    tvWarnings.setText(selectedSop.getSafeDataSheet() != null ? selectedSop.getSafeDataSheet() : "No warnings available.");

                    // Load steps (giả sử steps được lưu trong filePath hoặc bạn có thể tạo table riêng)
                    // Tạm thời hiển thị các bước mẫu
                    List<String> steps = List.of(
                            "Step 1: Prepare materials",
                            "Step 2: Mix solution",
                            "Step 3: Incubate for 30 min",
                            "Step 4: Observe results"
                    );

                    StepsAdapter adapter = new StepsAdapter(steps);
                    recyclerViewSteps.setAdapter(adapter);
                });
            }
        });
    }
}