package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
@Entity(tableName = "step",
        foreignKeys = {
                @ForeignKey(

                        entity = Sops.class,
                        parentColumns = "sop_id",
                        childColumns = "sops_id",
                        onDelete = ForeignKey.CASCADE
                )

        }
)
public class Step {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "step_id")
    private int id;
    private String safetyNote;
    private int stepNumber;
    private String description;
    @ColumnInfo(name = "sops_id")
    private int sopId;
}
