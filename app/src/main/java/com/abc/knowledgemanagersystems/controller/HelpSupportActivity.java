package com.abc.knowledgemanagersystems.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;
import com.google.android.material.appbar.MaterialToolbar;

public class HelpSupportActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Button buttonDownloadUserGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support); // <-- Sử dụng layout XML mới

        toolbar = findViewById(R.id.toolbar_help_support);
        buttonDownloadUserGuide = findViewById(R.id.button_download_user_guide);

        // Xử lý nút "Quay lại" (dấu <) trên toolbar
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // Đóng Activity này và quay lại
        });

        // (Tùy chọn) Xử lý nút tải hướng dẫn
        buttonDownloadUserGuide.setOnClickListener(v -> {
            // (Bạn có thể tái sử dụng logic DownloadManager ở đây để tải về link 'dummy.pdf')
            Toast.makeText(this, "Bắt đầu tải hướng dẫn sử dụng...", Toast.LENGTH_SHORT).show();
        });
    }
}