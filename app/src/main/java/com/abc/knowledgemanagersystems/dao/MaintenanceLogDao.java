package com.abc.knowledgemanagersystems.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import java.util.List;

/**
 * DAO (Data Access Object) cho bảng MaintenanceLog.
 */
@Dao
public interface MaintenanceLogDao {

    /**
     * Dùng trong AppDataBase (Callback) để chèn log mẫu.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MaintenanceLog maintenanceLog);

    /**
     * Dùng trong EquipmentRepository -> EquipmentViewModel -> EquipmentDetailActivity
     * để hiển thị danh sách nhật ký bảo trì cho một thiết bị cụ thể.
     */
    @Query("SELECT * FROM maintenance_logs WHERE equipment_id = :equipmentId ORDER BY date DESC")
    LiveData<List<MaintenanceLog>> getLogsForEquipment(int equipmentId);
}