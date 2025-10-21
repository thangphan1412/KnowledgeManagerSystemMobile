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
                    parentColumns = "experiment_log_id",
                    childColumns = "experiments_log_id",
                    onDelete = ForeignKey.CASCADE
            )
    }
)
public class Sops {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "sop_id")
    private int id;
    private String title;
    private String description;
    private String createAt;
    private String filePath;
    private String safeDataSheet;
    @ColumnInfo(name ="experiments_log_id" )
    private int experimentId;

}
