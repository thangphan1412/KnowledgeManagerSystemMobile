//package com.abc.knowledgemanagersystems.controller;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.abc.knowledgemanagersystems.R;
//
//public class ExperimentListActivity extends AppCompatActivity {
//    private int experimentId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_experiment_detail); // Layout chứa Note Log
//
//        // 1. Lấy ID thí nghiệm từ Intent
//        if (getIntent().getExtras() != null) {
//            // Lấy ID đã gửi từ ExperimentListActivity
//            experimentId = getIntent().getIntExtra("EXPERIMENT_ID", -1);
//            String experimentTitle = getIntent().getStringExtra("EXPERIMENT_TITLE");
//
//            if (experimentId <= 0) {
//                Toast.makeText(this, "Không tìm thấy thí nghiệm.", Toast.LENGTH_SHORT).show();
//                finish();
//                return;
//            }
//
//            // Cập nhật tiêu đề Activity/Toolbar
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("Log: " + experimentTitle);
//            }
//
//            // 2. Tải và hiển thị các Note Log (dùng experimentId)
//            loadNoteLogsForExperiment(experimentId); // <-- Cần triển khai hàm này
//
//        } else {
//            finish();
//        }
//
//        // ... (Các logic hiện tại của bạn về việc hiển thị và thêm Note Log)
//    }
//
//    private void loadNoteLogsForExperiment(int id) {
//        // Sử dụng ID này để truy vấn Room DB hoặc API
//        // Ví dụ: noteLogService.getLogsByExperimentId(id);
//        // Sau đó cập nhật RecyclerView hiển thị các Note Log
//    }
//}
