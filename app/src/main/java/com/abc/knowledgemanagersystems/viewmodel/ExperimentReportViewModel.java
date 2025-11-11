package com.abc.knowledgemanagersystems.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abc.knowledgemanagersystems.API.ApiService;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.dto.response.ReportResponse;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.service.RetrofitClient;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperimentReportViewModel extends AndroidViewModel {
    // 🔥 BỎ FAKE SERVICE
    // private final ExperimentReportService reportService;

    // 🔥 THÊM API THẬT
    private final ApiService apiService;

    private final ExecutorService databaseExecutor;

    private final MutableLiveData<Experiment> experimentData = new MutableLiveData<>();
    private final MutableLiveData<String> pdfDownloadUrl = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ExperimentReportViewModel(@NonNull Application application) {
        super(application);
        // 🔥 KHỞI TẠO API THẬT
        this.apiService = RetrofitClient.getApiService();
        this.databaseExecutor = AppDataBase.databaseWriteExecutor;
    }

    // --- Getters (Không đổi) ---
    public LiveData<Experiment> getExperiment() { return experimentData; }
    public LiveData<String> getPdfDownloadUrl() { return pdfDownloadUrl; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // --- Hành động (Không đổi) ---
    public void loadExperimentDetails(int experimentId) {
        databaseExecutor.execute(() -> {
            // (Code lấy dữ liệu từ Room... không thay đổi)

            // --- Dùng dữ liệu giả lập (Mock) cho mục đích demo ---
            Experiment mockExperiment = new Experiment();
            mockExperiment.setId(experimentId);
            mockExperiment.setTitle("Thí nghiệm Phân tích Protein");
            experimentData.postValue(mockExperiment);
        });
    }

    /**
     * 2. 🔥 SỬA LẠI HÀM NÀY: Bắt đầu quá trình gọi API thật
     */
    public void startPdfExport(int experimentId) {

        // Gọi API bằng Retrofit
        Call<ReportResponse> call = apiService.getExperimentReport(experimentId);

        // Chạy bất đồng bộ
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // THÀNH CÔNG: Server trả về JSON, lấy link URL
                    String url = response.body().getDownloadUrl();
                    pdfDownloadUrl.setValue(url);
                } else {
                    // Lỗi: Server trả về mã lỗi (404, 500, v.v.)
                    errorMessage.setValue("Lỗi Server: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                // Lỗi: Không có kết nối mạng, hoặc không kết nối được server
                errorMessage.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}
