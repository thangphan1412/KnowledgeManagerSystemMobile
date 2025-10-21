package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.InventoryLogs;

@Dao
public interface InventoryLogDao {
    @Insert
    void insert(InventoryLogs inventoryLogs);
}
