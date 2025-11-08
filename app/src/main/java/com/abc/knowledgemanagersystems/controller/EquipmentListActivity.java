package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer; // <-- THÊM IMPORT NÀY
import androidx.lifecycle.ViewModelProvider; // <-- THÊM IMPORT NÀY
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
// SỬA IMPORT NÀY để trỏ đến package 'adapter' của bạn
import com.abc.knowledgemanagersystems.adapter.EquipmentAdapter;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.viewmodel.EquipmentViewModel; // <-- THÊM IMPORT NÀY

import java.util.ArrayList;
import java.util.List;

public class EquipmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EquipmentAdapter adapter; // Adapter từ package 'adapter'
    private EquipmentViewModel equipmentViewModel; // <-- THÊM VIEWMODEL

    // Khởi tạo list rỗng
    private List<Equipment> equipmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

        recyclerView = findViewById(R.id.recycler_view_equipment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Khởi tạo Adapter với danh sách RỖNG
        // (Dữ liệu sẽ được nạp vào ở Bước 3)
        adapter = new EquipmentAdapter(equipmentList);
        recyclerView.setAdapter(adapter);

        // 2. LẤY VIEWMODEL (TỪ BƯỚC 3)
        equipmentViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);

        // 3. LẮNG NGHE (OBSERVE) THAY ĐỔI TỪ DATABASE
        // Đây là "Core Logic" mới của bạn
        equipmentViewModel.getAllEquipment().observe(this, new Observer<List<Equipment>>() {
            @Override
            public void onChanged(List<Equipment> equipments) {
                // KHI DỮ LIỆU THAY ĐỔI (TỪ DATABASE)

                // Cập nhật danh sách trong Adapter
                equipmentList.clear();
                equipmentList.addAll(equipments);
                adapter.notifyDataSetChanged(); // Báo cho RecyclerView vẽ lại
            }
        });

        // 4. XỬ LÝ CLICK (Giữ nguyên logic của bạn)
        // (Sử dụng interface 'OnItemClickListener' mà bạn đã định nghĩa)
        adapter.setOnItemClickListener(equipment -> {
            Intent intent = new Intent(EquipmentListActivity.this, EquipmentDetailActivity.class);
            intent.putExtra("EQUIPMENT_ID", equipment.getId());
            startActivity(intent);
        });
    }

    // 5. XÓA BỎ HÀM loadPlaceholderData()
    // (Vì dữ liệu giờ đã được lấy từ Database)
    // private void loadPlaceholderData() { ... }
}