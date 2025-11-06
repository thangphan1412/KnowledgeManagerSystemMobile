package com.abc.knowledgemanagersystems.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Sops;

import java.util.ArrayList;
import java.util.List;

public class SopsAdapter extends RecyclerView.Adapter<SopsAdapter.SopViewHolder> {

    private Context context;
    private List<Sops> sopsList;
    private List<Sops> filteredList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sops sop);
    }

    public SopsAdapter(Context context, List<Sops> sopsList, OnItemClickListener listener) {
        this.context = context;
        this.sopsList = sopsList;
        this.filteredList = new ArrayList<>(sopsList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sop, parent, false);
        return new SopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SopViewHolder holder, int position) {
        Sops sop = filteredList.get(position);
        holder.txtTitle.setText(sop.getTitle());
        holder.txtVersion.setText("Version: " + sop.getId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(sop));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Search filter logic
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(sopsList);
        } else {
            for (Sops sop : sopsList) {
                if (sop.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(sop);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class SopViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtVersion;

        public SopViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtSopTitle);
            txtVersion = itemView.findViewById(R.id.txtSopVersion);
        }
    }
}