package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity(tableName = "inventory_logs")
public class InventoryLogs {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "inventory_log_id")
    private UUID id;
    private String description;
    private LocalDate date;

}
