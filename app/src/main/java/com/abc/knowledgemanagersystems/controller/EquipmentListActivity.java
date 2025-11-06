package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.adapter.EquipmentAdapter;
import com.abc.knowledgemanagersystems.model.Equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EquipmentAdapter adapter;
    private List<Equipment> equipmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

        recyclerView = findViewById(R.id.recycler_view_equipment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Tải dữ liệu (ĐÃ SỬA)
        loadPlaceholderData();

        adapter = new EquipmentAdapter(equipmentList);
        recyclerView.setAdapter(adapter);

        // 4. Xử lý click (ĐÃ SỬA)
        adapter.setOnItemClickListener(equipment -> {
            Intent intent = new Intent(EquipmentListActivity.this, EquipmentDetailActivity.class);

            // ID của bạn là 'int', không phải 'long'
            intent.putExtra("EQUIPMENT_ID", equipment.getId());

            startActivity(intent);
        });
    }

    // --- SỬA LOGIC Ở HÀM NÀY ---
    private void loadPlaceholderData() {
        // Dùng NoArgsConstructor và Setter (vì model của bạn có Lombok)
        Equipment eq1 = new Equipment();
        eq1.setId(101); // ID của bạn là 'int'
        eq1.setName("HPLC Machine #1");
        eq1.setModel("Agilent 1260"); // Model của bạn có trường 'model'
        equipmentList.add(eq1);

        Equipment eq2 = new Equipment();
        eq2.setId(102);
        eq2.setName("Centrifuge");
        eq2.setModel("Eppendorf 5424 R");
        equipmentList.add(eq2);

        Equipment eq3 = new Equipment();
        eq3.setId(103);
        eq3.setName("PCR Machine");
        eq3.setModel("Bio-Rad T100");
        equipmentList.add(eq3);
    }
}