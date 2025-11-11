package com.abc.knowledgemanagersystems.dto.response;

import com.google.gson.annotations.SerializedName;

public class ReportResponse {
    @SerializedName("downloadUrl")
    private String downloadUrl;

    @SerializedName("message")
    private String message;

    // --- Getters ---

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getMessage() {
        return message;
    }
}
