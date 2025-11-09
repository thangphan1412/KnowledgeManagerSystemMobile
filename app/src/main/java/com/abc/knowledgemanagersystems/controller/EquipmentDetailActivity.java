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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.viewmodel.EquipmentViewModel;

public class EquipmentDetailActivity extends AppCompatActivity {

    private TextView textViewEquipmentName, textViewModel, textViewSerial, textViewEquipmentStatus;
    private Button buttonDownloadManual, buttonGoToBooking;
    private RecyclerView recyclerViewMaintenanceLogs;
    private EquipmentViewModel mViewModel; // <-- "Backend"
    private int equipmentId = -1;
    private String manualUrl = "https://www.agilent.com/cs/library/usermanuals/public/G7100-90030_InfinityLab-LC-Series-SitePrep_RevD_EN.pdf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Dùng layout (model_165) của bạn
        setContentView(R.layout.activity_equipment_detail);

        // Lấy "Backend" (từ Bước 2)
        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);

        // KHỚP VỚI XML CỦA BẠN (Sửa 34 lỗi gạch đỏ)
        textViewEquipmentName = findViewById(R.id.text_view_equipment_name_detail);
        textViewModel = findViewById(R.id.text_view_equipment_model_detail);
        textViewSerial = findViewById(R.id.text_view_serial_detail);
        textViewEquipmentStatus = findViewById(R.id.text_view_status_detail);
        buttonDownloadManual = findViewById(R.id.button_download_manual);
        buttonGoToBooking = findViewById(R.id.button_go_to_booking);
        recyclerViewMaintenanceLogs = findViewById(R.id.recycler_view_maintenance_logs);

        // Lấy ID
        equipmentId = getIntent().getIntExtra("EQUIPMENT_ID", -1);
        if (equipmentId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Gọi Backend để lấy dữ liệu
        mViewModel.getEquipmentById(equipmentId).observe(this, new Observer<Equipment>() {
            @Override
            public void onChanged(Equipment equipment) {
                if (equipment != null) {
                    textViewEquipmentName.setText(equipment.getName());
                    textViewModel.setText("Model: " + equipment.getModel());
                    // (Các trường khác)
                }
            }
        });

        // SỬA 2 LỖI (ảnh image_7e4408.png)
        setupClickListeners();
        setupLogRecyclerView();
    }

    private void setupClickListeners() {
        buttonDownloadManual.setOnClickListener(v -> startManualDownload());

        buttonGoToBooking.setOnClickListener(v -> {
            Intent intent = new Intent(EquipmentDetailActivity.this, EquipmentBookingActivity.class);
            intent.putExtra("EQUIPMENT_ID", equipmentId);
            intent.putExtra("EQUIPMENT_NAME", textViewEquipmentName.getText().toString());
            startActivity(intent);
        });
    }

    // ================================================================
    // THÊM 2 HÀM BỊ THIẾU (Sửa lỗi 'image_7e4408.png')
    // =================================D===============================

    private void setupLogRecyclerView() {
        recyclerViewMaintenanceLogs.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Cài đặt Adapter và load data cho Log
        // (Đây là nơi bạn sẽ tạo Adapter cho recycler_view_maintenance_logs)
    }

    private void startManualDownload() {
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