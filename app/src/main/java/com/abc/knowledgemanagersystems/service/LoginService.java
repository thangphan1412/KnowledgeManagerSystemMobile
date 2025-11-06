package com.abc.knowledgemanagersystems.service;

import android.content.Context;

import com.abc.knowledgemanagersystems.API.AuthApi;
import com.abc.knowledgemanagersystems.API.RetrofitClient;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.abc.knowledgemanagersystems.request.LoginRequest;
import com.abc.knowledgemanagersystems.response.LoginResponse;

import retrofit2.Response;


public class LoginService {
    private final AuthApi authApi;

    private final AuthPreferences authPreferences;

    public LoginService(Context context) {
        // Khởi tạo Retrofit Client và AuthPreferences
        this.authApi = RetrofitClient.getAuthApi(); // Giả lập Retrofit Client
        this.authPreferences = new AuthPreferences(context);
    }

    /**
     * Thực hiện cuộc gọi API đăng nhập. PHẢI chạy trên luồng nền.
     */
    public LoginResponse authenticate(String email, String password) throws Exception {
//        if (email.equals("test@lab.com") && password.equals("123456")) {
//
//            LoginResponse mockResponse = new LoginResponse();
//            mockResponse.setJwtToken("mock_jwt_token_12345");
//            if (email.contains("admin")) {
//                mockResponse.setRole("ADMIN");
//            } else {
//                mockResponse.setRole("RESEARCHER");
//            }
//
//            // 3. LƯU TOKEN VÀ ROLE VÀO LƯU TRỮ AN TOÀN (như code gốc)
//            authPreferences.saveAuthData(mockResponse.getJwtToken(), mockResponse.getRole());
//
//            // 4. Trả về thành công
//            return mockResponse;
//
//        } else {
//            // Trả về lỗi nếu nhập sai tài khoản giả lập
//            throw new Exception("Tên đăng nhập hoặc mật khẩu không hợp lệ. (Chỉ chấp nhận: test@lab.com / 123456)");
//        }
//    }
        LoginRequest request = new LoginRequest(email, password);

        // Thao tác đồng bộ Retrofit (chỉ an toàn khi gọi từ luồng nền)
        Response<LoginResponse> response = authApi.login(request).execute();

        if (response.isSuccessful() && response.body() != null) {
            LoginResponse loginData = response.body();

            //  LƯU JWT VÀ ROLE VÀO LƯU TRỮ AN TOÀN
            authPreferences.saveAuthData(loginData.getJwtToken(), loginData.getRole());

            return loginData;
        } else {

            String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Lỗi xác thực không xác định.";
            throw new Exception(errorMsg);
        }
    }
}
