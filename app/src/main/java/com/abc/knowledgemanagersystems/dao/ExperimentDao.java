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
    @Query("UPDATE experiment SET serverExperimentId = :serverId, isSynced = 1 WHERE experiment_id = :localId")
    void updateServerInfo(int localId, String serverId);
}
