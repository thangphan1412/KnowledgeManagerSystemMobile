package com.abc.knowledgemanagersystems.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.model.Equipment;
// 🔥 BẠN BỊ THIẾU IMPORT NÀY
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import com.abc.knowledgemanagersystems.repository.EquipmentRepository;

import java.util.List;

/**
 * ViewModel mà EquipmentListActivity và EquipmentDetailActivity sử dụng.
 */
public class EquipmentViewModel extends AndroidViewModel {

    private EquipmentRepository mRepository;
    private LiveData<List<Equipment>> mAllEquipment;

    public EquipmentViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EquipmentRepository(application);
        mAllEquipment = mRepository.getAllEquipment();
    }

    // Hàm mà EquipmentListActivity gọi
    public LiveData<List<Equipment>> getAllEquipment() {
        return mAllEquipment;
    }

    // Hàm mà EquipmentDetailActivity gọi
    public LiveData<Equipment> getEquipmentById(int id) {
        return mRepository.getEquipmentById(id);
    }

    // 🔥 BẠN BỊ THIẾU HÀM NÀY
    // Hàm mà EquipmentDetailActivity gọi
    public LiveData<List<MaintenanceLog>> getLogsForEquipment(int equipmentId) {
        return mRepository.getLogsForEquipment(equipmentId);
    }
}