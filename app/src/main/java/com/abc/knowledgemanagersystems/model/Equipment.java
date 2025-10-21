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
                entity = Users.class,
                parentColumns = "user_id",
                childColumns = "user_owner_id",
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
    private UUID user_owner_id;

}
