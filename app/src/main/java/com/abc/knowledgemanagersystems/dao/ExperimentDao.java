package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abc.knowledgemanagersystems.model.Experiment;

import java.util.List;

@Dao
public interface ExperimentDao {

    @Insert
    long insert(Experiment experiment);

    @Update
    void update(Experiment experiment);

    @Query("SELECT * FROM experiment WHERE users_id = :userId ORDER BY startDate DESC")
    List<Experiment> getExperimentsByUserId(int userId);

    @Query("SELECT * FROM experiment WHERE experiment_id = :experimentId")
    Experiment getExperimentById(int experimentId);

    // ✅ PHƯƠNG THỨC MỚI CẦN THÊM VÀO DAO ĐỂ FIX LỖI
    /**
     * Cập nhật ID từ Server và đặt trạng thái đồng bộ là true.
     * Sử dụng tên cột chính xác: experiment_id, serverExperimentId, isSynced
     */
    @Query("UPDATE experiment SET serverExperimentId = :serverId, isSynced = 1 WHERE experiment_id = :localId")
    void updateServerInfo(int localId, String serverId); // <--- Thêm phương thức này

    @Query("SELECT * FROM experiment")
    List<Experiment> getAllExperiments();
}