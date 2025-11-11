package com.abc.knowledgemanagersystems.API;

import com.abc.knowledgemanagersystems.dto.response.ReportResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    /**
     * Gửi yêu cầu GET tới (ví dụ) "https://api.your-lab.com/api/v1/experiments/123/report"
     * @param experimentId ID của thí nghiệm
     * @return một đối tượng Call chứa ReportResponse (với link download)
     */
    @GET("api/v1/experiments/{id}/report")
    Call<ReportResponse> getExperimentReport(@Path("id") int experimentId);

}
