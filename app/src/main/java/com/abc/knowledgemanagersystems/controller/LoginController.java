package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.databinding.ActivityLoginBinding;
import com.abc.knowledgemanagersystems.dto.response.LoginResponse;
import com.abc.knowledgemanagersystems.service.LoginService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginService loginService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Service
        loginService = new LoginService(this);


        // Thiết lập sự kiện cho nút Đăng nhập
        binding.buttonLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = binding.editTextUserIdEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vô hiệu hóa nút để tránh nhấp đúp
        binding.buttonLogin.setEnabled(false);

        // ✅ Gọi phương thức bất đồng bộ sử dụng Callback
        loginService.authenticate(email, password, new LoginService.AuthCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                // Chuyển về luồng UI để cập nhật giao diện
                runOnUiThread(() -> {
                    Toast.makeText(LoginController.this, "Đăng nhập thành công! Vai trò: " + response.getRole(), Toast.LENGTH_LONG).show();

                    // Chuyển sang màn hình chính
                    Intent intent = new Intent(LoginController.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String message) {
                // Xử lý lỗi (thường là "Thông tin đăng nhập không hợp lệ")
                runOnUiThread(() -> {
                    Toast.makeText(LoginController.this, "Đăng nhập thất bại: " + message, Toast.LENGTH_LONG).show();
                    binding.buttonLogin.setEnabled(true); // Kích hoạt lại nút
                });
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Không cần dọn dẹp executorService nữa
    }
}
