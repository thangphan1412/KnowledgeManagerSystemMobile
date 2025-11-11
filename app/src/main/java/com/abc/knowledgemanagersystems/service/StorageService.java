package com.abc.knowledgemanagersystems.service;

import android.content.Context;
import android.net.Uri;

import com.abc.knowledgemanagersystems.status.LogEntryType;

public class StorageService {
    private final Context context;

    public StorageService(Context context) {
        this.context = context;
    }

    /**
     * Giả lập việc tải tệp tin lên Cloud.
     * Trong ứng dụng thực tế, hàm này sẽ chứa logic gọi API hoặc Firebase Storage.
     *
     * @param fileUri Uri của tệp tin cục bộ.
     * @param type Loại mục nhập (IMAGE, DATA_FILE).
     * @return URL công khai của tệp tin đã được tải lên.
     * @throws Exception Giả lập lỗi nếu cần.
     */
    public String uploadFile(Uri fileUri, LogEntryType type) throws Exception {
        // --- LOGIC GIẢ LẬP TẢI LÊN (MOCK) ---

        // 1. Kiểm tra Uri
        if (fileUri == null) {
            throw new IllegalArgumentException("File URI không được rỗng.");
        }

        // 2. Tạo URL giả lập dựa trên thời gian hiện tại và loại tệp
        long timestamp = System.currentTimeMillis();
        String fileExtension = getFileExtension(fileUri);

        // Trả về một URL giả lập. Trong môi trường thực, đây là URL của Firebase/AWS.
        String mockUrl = String.format(
                "https://mock-storage.kms.com/%s/%d.%s",
                type.name().toLowerCase(),
                timestamp,
                fileExtension
        );

        // Giả lập thời gian tải lên
        Thread.sleep(1500); // 1.5 giây

        // --- KẾT THÚC LOGIC GIẢ LẬP ---

        return mockUrl;
    }

    // Phương thức trợ giúp để lấy phần mở rộng tệp (ví dụ: jpg, pdf)
    private String getFileExtension(Uri uri) {
        String path = uri.getPath();
        if (path != null && path.lastIndexOf('.') != -1) {
            return path.substring(path.lastIndexOf('.') + 1);
        }
        return "bin"; // Mặc định nếu không tìm thấy
    }
}
