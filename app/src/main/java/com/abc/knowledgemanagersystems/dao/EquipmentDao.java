package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Equipment;

@Dao
public interface EquipmentDao {
    @Insert
    void insert(Equipment equipment);
}
