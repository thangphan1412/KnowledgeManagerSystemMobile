package com.abc.knowledgemanagersystems.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.repository.EquipmentRepository; // <-- Import
import java.util.List;

public class EquipmentViewModel extends AndroidViewModel {

    private EquipmentRepository mRepository;
    private LiveData<List<Equipment>> mAllEquipment;

    public EquipmentViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EquipmentRepository(application);
        mAllEquipment = mRepository.getAllEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return mAllEquipment;
    }

    // THÊM HÀM NÀY (Bắt buộc)
    public LiveData<Equipment> getEquipmentById(int id) {
        return mRepository.getEquipmentById(id); // <-- HÀM MÀ BẠN ĐANG BỊ LỖI
    }
}