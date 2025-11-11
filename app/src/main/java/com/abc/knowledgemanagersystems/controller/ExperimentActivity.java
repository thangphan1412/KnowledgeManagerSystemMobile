package com.abc.knowledgemanagersystems.controller;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abc.knowledgemanagersystems.adapter.LogEntryAdapter;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.abc.knowledgemanagersystems.databinding.ActivityLogbookBinding;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.service.LogbookService;
import com.abc.knowledgemanagersystems.service.StorageService;
import com.abc.knowledgemanagersystems.status.LogEntryType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExperimentActivity extends AppCompatActivity {
    private ActivityLogbookBinding binding;
    private LogbookService logbookService;
    private StorageService storageService;
    private ExecutorService executorService;
    private AuthPreferences authPreferences;

    private int experimentId; // ID của thí nghiệm hiện tại
    private int currentUserId; // ID người dùng hiện tại
    private boolean isFabOpen = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    // 1. Activity Result Launcher cho việc chọn ảnh
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::handleImageSelection);

    // 2. Activity Result Launcher cho việc chọn tệp dữ liệu
    private final ActivityResultLauncher<String> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::handleFileSelection);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các thành phần
        authPreferences = new AuthPreferences(this);
        logbookService = new LogbookService(this);
        storageService = new StorageService(this); // Cần thiết cho upload ảnh/file
        executorService = Executors.newSingleThreadExecutor();

        // Lấy ID Thí nghiệm và ID Người dùng
        experimentId = getIntent().getIntExtra("EXPERIMENT_ID", -1); // ✅ ĐÃ SỬA: Dùng key chữ hoa
        currentUserId = authPreferences.getUserId();

        if (experimentId == -1 || currentUserId == -1) {
            Toast.makeText(this, "Thiếu thông tin thí nghiệm hoặc người dùng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupFabActions();
        setupRecyclerView();
        loadLogEntries();
    }

    // --- Cấu hình Giao diện và Sự kiện ---
    private void setupFabActions() {
        binding.fabMainAction.setOnClickListener(v -> toggleFabMenu());
        binding.fabAddNote.setOnClickListener(v -> handleAddNote());
        binding.fabUploadImage.setOnClickListener(v -> handleUploadImage());
        binding.fabAttachData.setOnClickListener(v -> handleAttachData());
    }

    private void toggleFabMenu() {
        isFabOpen = !isFabOpen;
        int visibility = isFabOpen ? View.VISIBLE : View.GONE;

        // Hiện/Ẩn các nút phụ
        binding.fabAddNote.setVisibility(visibility);
        binding.fabUploadImage.setVisibility(visibility);
        binding.fabAttachData.setVisibility(visibility);

        // Xoay nút chính
        binding.fabMainAction.animate().rotation(isFabOpen ? 45f : 0f).setDuration(300);
    }

    private void setupRecyclerView() {
        binding.recyclerViewLogEntries.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo Adapter rỗng
        binding.recyclerViewLogEntries.setAdapter(new LogEntryAdapter(new ArrayList<>()));
    }

    // --- Xử lý các Hành động người dùng ---

    private void handleAddNote() {
        toggleFabMenu();
        showNoteDialog();
    }

    private void handleUploadImage() {
        toggleFabMenu();
        // Intent chọn file ảnh
        imagePickerLauncher.launch("image/*");
    }

    private void handleAttachData() {
        toggleFabMenu();
        // Intent chọn mọi loại file
        filePickerLauncher.launch("*/*");
    }

    // --- Xử lý Kết quả Intent ---

    private void handleImageSelection(Uri uri) {
        if (uri != null) {
            Toast.makeText(this, "Đang tải ảnh lên cloud...", Toast.LENGTH_SHORT).show();
            uploadFileToCloud(uri, LogEntryType.IMAGE, "Ảnh thí nghiệm");
        }
    }

    private void handleFileSelection(Uri uri) {
        if (uri != null) {
            Toast.makeText(this, "Đang tải tệp dữ liệu lên cloud...", Toast.LENGTH_SHORT).show();
            uploadFileToCloud(uri, LogEntryType.DATA_FILE, "Tệp dữ liệu đính kèm");
        }
    }

    // --- Logic Tải lên và Lưu Log ---

    private void uploadFileToCloud(Uri uri, LogEntryType type, String fileName) {
        executorService.execute(() -> {
            try {
                // Giả định StorageService có phương thức tải lên và trả về URL
                String fileUrl = storageService.uploadFile(uri, type);

                // Sau khi tải lên thành công, tạo LogEntry và lưu vào Room
                String currentDate = dateFormat.format(new Date());
                ExperimentLogs newEntry = createNewLogEntry(type, fileUrl, fileName, currentDate);

                logbookService.insertLogEntry(newEntry);

                runOnUiThread(() -> {
                    Toast.makeText(this, type.name() + " đã được thêm vào nhật ký.", Toast.LENGTH_SHORT).show();
                    loadLogEntries(); // Tải lại danh sách để cập nhật UI
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi tải lên/lưu log: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private ExperimentLogs createNewLogEntry(LogEntryType type, String content, String fileName, String logDate) {
        // ID: 0 vì Room sẽ tự động gán Primary Key
        return new ExperimentLogs(
                0,
                "", // result có thể để trống
                logDate,
                currentUserId,
                experimentId,
                type,
                content, // URL hoặc Text
                fileName
        );
    }

    // --- Logic Ghi chú Văn bản ---
    private void showNoteDialog() {
        final EditText input = new EditText(this);
        input.setHint("Nhập ghi chú hoặc quan sát chi tiết...");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Ghi chú Thí nghiệm")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String noteText = input.getText().toString().trim();
                    if (!noteText.isEmpty()) {
                        saveNoteToDb(noteText);
                    } else {
                        Toast.makeText(this, "Ghi chú không được rỗng.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void saveNoteToDb(String noteText) {
        executorService.execute(() -> {
            try {
                String currentDate = dateFormat.format(new Date());
                ExperimentLogs newEntry = createNewLogEntry(LogEntryType.NOTE, noteText, null, currentDate);

                logbookService.insertLogEntry(newEntry);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Ghi chú đã được lưu.", Toast.LENGTH_SHORT).show();
                    loadLogEntries();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi lưu ghi chú: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    // --- Logic Tải dữ liệu từ DB ---
    private void loadLogEntries() {
        executorService.execute(() -> {
            // Lấy tất cả các mục nhập (đã được sắp xếp theo thời gian mới nhất trong DAO)
            List<ExperimentLogs> entries = logbookService.getLogEntriesByExperimentId(experimentId);

            runOnUiThread(() -> {
                LogEntryAdapter adapter = (LogEntryAdapter) binding.recyclerViewLogEntries.getAdapter();
                if (adapter != null) {
                    adapter.updateList(entries);
                    binding.recyclerViewLogEntries.scrollToPosition(0); // Cuộn lên mục mới nhất
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
