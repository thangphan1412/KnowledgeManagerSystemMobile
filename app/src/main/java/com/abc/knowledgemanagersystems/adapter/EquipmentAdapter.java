package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Equipment; // Import model CỦA BẠN
import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {

    private List<Equipment> equipmentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Equipment equipment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public EquipmentAdapter(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(itemView);
    }

    // --- SỬA LOGIC Ở HÀM NÀY ---
    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        Equipment currentEquipment = equipmentList.get(position);
        holder.nameTextView.setText(currentEquipment.getName());

        // Gọi getModel() thay vì getStatus()
        holder.modelTextView.setText("Model: " + currentEquipment.getModel());
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    // --- SỬA LOGIC Ở HÀM NÀY ---
    class EquipmentViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView modelTextView; // Sửa tên biến

        public EquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_equipment_name_item);
            modelTextView = itemView.findViewById(R.id.text_view_equipment_model_item); // Sửa ID

            // Set click listener cho cả item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(equipmentList.get(position));
                }
            });
        }
    }
}