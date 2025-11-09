package com.abc.knowledgemanagersystems.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.dao.EquipmentDao;
// 🔥 THÊM IMPORT
import com.abc.knowledgemanagersystems.dao.MaintenanceLogDao;
import com.abc.knowledgemanagersystems.model.Equipment;
// 🔥 THÊM IMPORT
import com.abc.knowledgemanagersystems.model.MaintenanceLog;

import java.util.List;

/**
 * Repository quản lý dữ liệu cho Equipment.
 * Lớp này lấy dữ liệu từ các DAO.
 */
public class EquipmentRepository {

    private EquipmentDao mEquipmentDao;
    // 🔥 THÊM DAO
    private MaintenanceLogDao mMaintenanceLogDao;
    private LiveData<List<Equipment>> mAllEquipment;

    public EquipmentRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        mEquipmentDao = db.equipmentDao();
        // 🔥 KHỞI TẠO DAO
        mMaintenanceLogDao = db.maintenanceLogDao();
        mAllEquipment = mEquipmentDao.getAllEquipment();
    }

    // Trả về tất cả Equipment
    public LiveData<List<Equipment>> getAllEquipment() {
        return mAllEquipment;
    }

    // Trả về một Equipment cụ thể bằng ID
    public LiveData<Equipment> getEquipmentById(int id) {
        return mEquipmentDao.getEquipmentById(id);
    }

    // 🔥 THÊM HÀM CÒN THIẾU
    // Trả về tất cả Log bảo trì cho một Equipment
    public LiveData<List<MaintenanceLog>> getLogsForEquipment(int equipmentId) {
        return mMaintenanceLogDao.getLogsForEquipment(equipmentId);
    }
}