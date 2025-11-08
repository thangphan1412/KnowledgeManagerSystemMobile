package com.abc.knowledgemanagersystems.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.dao.EquipmentDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Equipment;
import java.util.List;

public class EquipmentRepository {

    private EquipmentDao mEquipmentDao;
    private LiveData<List<Equipment>> mAllEquipment;

    public EquipmentRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        mEquipmentDao = db.equipmentDao();
        mAllEquipment = mEquipmentDao.getAllEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return mAllEquipment;
    }

    public LiveData<Equipment> getEquipmentById(int id) {
        return mEquipmentDao.getEquipmentById(id);
    }
}