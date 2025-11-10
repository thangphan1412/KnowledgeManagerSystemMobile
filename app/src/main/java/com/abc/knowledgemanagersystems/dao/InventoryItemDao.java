package com.abc.knowledgemanagersystems.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abc.knowledgemanagersystems.model.InventoryItem;

import java.util.List;

@Dao
public interface InventoryItemDao {

    // SỬA HÀM NÀY (từ void thành long)
    @Insert
    long insert(InventoryItem inventoryItem); // <-- Phải trả về 'long'
    @Query("SELECT * FROM inventory_item")
    List<InventoryItem> getAllItems();

    @Query("SELECT * FROM inventory_item WHERE inventory_item_id = :id LIMIT 1")
    InventoryItem getItemById(int id);

    @Update
    void update(InventoryItem item);

}