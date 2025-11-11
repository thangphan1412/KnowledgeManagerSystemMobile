package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.ExperimentAdapter;
import com.abc.knowledgemanagersystems.config.AuthPreferences;

// ✅ ĐẢM BẢO CHỈ DÙNG 1 IMPORT CHO MODEL EXPERIMENT THỰC TẾ
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.repository.ExperimentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExperimentListActivity extends AppCompatActivity {

    private static final Class<?> EXPERIMENT_DETAIL_ACTIVITY = ExperimentActivity.class;
    private static final Class<?> CREATE_EXPERIMENT_ACTIVITY = CreateExperimentActivity.class;

    private ExperimentRepository experimentRepository;
    private ExperimentAdapter experimentAdapter;
    private AuthPreferences authPreferences;
    private ExecutorService executorService;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Danh sách Thí nghiệm");
        }

        // Khởi tạo các thành phần
        authPreferences = new AuthPreferences(this);
        experimentRepository = new ExperimentRepository(this);
        executorService = Executors.newSingleThreadExecutor();
        currentUserId = authPreferences.getUserId();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_experiments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter rỗng
        // ExperimentAdapter cần sử dụng Model Experiment thực tế
        experimentAdapter = new ExperimentAdapter(new ArrayList<>(), this::onExperimentClicked);
        recyclerView.setAdapter(experimentAdapter);

        // Gắn sự kiện Tạo mới
        findViewById(R.id.fab_add_experiment).setOnClickListener(v -> {
            startActivity(new Intent(this, CREATE_EXPERIMENT_ACTIVITY));
        });

        // Tải dữ liệu lần đầu
        loadExperiments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách mỗi khi Activity này hiển thị (sau khi tạo mới/chỉnh sửa)
        loadExperiments();
    }

    private void loadExperiments() {
        if (currentUserId <= 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID người dùng.", Toast.LENGTH_LONG).show();
            return;
        }

        executorService.execute(() -> {
            try {
                List<Experiment> list = experimentRepository.getExperimentsByUserId(currentUserId);

                runOnUiThread(() -> {
                    experimentAdapter.updateList(list);
                    if (list.isEmpty()) {
                        Toast.makeText(this, "Chưa có thí nghiệm nào. Hãy thêm mới!", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    // Xử lý click để chuyển sang màn hình Chi tiết (Note Log)
    private void onExperimentClicked(Experiment experiment) {
        Intent intent = new Intent(this, EXPERIMENT_DETAIL_ACTIVITY);

        // Gửi ID và Tên thí nghiệm sang Activity chi tiết (ExperimentActivity)
        intent.putExtra("EXPERIMENT_ID", experiment.getId()); // Model Experiment (Entity) phải có getId()
        intent.putExtra("EXPERIMENT_TITLE", experiment.getTitle());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // ❌ LỚP PUBLIC STATIC CLASS EXPERIMENT GIẢ LẬP ĐÃ BỊ XÓA BỎ KHỎI ĐÂY
}