package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.InventoryAdapter;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InventoryManagementActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<InventoryItem> inventoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter với danh sách rỗng
        adapter = new InventoryAdapter(inventoryList, item -> {
            // Bấm nút Update → mở form AddUpdateInventoryActivity
            Intent intent = new Intent(InventoryManagementActivity.this, AddUpdateInventoryActivity.class);
            intent.putExtra("item_id", item.getId()); // truyền id để load dữ liệu
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Nút thêm vật tư mới
        fabAdd = findViewById(R.id.fab_add_inventory_item);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryManagementActivity.this, AddUpdateInventoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadInventory() {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            List<InventoryItem> items = AppDataBase.getInstance(this).inventoryItemDao().getAllItems();
            runOnUiThread(() -> {
                inventoryList.clear();
                inventoryList.addAll(items);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventory();
    }
}
