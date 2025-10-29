package com.abc.knowledgemanagersystems;

// THÊM CÁC DÒNG IMPORT NÀY
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// THÊM 2 DÒNG IMPORT NÀY
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EquipmentDetailActivity extends AppCompatActivity {

    // Khai báo các View (thêm 3 view mới)
    private TextView textViewEquipmentName;
    private TextView textViewModel; // MỚI
    private TextView textViewSerial; // MỚI
    private TextView textViewEquipmentStatus;
    private Button buttonDownloadManual;
    private CalendarView calendarView;
    private Button buttonPickStartTime;
    private Button buttonPickEndTime;
    private Button buttonBookNow;
    private RecyclerView recyclerViewMaintenanceLogs; // MỚI

    // (Biến lưu URL của file PDF - Sẽ lấy từ database)
    private String manualUrl = "https://www.agilent.com/cs/library/usermanuals/public/G7100-90030_InfinityLab-LC-Series-SitePrep_RevD_EN.pdf";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Nạp file layout
        setContentView(R.layout.activity_equipment_detail);

        // 2. Ánh xạ (tìm) các View (thêm 3 view mới)
        textViewEquipmentName = findViewById(R.id.text_view_equipment_name);
        textViewModel = findViewById(R.id.text_view_model); // MỚI
        textViewSerial = findViewById(R.id.text_view_serial); // MỚI
        textViewEquipmentStatus = findViewById(R.id.text_view_equipment_status);
        buttonDownloadManual = findViewById(R.id.button_download_manual);
        calendarView = findViewById(R.id.calendar_view);
        buttonPickStartTime = findViewById(R.id.button_pick_start_time);
        buttonPickEndTime = findViewById(R.id.button_pick_end_time);
        buttonBookNow = findViewById(R.id.button_book_now);
        recyclerViewMaintenanceLogs = findViewById(R.id.recycler_view_maintenance_logs); // MỚI

        // 3. Tải dữ liệu (Tạm thời)
        // TODO: Bạn sẽ cần nhận ID từ Intent và tải dữ liệu thật từ ViewModel
        textViewEquipmentName.setText("HPLC Machine #2");
        textViewModel.setText("Model: Agilent 1260"); // MỚI
        textViewSerial.setText("Serial #: US12345678"); // MỚI
        textViewEquipmentStatus.setText("Trạng thái: Sẵn sàng");

        // 4. Cài đặt các trình lắng nghe sự kiện
        setupClickListeners();

        // 5. Cài đặt RecyclerView cho Nhật ký
        setupLogRecyclerView();
    }

    private void setupClickListeners() {

        // ================================================================
        // BẮT ĐẦU CORE LOGIC: DOWNLOAD MANAGER
        // ================================================================
        buttonDownloadManual.setOnClickListener(v -> {
            // Thay vì Toast, gọi hàm download
            Toast.makeText(this, "Bắt đầu tải Hướng dẫn...", Toast.LENGTH_SHORT).show();
            startManualDownload();
        });
        // ================================================================
        // KẾT THÚC CORE LOGIC
        // ================================================================


        buttonPickStartTime.setOnClickListener(v -> {
            Toast.makeText(this, "Mở chọn giờ bắt đầu", Toast.LENGTH_SHORT).show();
        });

        buttonPickEndTime.setOnClickListener(v -> {
            Toast.makeText(this, "Mở chọn giờ kết thúc", Toast.LENGTH_SHORT).show();
        });

        buttonBookNow.setOnClickListener(v -> {
            Toast.makeText(this, "Đã xác nhận đặt lịch!", Toast.LENGTH_SHORT).show();
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(this, "Ngày chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Cài đặt RecyclerView cho nhật ký bảo trì
     */
    private void setupLogRecyclerView() {
        // TODO: Bạn sẽ cần tạo 1 Adapter (ví dụ: LogAdapter)
        // LogAdapter adapter = new LogAdapter();
        // recyclerViewMaintenanceLogs.setAdapter(adapter);

        // Cài đặt LayoutManager
        recyclerViewMaintenanceLogs.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Load dữ liệu nhật ký từ ViewModel và đưa vào adapter
        // viewModel.getLogs(equipmentId).observe(this, logs -> {
        //    adapter.submitList(logs);
        // });
    }

    /**
     * Bắt đầu tải file PDF bằng DownloadManager
     */
    private void startManualDownload() {
        if (manualUrl == null || manualUrl.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy URL tài liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tạo một yêu cầu (Request)
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(manualUrl));

        // 2. Đặt các thuộc tính
        request.setTitle("User Manual - " + textViewEquipmentName.getText()); // Tên file trên thông báo
        request.setDescription("Đang tải tệp PDF...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 3. Đặt thư mục đích (thư mục "Downloads" của điện thoại)
        // Tên file sẽ là duy nhất, ví dụ: "equipment_manual-1.pdf"
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "equipment_manual.pdf");

        // 4. Lấy dịch vụ DownloadManager và xếp yêu cầu vào hàng đợi
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        } else {
            Toast.makeText(this, "Không thể khởi động dịch vụ Tải xuống", Toast.LENGTH_SHORT).show();
        }
    }
}