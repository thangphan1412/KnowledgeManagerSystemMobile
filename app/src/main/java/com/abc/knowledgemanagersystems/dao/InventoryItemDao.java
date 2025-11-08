package com.abc.knowledgemanagersystems.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import com.abc.knowledgemanagersystems.model.InventoryItem;

@Dao
public interface InventoryItemDao {

    // SỬA HÀM NÀY (từ void thành long)
    @Insert
    long insert(InventoryItem inventoryItem); // <-- Phải trả về 'long'
}