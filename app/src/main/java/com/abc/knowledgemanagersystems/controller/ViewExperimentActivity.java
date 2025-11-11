package com.abc.knowledgemanagersystems.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Experiment;
// (Bạn cần import enum StatusExperiment của bạn)
// import com.abc.knowledgemanagersystems.status.StatusExperiment;
import com.abc.knowledgemanagersystems.viewmodel.ExperimentReportViewModel;

public class ViewExperimentActivity extends AppCompatActivity {

    private ExperimentReportViewModel mViewModel;
    private Button buttonExportPdf;
    private TextView textViewTitle, textViewStatus;

    private int mExperimentId = -1; // Giả sử ID được truyền qua Intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_details); // Sử dụng layout ở Bước 2

        // (Giả sử bạn truyền ID của thí nghiệm vào Activity này)
        mExperimentId = getIntent().getIntExtra("EXPERIMENT_ID", -1);
        if (mExperimentId == -1) {
            Toast.makeText(this, "Lỗi: Không có ID Thí nghiệm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        buttonExportPdf = findViewById(R.id.button_export_pdf);
        textViewTitle = findViewById(R.id.text_view_experiment_title);
        textViewStatus = findViewById(R.id.text_view_experiment_status);

        mViewModel = new ViewModelProvider(this).get(ExperimentReportViewModel.class);

        // Bắt đầu xử lý logic
        setupObservers();
        mViewModel.loadExperimentDetails(mExperimentId); // 1. Tải dữ liệu

        // 3. Xử lý sự kiện nhấn nút
        buttonExportPdf.setOnClickListener(v -> {
            Toast.makeText(this, "Đang gửi yêu cầu lên server...", Toast.LENGTH_SHORT).show();
            v.setEnabled(false); // Vô hiệu hóa nút
            mViewModel.startPdfExport(mExperimentId); // Gọi "server"
        });
    }

    private void setupObservers() {
        // 2. Lắng nghe dữ liệu Thí nghiệm trả về
        mViewModel.getExperiment().observe(this, experiment -> {
            if (experiment == null) return;

            textViewTitle.setText(experiment.getTitle());

            // (Bạn cần dùng enum StatusExperiment thật của bạn)
            // StatusExperiment status = experiment.getStatusExperiment();
            // textViewStatus.setText("Trạng thái: " + status.name());

            // 🔥 LOGIC CỐT LÕI (UI):
            // Chỉ hiển thị nút "Export" nếu thí nghiệm đã hoàn thành
            // if (status == StatusExperiment.COMPLETED) {
            //     buttonExportPdf.setVisibility(View.VISIBLE);
            // } else {
            //     buttonExportPdf.setVisibility(View.GONE);
            // }

            // --- Giả lập cho demo ---
            // (Bạn hãy xóa 2 dòng giả lập này khi dùng enum thật)
            textViewStatus.setText("Trạng thái: COMPLETED (Demo)");
            buttonExportPdf.setVisibility(View.VISIBLE);
            // --- Hết Giả lập ---
        });

        // 4. Lắng nghe link PDF trả về từ "server"
        mViewModel.getPdfDownloadUrl().observe(this, downloadUrl -> {
            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                Toast.makeText(this, "Server đã trả về link, đang tải...", Toast.LENGTH_LONG).show();
                // 5. Dùng DownloadManager để tải file
                startDownload(downloadUrl);
            }
        });

        // (Tùy chọn) Lắng nghe lỗi
        mViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                buttonExportPdf.setEnabled(true); // Kích hoạt lại nút nếu lỗi
            }
        });
    }

    /**
     * 5. Tái sử dụng logic DownloadManager (từ EquipmentDetailActivity).
     */
    private void startDownload(String pdfUrl) {
        String fileName = "Experiment_Report_" + mExperimentId + ".pdf";

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
            request.setTitle(fileName);
            request.setDescription("Đang tải Báo cáo Thí nghiệm...");
            request.setMimeType("application/pdf");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);

                // (Sau khi tải xong, bạn có thể chuyển về trang trước)
                // finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            buttonExportPdf.setEnabled(true); // Kích hoạt lại nút nếu lỗi
        }
    }
}