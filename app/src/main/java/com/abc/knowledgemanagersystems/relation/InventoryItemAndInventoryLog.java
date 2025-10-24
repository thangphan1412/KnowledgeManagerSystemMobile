package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.model.Booking;

import java.util.List;

public class InventoryItemAndInventoryLog {
    @Embedded
    public InventoryItem inventoryItem;
    @Relation(
            parentColumn = "inventory_item_id",
            entityColumn = "inventorys_item_id"
    )
    public List<InventoryLogs> inventoryLogs;
}
