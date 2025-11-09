package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abc.knowledgemanagersystems.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);

    @Query("SELECT * FROM step WHERE sops_id = :sopId ORDER BY stepNumber ASC")
    List<Step> getStepsBySopId(int sopId);

    @Query("SELECT * FROM step WHERE step_id = :stepId")
    Step getStepById(int stepId);

    @Query("DELETE FROM step WHERE sops_id = :sopId")
    void deleteAllStepsBySopId(int sopId);

    @Query("SELECT COUNT(*) FROM step WHERE sops_id = :sopId")
    int getStepCountBySopId(int sopId);

    @Query("SELECT * FROM step ORDER BY stepNumber ASC")
    List<Step> getAllSteps();
}
