package com.abc.knowledgemanagersystems.model;

import android.icu.util.LocaleData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "experiments_logs",
        foreignKeys = @ForeignKey(
                entity = Users.class,
                parentColumns = "user_id",
                childColumns = "user_owner_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class ExperimentLogs {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "experiment_log_id")
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    private UUID user_owner_id;

}
