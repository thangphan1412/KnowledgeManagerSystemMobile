package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.status.LogEntryType;

import java.util.List;

public class LogEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<ExperimentLogs> logEntries;

    // Khai báo các loại View (tương ứng với các layout item XML)
    private static final int VIEW_TYPE_NOTE = 1;
    private static final int VIEW_TYPE_IMAGE = 2;
    private static final int VIEW_TYPE_DATA_FILE = 3;

    public LogEntryAdapter(List<ExperimentLogs> logEntries) {
        this.logEntries = logEntries;
    }

    // Phương thức cập nhật danh sách
    public void updateList(List<ExperimentLogs> newEntries) {
        logEntries.clear();
        logEntries.addAll(newEntries);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        LogEntryType type = logEntries.get(position).getLogType();
        if (type == LogEntryType.IMAGE) {
            return VIEW_TYPE_IMAGE;
        } else if (type == LogEntryType.DATA_FILE) {
            return VIEW_TYPE_DATA_FILE;
        } else {
            return VIEW_TYPE_NOTE; // Dùng NOTE cho cả NOTE và OBSERVATION
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.item_logbook_image, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == VIEW_TYPE_DATA_FILE) {
            // TODO: Triển khai item_logbook_file.xml và FileViewHolder
            View view = inflater.inflate(R.layout.item_logbook_note, parent, false);
            return new NoteViewHolder(view); // Dùng tạm Note
        } else {
            View view = inflater.inflate(R.layout.item_logbook_note, parent, false);
            return new NoteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExperimentLogs entry = logEntries.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_NOTE) {
            ((NoteViewHolder) holder).bind(entry);
        } else if (holder.getItemViewType() == VIEW_TYPE_IMAGE) {
            ((ImageViewHolder) holder).bind(entry);
        }
    }

    @Override
    public int getItemCount() {
        return logEntries.size();
    }

    // ViewHolder cho Ghi chú
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        TextView content;
        // Thêm các TextView khác cho user và result...

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.textViewNoteTimestamp);
            content = itemView.findViewById(R.id.textViewNoteContent);
        }

        public void bind(ExperimentLogs entry) {
            timestamp.setText(entry.getLogDate());
            content.setText(entry.getContent());
        }
    }

    // ViewHolder cho Hình ảnh
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        // ImageView imageView; // Cần dùng thư viện tải ảnh (Glide/Picasso)

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.textViewImageTimestamp);
            // imageView = itemView.findViewById(R.id.imageViewUploaded);
        }

        public void bind(ExperimentLogs entry) {
            timestamp.setText(entry.getLogDate());
            // TODO: Dùng Glide/Picasso để tải ảnh vào imageView từ entry.getContent() (URL)
        }
    }
}
