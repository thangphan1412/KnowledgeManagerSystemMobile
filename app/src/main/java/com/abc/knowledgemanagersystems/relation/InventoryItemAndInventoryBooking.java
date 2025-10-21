package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InvetoryBooking;
import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

public class InventoryItemAndInventoryBooking {
    @Embedded
    public InventoryItem inventoryItem;
    @Relation(
            parentColumn = "inventory_item_id",
            entityColumn = "inventorys_item_id"
    )
    public List<InvetoryBooking> invetoryBookings;
}
