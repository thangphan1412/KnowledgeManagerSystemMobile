package com.abc.knowledgemanagersystems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Sops;

import java.util.List;

public class SDSAdapter extends RecyclerView.Adapter<SDSAdapter.ViewHolder> {

    private List<Sops> list;
    private OnSDSClickListener listener;

    public interface OnSDSClickListener {
        void onViewSDSClick(Sops sops);
    }

    public SDSAdapter(List<Sops> list, OnSDSClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sops sops = list.get(position);
        holder.tvChemicalName.setText(sops.getSopsName());
        holder.tvCASNumber.setText(sops.getTitle()); // hoặc dùng một trường khác nếu title không phải CAS
        holder.btnViewSDS.setOnClickListener(v -> listener.onViewSDSClick(sops));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChemicalName, tvCASNumber;
        CardView btnViewSDS;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChemicalName = itemView.findViewById(R.id.tvChemicalName);
            tvCASNumber = itemView.findViewById(R.id.tvCASNumber);
            btnViewSDS = itemView.findViewById(R.id.btnViewSDS);
        }
    }
}