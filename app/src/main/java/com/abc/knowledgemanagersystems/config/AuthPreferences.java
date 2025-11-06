package com.abc.knowledgemanagersystems.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthPreferences {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";

    //  Thêm các hằng số mới
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";

    private final SharedPreferences sharedPreferences;



    public AuthPreferences(Context context) {
        try {
            // 1. Khởi tạo Master Key
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. Sử dụng EncryptedSharedPreferences để tạo lớp SharedPreferences bảo mật
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Xử lý nếu việc tạo EncryptedSharedPreferences thất bại (rất hiếm)
            e.printStackTrace();
            // Fallback: sử dụng SharedPreferences thông thường (nhưng KHÔNG NÊN)
            // Hoặc ném RuntimeException để dừng ứng dụng nếu không thể bảo mật dữ liệu quan trọng
            throw new RuntimeException("Không thể tạo EncryptedSharedPreferences", e);
        }
    }

    // Phương thức đã có
    public void saveAuthData(String token, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JWT_TOKEN, token);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    // --- 📢 PHƯƠNG THỨC CẦN THÊM (Fix lỗi) ---

    /**
     * Lưu Email người dùng sau khi đăng nhập.
     */
    public void saveUserEmail(String email) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    /**
     * Lưu Username người dùng sau khi đăng nhập.
     */
    public void saveUserName(String username) {
        sharedPreferences.edit().putString(KEY_USER_NAME, username).apply();
    }

    // --- Phương thức Getter (Để lấy dữ liệu) ---

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    public String getJwtToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }
    public void clearAuthData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Xóa tất cả các khóa liên quan đến phiên
        editor.remove(KEY_JWT_TOKEN);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME);

        editor.apply(); // Áp dụng các thay đổi
    }
}
