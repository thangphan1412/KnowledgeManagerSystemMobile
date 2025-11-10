package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.google.android.material.button.MaterialButton;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private List<InventoryItem> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onUpdateClick(InventoryItem item);
    }

    public InventoryAdapter(List<InventoryItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryItem item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.quantity.setText(item.getQuantity() + " " + item.getUnits());
        holder.location.setText(item.getLocation());
        holder.expiryDate.setText("HSD: " + item.getExpiredDate());

        holder.buttonUpdate.setOnClickListener(v -> {
            if (listener != null) listener.onUpdateClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, location, expiryDate;
        MaterialButton buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            location = itemView.findViewById(R.id.item_location);
            expiryDate = itemView.findViewById(R.id.item_expiry_date);
            buttonUpdate = itemView.findViewById(R.id.button_update_item);
        }
    }
}

