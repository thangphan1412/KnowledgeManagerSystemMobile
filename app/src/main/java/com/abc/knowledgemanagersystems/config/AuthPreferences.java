package com.abc.knowledgemanagersystems.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthPreferences {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";

    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";

    private final SharedPreferences sharedPreferences;

    public AuthPreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo EncryptedSharedPreferences", e);
        }
    }

    public void saveAuthData(String token, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JWT_TOKEN, token);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public void saveUserEmail(String email) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public void saveUserName(String username) {
        sharedPreferences.edit().putString(KEY_USER_NAME, username).apply();
    }

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
        editor.remove(KEY_JWT_TOKEN);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME);
        editor.apply();
    }

    public int getUserId() {
        String token = getJwtToken();

        if (token == null || token.isEmpty()) {
            return -1;
        }

        return extractUserIdFromToken(token);
    }

    // File: com.abc.knowledgemanagersystems.config.AuthPreferences.java

    private int extractUserIdFromToken(String token) {
        try {
            // ✅ BƯỚC 1: Xử lý Token giả lập (local_ID_admin_token HOẶC api_jwt_user_ID)
            if (token.startsWith("api_jwt_user_") || token.startsWith("local_")) {

                // Xử lý token Manager (Ví dụ: local_1_admin_token)
                if (token.startsWith("local_")) {
                    String[] parts = token.split("_");
                    // ID là phần tử thứ 1 (index 1)
                    String idString = parts[1].trim();

                    int userId = Integer.parseInt(idString);
                    if (userId <= 0) {
                        Log.e("AuthPreferences", "Manager Token trích xuất ID <= 0: " + userId);
                        clearAuthData();
                        return -1;
                    }
                    return userId;
                }

                // Xử lý token Researcher/Technician (Ví dụ: api_jwt_user_2)
                if (token.startsWith("api_jwt_user_")) {
                    String[] parts = token.split("_");
                    // ID là phần tử cuối cùng (index 3)
                    String idString = parts[parts.length - 1].trim();

                    int userId = Integer.parseInt(idString);
                    if (userId <= 0) {
                        Log.e("AuthPreferences", "API Token trích xuất ID <= 0: " + userId);
                        clearAuthData();
                        return -1;
                    }
                    return userId;
                }

                // Nếu không phải hai loại trên, tiếp tục với JWT thật (nếu có)
            }

            // ✅ BƯỚC 2: Xử lý JWT thật
            // KHÓA LỖI: Đoạn code này sẽ ném ngoại lệ nếu Token giả lập được truyền vào,
            // nhưng hiện tại logic đã được xử lý ở BƯỚC 1.
            JWT jwt = new JWT(token);
            Claim userIdClaim = jwt.getClaim("user_id");
            Integer userId = userIdClaim.asInt();

            if (userId == null || userId <= 0) {
                Log.e("AuthPreferences", "JWT missing or invalid 'user_id' claim.");
                clearAuthData();
                return -1;
            }
            return userId;

        } catch (NumberFormatException e) {
            Log.e("AuthPreferences", "Token ID không phải là số hợp lệ (Lỗi chuyển đổi): " + token, e);
            clearAuthData();
            return -1;
        } catch (Exception e) {
            Log.e("AuthPreferences", "Lỗi giải mã hoặc token không hợp lệ: " + e.getMessage(), e);
            clearAuthData();
            return -1;
        }
    }
}