package com.abc.knowledgemanagersystems.dao;

import androidx.lifecycle.LiveData; // <-- THÊM IMPORT
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query; // <-- THÊM IMPORT
import com.abc.knowledgemanagersystems.model.Equipment;
import java.util.List; // <-- THÊM IMPORT

@Dao
public interface EquipmentDao {

    @Insert
    void insert(Equipment equipment);

    // THÊM 2 HÀM NÀY (Bắt buộc)
    @Query("SELECT * FROM equipment")
    LiveData<List<Equipment>> getAllEquipment();

    @Query("SELECT * FROM equipment WHERE equipment_id = :id")
    LiveData<Equipment> getEquipmentById(int id); // <-- Sửa lỗi 'getEquipmentById'
}