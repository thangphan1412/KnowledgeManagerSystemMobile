package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity(tableName = "inventory_item")
public class InventoryItem {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "inventory_id")
    private UUID id;
    private String name;
    private String fomula;
    private double units;
    private double quantity;
    private String location;
    private LocalDate expiredDate;

}
