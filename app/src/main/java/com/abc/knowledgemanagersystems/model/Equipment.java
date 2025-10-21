package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import javax.annotation.processing.Generated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Entity(tableName = "equipment",
        foreignKeys = @ForeignKey(
                entity = InventoryItem.class,
                parentColumns = "inventory_item_id",
                childColumns = "inventorys_item_id",
                onDelete = ForeignKey.CASCADE
        )
)
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
    @ColumnInfo(name = "users_id")
    private UUID userId;
    @ColumnInfo(name = "inventorys_item_id")
    private UUID inventoryItemId;

}
