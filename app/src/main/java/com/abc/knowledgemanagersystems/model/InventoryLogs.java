package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "inventory_logs",
        foreignKeys = {
        @ForeignKey(
                        entity = Users.class,
                        parentColumns = "user_id",
                        childColumns = "users_id",
                        onDelete = ForeignKey.CASCADE
                ),
        @ForeignKey(
                entity = InventoryItem.class,
                parentColumns = "inventory_item_id",
                childColumns = "inventorys_item_id",
                onDelete = ForeignKey.CASCADE
        )
        }
)
public class InventoryLogs {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "inventory_log_id")
    private UUID id;
    private String description;
    private LocalDate date;
    @ColumnInfo(name = "users_id")
    private UUID userID;
    @ColumnInfo(name = "inventorys_item_id")
    private UUID inventoryItemId;
}
