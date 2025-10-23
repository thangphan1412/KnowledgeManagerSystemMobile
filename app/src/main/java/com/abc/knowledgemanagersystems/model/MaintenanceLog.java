package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "maintenance_logs",
        foreignKeys = @ForeignKey(
                // Liên kết với máy móc
                entity = Equipment.class,
                parentColumns = "equipment_id",
                childColumns = "equipment_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("equipment_id")}
)
public class MaintenanceLog {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "log_id")
    private int id;

    // Ngày thực hiện bảo trì (Unix Timestamp)
    private long date;

    // Mô tả công việc đã làm
    private String description;

    // Kỹ thuật viên thực hiện
    @ColumnInfo(name = "technician_name")
    private String technicianName;

    // ID của máy móc được bảo trì
    @ColumnInfo(name = "equipment_id")
    private int equipmentId;
}