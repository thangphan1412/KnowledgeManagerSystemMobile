package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Experiment;

@Dao
public interface ExperimentDao {
    @Insert
    void insert(Experiment experiment);
}
