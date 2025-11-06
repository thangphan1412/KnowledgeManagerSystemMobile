package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.SopsAdapter;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.status.RoleName;
import com.abc.knowledgemanagersystems.status.StatusExperiment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class ProtocolActivity extends AppCompatActivity {
    boolean USE_MOCK_DATA = true;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    private SopsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_repository);

        recyclerView = findViewById(R.id.recyclerViewProtocols);
        searchView = findViewById(R.id.searchViewProtocol);
        fabAdd = findViewById(R.id.fabAddProtocol);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//         Load data from Room database
//        Executors.newSingleThreadExecutor().execute(() -> {
//            List<Sops> list = AppDataBase.getInstance(this).sopsDao().getAllSops();
//            runOnUiThread(() -> {
//                adapter = new SopsAdapter(this, list, sop -> {
//                    // Handle click -> open details
//                });
//                recyclerView.setAdapter(adapter);
//            });
//        });

        if (USE_MOCK_DATA) {
            // Fake data for testing
            List<Sops> mockList = new ArrayList<>();
            mockList.add(new Sops(1, "SOP-001", "DNA Extraction Protocol",
                    "Standard procedure for DNA extraction using ethanol precipitation.",
                    "2025-10-20", "/path/dna_extraction.pdf", "Always wear gloves and goggles.", 0));

            mockList.add(new Sops(2, "SOP-002", "Cell Culture Handling",
                    "Procedure for aseptic handling of mammalian cell cultures.",
                    "2025-10-22", "/path/cell_culture.pdf", "Sterilize workspace and use biosafety cabinet.", 0));

            mockList.add(new Sops(3, "SOP-003", "PCR Amplification",
                    "Steps for preparing and running polymerase chain reaction (PCR).",
                    "2025-10-25", "/path/pcr_protocol.pdf", "Avoid contamination of template DNA.", 0));

            adapter = new SopsAdapter(this, mockList, sop -> {
                Intent intent = new Intent(ProtocolActivity.this, ViewProtocolActivity.class);
                intent.putExtra("SOP_ID", sop.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);

        } else {
            // Normal mode: Load from Room DB
            Executors.newSingleThreadExecutor().execute(() -> {
                List<Sops> list = AppDataBase.getInstance(this).sopsDao().getAllSops();
                runOnUiThread(() -> {
                    adapter = new SopsAdapter(this, list, sop -> {
                        Intent intent = new Intent(ProtocolActivity.this, ViewProtocolActivity.class);
                        intent.putExtra("SOP_ID", sop.getId());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                });
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.filter(newText);
                return true;
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewProtocolActivity.class);
            startActivity(intent);
        });



    }
}