package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;

@Dao
public interface ExperimentLogsDao {
    @Insert
    void insert(ExperimentLogs experimentLogs);
}
