package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.MaintenanceLog;

import java.util.List;

public class EquipmentWithMaintenanceLogs {
    @Embedded
    public Equipment equipment;

    @Relation(
            parentColumn = "equipment_id", // Khóa chính của Equipment
            entityColumn = "equipment_id"  // Khóa ngoại trong MaintenanceLog
    )
    public List<MaintenanceLog> maintenanceLogs;
}