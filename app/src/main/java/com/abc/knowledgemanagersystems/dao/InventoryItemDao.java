package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.InventoryItem;

@Dao
public interface InventoryItemDao {
    @Insert
    void insert(InventoryItem inventoryItem);
}
