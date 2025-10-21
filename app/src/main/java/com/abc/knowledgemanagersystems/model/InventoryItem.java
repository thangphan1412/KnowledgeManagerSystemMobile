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
@Entity(tableName = "inventory_item",
        foreignKeys = @ForeignKey(
                entity = Users.class,
                parentColumns = "user_id",
                childColumns = "user_owner_id",
                onDelete = ForeignKey.CASCADE
        )
)
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
    private UUID user_owner_id;

}
