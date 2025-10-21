package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity(tableName = "equipment")
public class Equipment {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "equipment_id")
    private UUID id;
    private String name;
    private double quantity;
    private String formula;
    private String model;
    private String serial_number;

}
