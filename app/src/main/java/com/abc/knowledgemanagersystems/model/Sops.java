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
@Entity(tableName = "sops",
    foreignKeys = {
        @ForeignKey(
                    entity = ExperimentLogs.class,
                    parentColumns = "experiment_id",
                    childColumns = "experiments_id",
                    onDelete = ForeignKey.CASCADE
            )
    }
)
public class Sops {
    @PrimaryKey
    @NotNull
    @ColumnInfo
    private UUID id;
    private String title;
    private String description;
    private LocalDate createAt;
    private String filePath;
    private String safeDataSheet;
    @ColumnInfo(name ="experiments_id" )
    private UUID experimentId;

}
