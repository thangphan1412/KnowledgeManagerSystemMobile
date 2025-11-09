package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.SDSAdapter;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.db.AppDataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SDSLookupActivity extends AppCompatActivity implements SDSAdapter.OnSDSClickListener {

    private EditText searchViewSDS;
    private RecyclerView recyclerViewSDS;
    private LinearLayout emptyStateView;
    private FrameLayout loadingView;
    private SDSAdapter adapter;
    private List<Sops> sdsList = new ArrayList<>();
    private AppDataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sds_lookup);

        // Init UI
        searchViewSDS = findViewById(R.id.searchViewSDS);
        recyclerViewSDS = findViewById(R.id.recyclerViewSDS);
        emptyStateView = findViewById(R.id.emptyStateView);
        loadingView = findViewById(R.id.loadingView);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Database instance
        db = AppDataBase.getInstance(this);

        // RecyclerView setup
        adapter = new SDSAdapter(sdsList, this);
        recyclerViewSDS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSDS.setAdapter(adapter);

        // Search listener
        searchViewSDS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    fetchSDSFromDB(s.toString());
                } else {
                    sdsList.clear();
                    adapter.notifyDataSetChanged();
                    showEmptyState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchSDSFromDB(String query) {
        showLoading(true);

        new Thread(() -> {
            List<Sops> results = db.sopsDao().searchSDS("%" + query + "%"); // Tạo query LIKE tên hóa chất

            runOnUiThread(() -> {
                sdsList.clear();
                sdsList.addAll(results);
                adapter.notifyDataSetChanged();
                showLoading(false);

                if (results.isEmpty()) {
                    showEmptyState();
                } else {
                    emptyStateView.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private void showLoading(boolean isLoading) {
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState() {
        emptyStateView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewSDSClick(Sops sops) {
        String pdfPath = sops.getSafeDataSheet();
        if (pdfPath != null && !pdfPath.isEmpty()) {
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                try {
                    Uri pdfUri = androidx.core.content.FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".provider",
                            pdfFile
                    );

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No SDS PDF linked", Toast.LENGTH_SHORT).show();
        }
    }
}