package com.abc.knowledgemanagersystems.controller;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.abc.knowledgemanagersystems.databinding.ActivityUserProfileBinding;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.service.LoginService;
import com.abc.knowledgemanagersystems.service.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private AuthPreferences authPreferences;
    private UserService userService;
    private ExecutorService executorService;
    private static final String TAG = "UserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authPreferences = new AuthPreferences(this);
        userService = new UserService(this);
        executorService = Executors.newSingleThreadExecutor();

        loadUserProfile();
    }

    private void loadUserProfile() {
        String loggedInEmail = authPreferences.getUserEmail();
        Log.d(TAG, "Đang tải hồ sơ cho Email: " + loggedInEmail);

        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem hồ sơ.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        executorService.execute(() -> {
            try {
                // ✅ GỌI DATABASE TỪ LUỒNG NỀN
                Users user = userService.getUserByEmail(loggedInEmail);

                runOnUiThread(() -> {
                    if (user != null) {
                        displayProfile(user);
                    } else {
                        Toast.makeText(this, "Không tìm thấy hồ sơ người dùng trong hệ thống.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Người dùng không được tìm thấy với email: " + loggedInEmail);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi tải dữ liệu hồ sơ từ DB.", e);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void displayProfile(Users user) {
        // ✅ GÁN DỮ LIỆU TỪ OBJECT USERS VÀO CÁC TEXTVIEW
        binding.textViewUsername.setText(user.getUsername());
        binding.textViewEmail.setText(user.getEmail());

        // Vai trò cần gọi .name() nếu nó là Enum RoleName
        binding.textViewRole.setText(user.getRoleName().name());

        // Xử lý giá trị null/rỗng cho các trường tùy chọn (Phone và Address)
        String phone = user.getNumberphone();
        binding.textViewPhone.setText(phone != null && !phone.isEmpty() ? phone : "Chưa cập nhật");

        String address = user.getAddress();
        binding.textViewAddress.setText(address != null && !address.isEmpty() ? address : "Chưa cập nhật");

        binding.buttonEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng chỉnh sửa đang phát triển...", Toast.LENGTH_SHORT).show();
            // TODO: Intent sang EditProfileActivity
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