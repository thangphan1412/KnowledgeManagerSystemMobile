package com.abc.knowledgemanagersystems.service;

import android.content.Context;
import android.util.Log;


import com.abc.knowledgemanagersystems.API.AuthApi;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.abc.knowledgemanagersystems.dao.UserDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.dto.request.CreateUserRequest;
import com.abc.knowledgemanagersystems.dto.request.LoginRequest;
import com.abc.knowledgemanagersystems.dto.response.CreateUserResponse;
import com.abc.knowledgemanagersystems.dto.response.LoginResponse;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.status.RoleName;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LoginService {
    private AuthPreferences authPreferences;
    private UserDao userDao;
    private AuthApi authApi; // Giả định

    public LoginService(Context context) {
        this.authPreferences = new AuthPreferences(context);
        this.userDao = AppDataBase.getInstance(context).userDao();
        // Giả định: this.authApi = RetrofitClient.getRetrofitInstance().create(AuthApi.class);
    }
    public interface AuthCallback {
        void onSuccess(LoginResponse response);
        void onFailure(String message);
    }

    public void authenticate(String email, String password, AuthCallback callback) {

        // 🛑 THÊM TRIM CHO AN TOÀN KHI GỌI TỪ SERVICE
        final String trimmedEmail = email.trim();
        final String trimmedPassword = password.trim();

        AppDataBase.databaseWriteExecutor.execute(() -> {
            try {
                // --- BƯỚC 1: XÁC THỰC DUY NHẤT TRONG ROOM DB ---
                Users authenticatedUser = userDao.authenticateUser(trimmedEmail, trimmedPassword);

                if (authenticatedUser != null) {
                    LoginResponse response = new LoginResponse();

                    // --- BƯỚC 2: PHÂN VAI TRÒ VÀ TẠO TOKEN ---
                    if (authenticatedUser.getRoleName() == RoleName.MANAGER) {
                        // Nếu là MANAGER (Admin cứng), tạo Local Token
                        response = createLocalLoginResponse(authenticatedUser);
                    } else {
                        // Nếu là RESEARCHER/TECHNICIAN (Giả lập API)
                        // ✅ Dùng getUserId() để có ID chính xác
                        response.setJwtToken("api_jwt_user_" + authenticatedUser.getId());
                        response.setRole(authenticatedUser.getRoleName().name());

                        // Lưu dữ liệu Auth cho User thường
                        authPreferences.saveAuthData(response.getJwtToken(), response.getRole());
                        authPreferences.saveUserEmail(authenticatedUser.getEmail());
                        authPreferences.saveUserName(authenticatedUser.getUsername());
                    }

                    callback.onSuccess(response);
                    return;
                }

                // --- BƯỚC 3: THẤT BẠI HOÀN TOÀN ---
                callback.onFailure("Thông tin đăng nhập không hợp lệ.");

            } catch (Exception e) {
                Log.e("LOGIN_SERVICE", "Lỗi trong quá trình xác thực: " + e.getMessage());
                callback.onFailure("Lỗi hệ thống: " + e.getMessage());
            }
        });
    }

    private LoginResponse createLocalLoginResponse(Users user) {
        LoginResponse response = new LoginResponse();
        // ✅ Dùng getUserId() để có ID chính xác
        response.setJwtToken("local_" + user.getId() + "_admin_token");
        response.setRole(user.getRoleName().name());

        authPreferences.saveAuthData(response.getJwtToken(), response.getRole());
        authPreferences.saveUserEmail(user.getEmail());
        authPreferences.saveUserName(user.getUsername());

        return response;
    }

    // ... (Giữ nguyên các phương thức khác)
    public CreateUserResponse createRegularUser(CreateUserRequest request) throws ExecutionException, InterruptedException {
        // ... (Giữ nguyên logic tạo user)
        Future<Users> checkFuture = AppDataBase.databaseWriteExecutor.submit(() ->
                userDao.getUserByEmail(request.getEmail())
        );

        if (checkFuture.get() != null) {
            return new CreateUserResponse(false, "Email đã được sử dụng.");
        }

        Users newUser = new Users();
        newUser.setEmail(request.getEmail().trim()); // 🛑 TRIM EMAIL
        newUser.setPassword(request.getPassword().trim()); // 🛑 TRIM PASSWORD
        newUser.setUsername(request.getUsername().trim()); // 🛑 TRIM USERNAME

        RoleName assignedRole = request.getRoleName() != null ? request.getRoleName() : RoleName.RESEARCHER;
        newUser.setRoleName(assignedRole);

        Future<?> insertFuture = AppDataBase.databaseWriteExecutor.submit(() ->
                userDao.insert(newUser)
        );

        try {
            insertFuture.get();
            return new CreateUserResponse(true, "Tạo tài khoản thành công! Vai trò: " + assignedRole.name());
        } catch (Exception e) {
            Log.e("CREATE_USER_SERVICE", "Lỗi tạo tài khoản:", e);
            return new CreateUserResponse(false, "Lỗi hệ thống khi lưu dữ liệu.");
        }
    }
}