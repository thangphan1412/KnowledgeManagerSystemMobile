package com.abc.knowledgemanagersystems.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log; // <-- THÊM IMPORT
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.MaintenanceLogAdapter;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import com.abc.knowledgemanagersystems.viewmodel.EquipmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class EquipmentDetailActivity extends AppCompatActivity {

    // --- THÊM TAG LOGGING ---
    private static final String TAG = "DownloadDebug";

    // --- Khai báo View (Khớp với layout model_165) ---
    private TextView textViewEquipmentName, textViewModel, textViewSerial, textViewEquipmentStatus;
    private Button buttonDownloadManual, buttonGoToBooking;
    private RecyclerView recyclerViewMaintenanceLogs;

    // --- Khai báo "Backend" ---
    private EquipmentViewModel mViewModel;
    private int equipmentId = -1;
    private String currentManualUrl = null;

    private MaintenanceLogAdapter mLogAdapter;
    private List<MaintenanceLog> mLogList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_detail);

        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);


        // KHỚP VỚI XML CỦA BẠN (Sửa 34 lỗi gạch đỏ)

        // Ánh xạ View

        textViewEquipmentName = findViewById(R.id.text_view_equipment_name);
        textViewModel = findViewById(R.id.text_view_model);
        textViewSerial = findViewById(R.id.text_view_serial);
        textViewEquipmentStatus = findViewById(R.id.text_view_equipment_status);
        buttonDownloadManual = findViewById(R.id.button_download_manual);
        buttonGoToBooking = findViewById(R.id.button_go_to_booking);
        recyclerViewMaintenanceLogs = findViewById(R.id.recycler_view_maintenance_logs);

        equipmentId = getIntent().getIntExtra("EQUIPMENT_ID", -1);
        if (equipmentId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 1. GỌI BACKEND (Lấy thông tin Equipment)
        mViewModel.getEquipmentById(equipmentId).observe(this, new Observer<Equipment>() {
            @Override
            public void onChanged(Equipment equipment) {
                if (equipment != null) {
                    textViewEquipmentName.setText(equipment.getName());
                    textViewModel.setText("Model: " + equipment.getModel());

                    // SỬA LỖI 1: Lấy link PDF từ database (backend)
                    currentManualUrl = equipment.getManualUrl();

                    // --- THÊM LOGGING ---
                    Log.d(TAG, "Database observed. URL cho thiết bị " + equipment.getName() + " là: " + currentManualUrl);
                } else {
                    Log.d(TAG, "Observer: Equipment với ID " + equipmentId + " là null.");
                }
            }
        });

        // 2. Cài đặt các nút bấm
        setupClickListeners();

        // 3. GỌI BACKEND (Lấy "Nhật ký Bảo trì")
        setupLogRecyclerView();
    }

    private void setupClickListeners() {
        // Nút Tải PDF
        buttonDownloadManual.setOnClickListener(v -> {
            // SỬA LỖI 1: Dùng link từ backend
            Log.d(TAG, "Nút Download đã được nhấn.");
            startManualDownload(currentManualUrl);
        });

        // Nút Đặt lịch
        buttonGoToBooking.setOnClickListener(v -> {
            Intent intent = new Intent(EquipmentDetailActivity.this, EquipmentBookingActivity.class);
            intent.putExtra("EQUIPMENT_ID", equipmentId);
            intent.putExtra("EQUIPMENT_NAME", textViewEquipmentName.getText().toString());
            startActivity(intent);
        });
    }

    /**
     * SỬA LỖI 2: Cài đặt "Nhật ký" (RecyclerView)
     */
    private void setupLogRecyclerView() {
        // 1. Khởi tạo Adapter với danh sách rỗng (từ Phần 1)
        mLogAdapter = new MaintenanceLogAdapter(mLogList);

        // 2. Cài đặt RecyclerView
        recyclerViewMaintenanceLogs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMaintenanceLogs.setAdapter(mLogAdapter);

        // 3. GỌI BACKEND: Lấy dữ liệu Log (từ Phần 1)
        mViewModel.getLogsForEquipment(equipmentId).observe(this, new Observer<List<MaintenanceLog>>() {
            @Override
            public void onChanged(List<MaintenanceLog> logs) {
                // 4. Cập nhật Adapter khi dữ liệu về
                mLogAdapter.setLogs(logs);
            }
        });
    }

    /**
     * SỬA LỖI 1: Hàm Download (thêm tham số)
     */
    private void startManualDownload(String manualUrl) {
        // --- KIỂM TRA 1 (Toast "Không tìm thấy URL") ---
        if (manualUrl == null || manualUrl.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy URL Hướng dẫn (Manual)", Toast.LENGTH_SHORT).show();
            // --- THÊM LOGGING ---
            Log.e(TAG, "URL is null or empty. Cannot download.");
            return;
        }

        // --- KIỂM TRA 2 (Toast "Bắt đầu tải..." BẠN BỊ THIẾU) ---
        Toast.makeText(this, "Bắt đầu tải Hướng dẫn...", Toast.LENGTH_SHORT).show();
        // --- THÊM LOGGING ---
        Log.d(TAG, "Attempting to download from URL: " + manualUrl);

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(manualUrl));
            request.setTitle("User_Manual_" + textViewEquipmentName.getText());
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
            // --- THÊM LOGGING ---
            Log.e(TAG, "Lỗi DownloadManager: ", e);
        }
    }
}