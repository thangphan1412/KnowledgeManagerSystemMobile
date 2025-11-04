package com.abc.knowledgemanagersystems.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent; // <-- THÊM IMPORT NÀY
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;
import android.widget.Button;
// import android.widget.CalendarView; // XÓA
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;

public class EquipmentDetailActivity extends AppCompatActivity {

    // --- Khai báo các View (Đã xóa các view Đặt lịch) ---
    private TextView textViewEquipmentName;
    private TextView textViewModel;
    private TextView textViewSerial;
    private TextView textViewEquipmentStatus;
    private Button buttonDownloadManual;
    private Button buttonGoToBooking; // MỚI
    private RecyclerView recyclerViewMaintenanceLogs;

    // Biến lưu ID (Sẽ nhận từ Intent)
    private long equipmentId = -1; // Ví dụ: 123L

    // (Biến lưu URL của file PDF - Sẽ lấy từ database)
    private String manualUrl = "https://www.agilent.com/cs/library/usermanuals/public/G7100-90030_InfinityLab-LC-Series-SitePrep_RevD_EN.pdf";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Nạp file layout
        setContentView(R.layout.activity_equipment_detail);

        // TODO: Nhận equipmentId được gửi từ HomeActivity
        // equipmentId = getIntent().getLongExtra("EQUIPMENT_ID", -1);

        // 2. Ánh xạ (tìm) các View
        textViewEquipmentName = findViewById(R.id.text_view_equipment_name);
        textViewModel = findViewById(R.id.text_view_model);
        textViewSerial = findViewById(R.id.text_view_serial);
        textViewEquipmentStatus = findViewById(R.id.text_view_equipment_status);
        buttonDownloadManual = findViewById(R.id.button_download_manual);
        buttonGoToBooking = findViewById(R.id.button_go_to_booking); // MỚI
        recyclerViewMaintenanceLogs = findViewById(R.id.recycler_view_maintenance_logs);

        // 3. Tải dữ liệu (Tạm thời)
        textViewEquipmentName.setText("HPLC Machine #2");
        textViewModel.setText("Model: Agilent 1260");
        textViewSerial.setText("Serial #: US12345678");
        textViewEquipmentStatus.setText("Trạng thái: Sẵn sàng");

        // 4. Cài đặt các trình lắng nghe sự kiện
        setupClickListeners();

        // 5. Cài đặt RecyclerView cho Nhật ký
        setupLogRecyclerView();
    }

    private void setupClickListeners() {

        // --- Nút Tải Hướng dẫn ---
        buttonDownloadManual.setOnClickListener(v -> {
            Toast.makeText(this, "Bắt đầu tải Hướng dẫn...", Toast.LENGTH_SHORT).show();
            startManualDownload();
        });

        // --- NÚT MỚI: Chuyển sang Màn hình Đặt lịch ---
        buttonGoToBooking.setOnClickListener(v -> {
            // Mở Activity Đặt lịch mới
            Intent intent = new Intent(EquipmentDetailActivity.this, EquipmentBookingActivity.class);

            // Gửi ID của thiết bị này sang màn hình Đặt lịch
            intent.putExtra("EQUIPMENT_ID", equipmentId);
            intent.putExtra("EQUIPMENT_NAME", textViewEquipmentName.getText().toString()); // Gửi cả tên cho tiện

            startActivity(intent);
        });
    }

    private void setupLogRecyclerView() {
        recyclerViewMaintenanceLogs.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Cài đặt Adapter và load data
    }

    private void startManualDownload() {
        // ... (Hàm download giữ nguyên y như cũ) ...
        if (manualUrl == null || manualUrl.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy URL tài liệu", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(manualUrl));
            request.setTitle("User Manual - " + textViewEquipmentName.getText());
            request.setDescription("Đang tải tệp PDF...");
            request.setMimeType("application/pdf");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "equipment_manual.pdf");
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}