package com.abc.knowledgemanagersystems.API;

import com.abc.knowledgemanagersystems.dto.request.LoginRequest;
import com.abc.knowledgemanagersystems.dto.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
