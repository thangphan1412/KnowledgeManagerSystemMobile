package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;

import java.util.List;

@Dao
public interface ExperimentLogsDao {
    @Insert
    void insert(ExperimentLogs experimentLogs);

    @Query("SELECT * FROM experiments_logs")
    List<ExperimentLogs> getAllExperiments();
}
