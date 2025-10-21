package com.abc.knowledgemanagersystems.model;

import android.icu.util.LocaleData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Entity(tableName = "experiments_logs")
public class ExperimentLogs {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "experiment_log_id")
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

}
