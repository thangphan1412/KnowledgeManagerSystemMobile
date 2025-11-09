package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MaintenanceLogAdapter extends RecyclerView.Adapter<MaintenanceLogAdapter.LogViewHolder> {

    private List<MaintenanceLog> mLogs;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public MaintenanceLogAdapter(List<MaintenanceLog> logs) {
        mLogs = logs;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dùng Layout (đã sửa ở Bước 1)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        MaintenanceLog currentLog = mLogs.get(position);

        // --- SỬA LOGIC Ở ĐÂY ---
        // (Dùng các hàm getter mới từ file model_194 của bạn)
        holder.logDescription.setText(currentLog.getDescription());
        holder.logTechnician.setText("Thực hiện bởi: " + currentLog.getTechnicianName());

        // Chuyển long (timestamp) về String (Date)
        String dateString = sdf.format(new Date(currentLog.getDate()));
        holder.logDate.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    public void setLogs(List<MaintenanceLog> logs) {
        mLogs = logs;
        notifyDataSetChanged();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {
        // --- SỬA CÁC BIẾN NÀY (Khớp với Bước 1) ---
        private final TextView logDescription;
        private final TextView logDate;
        private final TextView logTechnician;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            // --- SỬA CÁC ID NÀY (Khớp với Bước 1) ---
            logDescription = itemView.findViewById(R.id.text_view_log_description);
            logDate = itemView.findViewById(R.id.text_view_log_date);
            logTechnician = itemView.findViewById(R.id.text_view_log_technician);
        }
    }
}