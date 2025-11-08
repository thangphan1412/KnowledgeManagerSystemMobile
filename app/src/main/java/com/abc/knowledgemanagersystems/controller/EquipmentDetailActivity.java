package com.abc.knowledgemanagersystems.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer; // <-- THÊM IMPORT NÀY
import androidx.lifecycle.ViewModelProvider; // <-- THÊM IMPORT NÀY
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Equipment; // <-- THÊM IMPORT NÀY
import com.abc.knowledgemanagersystems.viewmodel.EquipmentViewModel; // <-- THÊM IMPORT NÀY

public class EquipmentDetailActivity extends AppCompatActivity {

    // --- Khai báo các View ---
    private TextView textViewEquipmentName;
    private TextView textViewModel;
    private TextView textViewSerial;
    private TextView textViewEquipmentStatus;
    private Button buttonDownloadManual;
    private Button buttonGoToBooking;
    private RecyclerView recyclerViewMaintenanceLogs;

    // --- Khai báo ViewModel ---
    private EquipmentViewModel mViewModel;

    // Biến lưu ID
    private int equipmentId = -1; // <-- Sửa thành int

    // (Biến lưu URL của file PDF - Sẽ lấy từ database)
    private String manualUrl = "https://www.agilent.com/cs/library/usermanuals/public/G7100-90030_InfinityLab-LC-Series-SitePrep_RevD_EN.pdf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_detail);

        // 1. LẤY VIEWMODEL (TỪ BƯỚC 3)
        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);

        // 2. ÁNH XẠ VIEWS
        // (Sử dụng ID từ layout 'activity_equipment_detail.xml' mà tôi đã gửi)
        textViewEquipmentName = findViewById(R.id.text_view_equipment_name);
        textViewModel = findViewById(R.id.text_view_model);
        textViewSerial = findViewById(R.id.text_view_serial);
        textViewEquipmentStatus = findViewById(R.id.text_view_equipment_status);
        buttonDownloadManual = findViewById(R.id.button_download_manual);
        buttonGoToBooking = findViewById(R.id.button_go_to_booking);
        recyclerViewMaintenanceLogs = findViewById(R.id.recycler_view_maintenance_logs);

        // 3. LẤY ID TỪ INTENT (SỬA LỖI Ở ĐÂY)
        // Phải dùng getIntExtra vì ListActivity gửi 'int'
        equipmentId = getIntent().getIntExtra("EQUIPMENT_ID", -1);

        if (equipmentId == -1) {
            // Đây chính là lỗi "Không tìm thấy ID"
            Toast.makeText(this, "Lỗi: Không tìm thấy ID thiết bị", Toast.LENGTH_LONG).show();
            finish(); // Đóng Activity
            return;
        }

        // 4. TẢI DỮ LIỆU TỪ DATABASE (THAY THẾ CODE CỨNG)
        mViewModel.getEquipmentById(equipmentId).observe(this, new Observer<Equipment>() {
            @Override
            public void onChanged(Equipment equipment) {
                // Khi Room tìm thấy dữ liệu, cập nhật UI
                if (equipment != null) {
                    textViewEquipmentName.setText(equipment.getName());
                    textViewModel.setText("Model: " + equipment.getModel());

                    // TODO: Cập nhật các trường này khi bạn thêm chúng vào model
                    textViewSerial.setText("Serial #: US12345678");
                    textViewEquipmentStatus.setText("Trạng thái: Sẵn sàng");
                }
            }
        });

        // 5. Cài đặt các trình lắng nghe sự kiện
        setupClickListeners();

        // 6. Cài đặt RecyclerView cho Nhật ký
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
            Intent intent = new Intent(EquipmentDetailActivity.this, EquipmentBookingActivity.class);
            // Gửi ID (đã là int) sang màn hình Đặt lịch
            intent.putExtra("EQUIPMENT_ID", equipmentId);
            intent.putExtra("EQUIPMENT_NAME", textViewEquipmentName.getText().toString());
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