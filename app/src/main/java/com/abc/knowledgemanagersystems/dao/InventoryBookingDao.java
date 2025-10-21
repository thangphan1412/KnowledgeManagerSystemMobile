package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.InvetoryBooking;

@Dao
public interface InventoryBookingDao {
    @Insert
    void insert(InvetoryBooking invetoryBooking);
}
