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

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "log_id")
    private int id;


    private long date;


    private String description;


    @ColumnInfo(name = "technician_name")
    private String technicianName;

    @ColumnInfo(name = "equipment_id")
    private int equipmentId;
}