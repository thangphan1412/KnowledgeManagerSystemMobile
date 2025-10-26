package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.abc.knowledgemanagersystems.model.Experiment;

import java.util.List;

@Dao
public interface ExperimentDao {
    @Insert
    long insert(Experiment experiment);

}
