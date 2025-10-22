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
        foreignKeys = {
        @ForeignKey(
                        entity = Users.class,
                        parentColumns = "user_id",
                        childColumns = "users_id",
                        onDelete = ForeignKey.CASCADE
                ),
         @ForeignKey(
                 entity = Sops.class,
                 parentColumns = "sop_id",
                 childColumns = "sops_id",
                 onDelete = ForeignKey.CASCADE
         )
        }
)
public class InventoryItem {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "inventory_item_id")
    private int id;
    private String name;
    private String fomula;
    private String units;
    private double quantity;
    private String location;
    private String expiredDate;
    private StatusInventoryItem statusInventoryItem;
    @ColumnInfo(name = "users_id")
    private int userId;
    @ColumnInfo(name = "sops_id")
    private int sopId;

}
