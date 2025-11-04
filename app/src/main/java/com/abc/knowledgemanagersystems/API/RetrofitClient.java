package com.abc.knowledgemanagersystems.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://your-api-base-url.com/api/v1/";

    private static Retrofit retrofit;


    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // Thêm GsonConverterFactory để tự động chuyển đổi JSON sang Java Object
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static AuthApi getAuthApi() {
        return getRetrofitInstance().create(AuthApi.class);
    }

    /*
    private static OkHttpClient getClientWithInterceptor() {
         HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
         logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Ghi log chi tiết

         return new OkHttpClient.Builder()
                 .addInterceptor(logging)
                 .addInterceptor(chain -> {
                     Request original = chain.request();
                     // Lấy token từ AuthPreferences (như đã định nghĩa)
                     String token = new AuthPreferences(context).getJwtToken();

                     if (token != null) {
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                     }
                     return chain.proceed(original);
                 })
                 .build();
    }

     */
}
