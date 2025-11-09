package com.abc.knowledgemanagersystems.controller;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.SDSAdapter;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Sops;

import java.io.File;
import java.io.IOException;
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

    // PdfRenderer
    private FrameLayout pdfContainer;
    private ImageView pdfImageView;
    private Button btnClosePdf, btnPrevPage, btnNextPage;

    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    private int currentPageIndex = 0;

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

        pdfContainer = findViewById(R.id.pdfContainer);
        pdfImageView = findViewById(R.id.pdfImageView);
        btnClosePdf = findViewById(R.id.btnClosePdf);
        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);

        btnBack.setOnClickListener(v -> finish());

        btnClosePdf.setOnClickListener(v -> closePdfViewer());
        btnPrevPage.setOnClickListener(v -> showPage(currentPageIndex - 1));
        btnNextPage.setOnClickListener(v -> showPage(currentPageIndex + 1));

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
            List<Sops> results = db.sopsDao().searchSDS("%" + query + "%");
            runOnUiThread(() -> {
                sdsList.clear();
                sdsList.addAll(results);
                adapter.notifyDataSetChanged();
                showLoading(false);

                if (results.isEmpty()) showEmptyState();
                else emptyStateView.setVisibility(View.GONE);
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
            if (pdfFile.exists()) openPdfInApp(pdfFile);
            else Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No SDS PDF linked", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdfInApp(File file) {
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            currentPageIndex = 0;
            showPage(currentPageIndex);
            pdfContainer.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot open PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPage(int index) {
        if (pdfRenderer == null || index < 0 || index >= pdfRenderer.getPageCount()) return;

        if (currentPage != null) currentPage.close();
        currentPage = pdfRenderer.openPage(index);
        currentPageIndex = index;

        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfImageView.setImageBitmap(bitmap);

        btnPrevPage.setEnabled(index > 0);
        btnNextPage.setEnabled(index < pdfRenderer.getPageCount() - 1);
    }

    private void closePdfViewer() {
        pdfContainer.setVisibility(View.GONE);
        if (currentPage != null) currentPage.close();
        currentPage = null;
        if (pdfRenderer != null) pdfRenderer.close();
        pdfRenderer = null;
        try {
            if (parcelFileDescriptor != null) parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parcelFileDescriptor = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closePdfViewer();
    }
}
