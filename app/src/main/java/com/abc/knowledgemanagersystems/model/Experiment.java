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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "experiment",
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
public class Experiment {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "experiment_id")
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @ColumnInfo(name = "users_id")
    private UUID userId;
    @ColumnInfo(name = "sops_id")
    private UUID sopId;
}
