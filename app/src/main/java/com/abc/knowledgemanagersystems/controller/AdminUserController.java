package com.abc.knowledgemanagersystems.controller;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.databinding.ActivityAdminUserBinding;
import com.abc.knowledgemanagersystems.dto.request.CreateUserRequest;
import com.abc.knowledgemanagersystems.dto.response.CreateUserResponse;
import com.abc.knowledgemanagersystems.service.LoginService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminUserController  extends AppCompatActivity {
    private ActivityAdminUserBinding binding;
    private LoginService loginService;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginService = new LoginService(this);
        executorService = Executors.newSingleThreadExecutor();

        binding.buttonCreateUser.setOnClickListener(v -> handleCreateUser());

        // Gợi ý: Kiểm tra vai trò ở đây và nếu không phải Admin thì chuyển hướng
        // if (!authPreferences.getRole().equals("ADMIN")) finish();
    }

    private void handleCreateUser() {
        String username = binding.editTextNewUsername.getText().toString().trim();
        String email = binding.editTextNewEmail.getText().toString().trim();
        String password = binding.editTextNewPassword.getText().toString(); // Mật khẩu Admin cấp

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin người dùng.", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.buttonCreateUser.setEnabled(false);

        executorService.execute(() -> {
            try {
                // 1. Tạo Request DTO
                CreateUserRequest request = new CreateUserRequest(email, password, username);

                // 2. Gọi service để tạo user
                CreateUserResponse response = loginService.createRegularUser(request);

                runOnUiThread(() -> {
                    if (response.isSuccess()) {
                        Toast.makeText(this, response.getMessage() + " Tên: " + username, Toast.LENGTH_LONG).show();
                        // Xóa form sau khi thành công
                        binding.editTextNewUsername.setText("");
                        binding.editTextNewEmail.setText("");
                        binding.editTextNewPassword.setText("");
                    } else {
                        Toast.makeText(this, "Tạo thất bại: " + response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    binding.buttonCreateUser.setEnabled(true);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.buttonCreateUser.setEnabled(true);
                });
            }
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
