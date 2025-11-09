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

import retrofit2.Response;

public class LoginService {
//    private static final RoleName DEFAULT_ROLE = RoleName.RESEARCHER;

    private AuthPreferences authPreferences;
    private UserDao userDao;
    private AuthApi authApi;

    public LoginService(Context context) {
        this.authPreferences = new AuthPreferences(context);
        this.userDao = AppDataBase.getInstance(context).userDao();
        // Giả định: this.authApi = RetrofitClient.getRetrofitInstance().create(AuthApi.class);
    }
    public interface AuthCallback {
        void onSuccess(LoginResponse response);
        void onFailure(String message);
    }

    /**
     * Phương thức Hybrid: Kiểm tra Room (cho Admin cứng) trước, sau đó gọi API (cho User thường).
     * Sửa: Chuyển sang VOID và dùng AuthCallback để xử lý kết quả BẤT ĐỒNG BỘ.
     */
    public void authenticate(String email, String password, AuthCallback callback) { // 👈 Sửa 1: Thêm callback

        // Gửi TẤT CẢ công việc chặn (blocking work) vào Executor của Room
        AppDataBase.databaseWriteExecutor.execute(() -> {
            try {
                // --- BƯỚC 1: XÁC THỰC CỤC BỘ (Admin cứng) ---
                Users localUser = userDao.authenticateUser(email, password);

                if (localUser != null) {
                    // Chỉ Admin mới được xác thực cục bộ
                    if (localUser.getRoleName() == RoleName.MANAGER) {
                        LoginResponse response = createLocalLoginResponse(localUser);
                        callback.onSuccess(response); // ✅ Gửi kết quả qua Callback
                        return;
                    }
                }

                // --- BƯỚC 2: XÁC THỰC QUA API BACKEND (Giả lập) ---

                // Ví dụ GIẢ LẬP API thành công.
                if (email.endsWith("@lab.com") && !email.equals("admin@lab.com")) {
                    LoginResponse apiResponse = new LoginResponse();
                    apiResponse.setJwtToken("real_jwt_from_server_123");
                    apiResponse.setRole(RoleName.RESEARCHER.name());

                    authPreferences.saveAuthData(apiResponse.getJwtToken(), apiResponse.getRole());

                    callback.onSuccess(apiResponse); // ✅ Gửi kết quả qua Callback
                    return;
                }

                // --- BƯỚC 3: THẤT BẠI HOÀN TOÀN ---
                callback.onFailure("Thông tin đăng nhập không hợp lệ."); // ❌ Báo lỗi qua Callback

            } catch (Exception e) {
                Log.e("LOGIN_SERVICE", "Lỗi trong quá trình xác thực: " + e.getMessage());
                callback.onFailure("Lỗi hệ thống: " + e.getMessage());
            }
        });
    }

    /** Tạo Response từ Users cục bộ (Chỉ dùng cho Admin cứng) */
    private LoginResponse createLocalLoginResponse(Users user) {
        LoginResponse response = new LoginResponse();
        response.setJwtToken("local_" + user.getEmail() + "_admin_token");
        response.setRole(user.getRoleName().name());

        authPreferences.saveAuthData(response.getJwtToken(), response.getRole());
        authPreferences.saveUserEmail(user.getEmail());
        authPreferences.saveUserName(user.getUsername());

        return response;
    }
    /**
     * 📢 Phương thức mới: Admin tạo người dùng mới và cấp mật khẩu.
     */
    public CreateUserResponse createRegularUser(CreateUserRequest request) throws ExecutionException, InterruptedException {

        // 1. Kiểm tra email tồn tại... (Giữ nguyên)
        Future<Users> checkFuture = AppDataBase.databaseWriteExecutor.submit(() ->
                userDao.getUserByEmail(request.getEmail())
        );

        if (checkFuture.get() != null) {
            return new CreateUserResponse(false, "Email đã được sử dụng.");
        }

        // 2. Tạo đối tượng Users
        Users newUser = new Users();
        newUser.setEmail(request.getEmail());
        // (Lưu ý: Bạn nên HASH mật khẩu tại đây)
        newUser.setPassword(request.getPassword());
        newUser.setUsername(request.getUsername());

        // ✅ SỬA LỖI: Sử dụng roleName từ Request. Nếu Request không cung cấp (null),
        //             thì mặc định là RESEARCHER.
        RoleName assignedRole = request.getRoleName() != null ? request.getRoleName() : RoleName.RESEARCHER;
        newUser.setRoleName(assignedRole);

        // 3. Chèn vào DB trên luồng nền... (Giữ nguyên)
        Future<?> insertFuture = AppDataBase.databaseWriteExecutor.submit(() ->
                userDao.insert(newUser)
        );

        try {
            insertFuture.get(); // Đợi thao tác chèn hoàn thành
            return new CreateUserResponse(true, "Tạo tài khoản thành công! Mật khẩu đã được Admin cấp.");
        } catch (Exception e) {
            Log.e("CREATE_USER_SERVICE", "Lỗi tạo tài khoản:", e);
            return new CreateUserResponse(false, "Lỗi hệ thống khi lưu dữ liệu.");
        }
    }
}