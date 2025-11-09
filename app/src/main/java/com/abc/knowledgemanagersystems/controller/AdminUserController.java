package com.abc.knowledgemanagersystems.controller;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.databinding.ActivityAdminUserBinding;
import com.abc.knowledgemanagersystems.dto.request.CreateUserRequest;
import com.abc.knowledgemanagersystems.dto.response.CreateUserResponse;
import com.abc.knowledgemanagersystems.service.LoginService;
import com.abc.knowledgemanagersystems.status.RoleName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminUserController  extends AppCompatActivity {
    private ActivityAdminUserBinding binding;
    private LoginService loginService;
    private ExecutorService executorService;
    private final RoleName[] ROLE_MAPPING = {
            RoleName.RESEARCHER,
            RoleName.MANAGER,
            RoleName.TECHNICIAN
    };

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
        String password = binding.editTextNewPassword.getText().toString();
        int selectedPosition = binding.spinnerRole.getSelectedItemPosition();
        RoleName selectedRole = ROLE_MAPPING[selectedPosition];

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin người dùng.", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.buttonCreateUser.setEnabled(false);

        executorService.execute(() -> {
            try {
                // 1. Tạo Request DTO
                // 1. Tạo Request DTO và THÊM DỮ LIỆU
                CreateUserRequest request = new CreateUserRequest();
                request.setUsername(username);
                request.setEmail(email);
                request.setPassword(password);
                request.setRoleName(selectedRole);

                // 2. Gọi service để tạo user
                CreateUserResponse response = loginService.createRegularUser(request);

                runOnUiThread(() -> {
                    if (response.isSuccess()) {
                        // ... (Xử lý thành công)
                        Toast.makeText(this, response.getMessage() + " Vai trò: " + selectedRole.name(), Toast.LENGTH_LONG).show();
                         binding.editTextNewUsername.getText().toString().trim();
                         binding.editTextNewEmail.getText().toString().trim();
                         binding.editTextNewPassword.getText().toString();
                    } else {
                        // ... (Xử lý thất bại)
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
