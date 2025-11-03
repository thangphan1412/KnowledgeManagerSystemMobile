package com.abc.knowledgemanagersystems.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthPreferences {
    private static final String PREF_FILE_NAME = "secure_auth_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";
    private final SharedPreferences sharedPreferences;

    public AuthPreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_FILE_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Lỗi khi khởi tạo EncryptedSharedPreferences", e);
        }
    }

    public void saveAuthData(String token, String role) {
        sharedPreferences.edit()
                .putString(KEY_JWT_TOKEN, token)
                .putString(KEY_USER_ROLE, role)
                .apply();
    }

    public String getJwtToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "GUEST");
    }

    public void clearAuthData() {
        sharedPreferences.edit().clear().apply();
    }
}
