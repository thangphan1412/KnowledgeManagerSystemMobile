package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.abc.knowledgemanagersystems.databinding.ActivityCreateExperimentBinding;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.Sops; // Cần import Sops
import com.abc.knowledgemanagersystems.repository.ExperimentRepository;
import com.abc.knowledgemanagersystems.status.StatusExperiment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CreateExperimentActivity extends AppCompatActivity {

    private ActivityCreateExperimentBinding binding;
    private AuthPreferences authPreferences;
    private ExperimentRepository experimentRepository;
    private ExecutorService executorService;
    private List<Sops> availableSops = new ArrayList<>(); // Danh sách Sops từ DB

    // Định dạng ngày tháng
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateExperimentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tạo Thí Nghiệm Mới");
        }

        authPreferences = new AuthPreferences(this);
        experimentRepository = new ExperimentRepository(this);
        executorService = Executors.newSingleThreadExecutor();

        setupStatusDropdown();
        loadAndSetupProtocolDropdown(); // Tải Sops và thiết lập Dropdown
        setupDatePicker();
        setupCreateButton();
    }

    // --- Cấu hình Dropdown Trạng thái ---
    private void setupStatusDropdown() {
        // Lấy tất cả giá trị từ Enum StatusExperiment
        StatusExperiment[] statuses = StatusExperiment.values();
        String[] statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusNames[i] = statuses[i].name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                statusNames
        );
        binding.autoCompleteStatus.setAdapter(adapter);

        // Đặt giá trị mặc định là ACTIVE
        binding.autoCompleteStatus.setText(StatusExperiment.Running.name(), false);
    }

    // --- Cấu hình Dropdown Protocol ---
    private void loadAndSetupProtocolDropdown() {
        executorService.execute(() -> {
            try {
                availableSops = experimentRepository.getAllSops();

                // Lấy danh sách tên SOP để hiển thị
                List<String> sopNames = availableSops.stream()
                        .map(Sops::getSopsName) // Giả định Sops có phương thức getSopName()
                        .collect(Collectors.toList());

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateExperimentActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            sopNames
                    );
                    binding.autoCompleteProtocol.setAdapter(adapter);
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải Protocols: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    // --- Cấu hình Date Picker ---
    private void setupDatePicker() {
        TextInputEditText dateEditText = binding.editTextStartDate;

        dateEditText.setOnClickListener(v -> showDatePicker());
        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày bắt đầu")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String dateString = dateFormat.format(new Date(selection));
            binding.editTextStartDate.setText(dateString);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    // --- Xử lý nút Tạo Thí nghiệm ---
    private void setupCreateButton() {
        binding.buttonCreateExperiment.setOnClickListener(v -> validateAndSaveExperiment());
    }

    private void validateAndSaveExperiment() {
        String title = binding.editTextExperimentTitle.getText().toString().trim();
        String hypothesis = binding.editTextHypothesisObjective.getText().toString().trim();
        String startDate = binding.editTextStartDate.getText().toString().trim();
        String selectedProtocolName = binding.autoCompleteProtocol.getText().toString().trim();
        String statusText = binding.autoCompleteStatus.getText().toString().trim();

        int userId = authPreferences.getUserId();

        if (userId <= 0) {
            Toast.makeText(this, "Lỗi xác thực người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        if (title.isEmpty() || startDate.isEmpty() || statusText.isEmpty()) {
            Toast.makeText(this, "Tên, Ngày Bắt Đầu và Trạng thái không được để trống.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm sopId tương ứng với tên protocol được chọn
        Integer sopId = availableSops.stream()
                .filter(sop -> sop.getSopsName().equals(selectedProtocolName))
                .map(Sops::getId) // Giả định Sops có phương thức getId() trả về int
                .findFirst()
                .orElse(null); // Nếu không tìm thấy, sopId là null (vì là Integer)

        // Chuyển statusText thành Enum
        StatusExperiment status;
        try {
            status = StatusExperiment.valueOf(statusText);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Trạng thái không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Experiment
        Experiment newExperiment = new Experiment(
                userId,
                title,
                startDate,
                null, // endDate ban đầu là null
                status,
                userId,
                sopId,
                null, // serverExperimentId
                false // isSynced
        );

        saveExperimentToDatabase(newExperiment);
    }

    private void saveExperimentToDatabase(Experiment experiment) {
        executorService.execute(() -> {
            long newId = experimentRepository.insertExperimentLocal(experiment);

            runOnUiThread(() -> {
                if (newId > 0) {
                    Toast.makeText(this, "Tạo thí nghiệm thành công! ID: " + newId, Toast.LENGTH_LONG).show();

                    // Cập nhật ID mới cho đối tượng Experiment
                    experiment.setId((int) newId);

                    // Chuyển hướng: Nếu ACTIVE thì vào Logbook, nếu DRAFT/PENDING thì về List
                    if (experiment.getStatusExperiment() == StatusExperiment.Running) {
                        Intent intent = new Intent(this, ExperimentActivity.class);
                        intent.putExtra("EXPERIMENT_ID", (int) newId);
                        intent.putExtra("EXPERIMENT_TITLE", experiment.getTitle());
                        startActivity(intent);
                        finish();
                    } else {
                        // Quay về màn hình Danh sách
                        Intent intent = new Intent(this, ExperimentListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Lỗi khi lưu dữ liệu vào Room.", Toast.LENGTH_LONG).show();
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
        binding = null;
    }
}