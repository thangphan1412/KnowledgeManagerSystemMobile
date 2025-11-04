package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.databinding.ActivityLoginBinding;
import com.abc.knowledgemanagersystems.response.LoginResponse;
import com.abc.knowledgemanagersystems.service.LoginService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginService loginService;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginService = new LoginService(this);
        executorService = Executors.newSingleThreadExecutor();

        binding.buttonLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = binding.editTextUserIdEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.buttonLogin.setEnabled(false); // Vô hiệu hóa nút trong khi đăng nhập

        // Thực hiện logic đăng nhập trên luồng nền
        executorService.execute(() -> {
            try {
                //  GỌI SERVICE ĐỂ XÁC THỰC VÀ LƯU TOKEN
                LoginResponse response = loginService.authenticate(email, password);

                //  XỬ LÝ THÀNH CÔNG (trên Main Thread)
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng nhập thành công! Vai trò: " + response.getRole(), Toast.LENGTH_LONG).show();
                    // Chuyển sang màn hình chính (HomepageActivity)
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                });

            } catch (Exception e) {
                //  XỬ LÝ THẤT BẠI (trên Main Thread)
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng nhập thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.buttonLogin.setEnabled(true); // Kích hoạt lại nút
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
