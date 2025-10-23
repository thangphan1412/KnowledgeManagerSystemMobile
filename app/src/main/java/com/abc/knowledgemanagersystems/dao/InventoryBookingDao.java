package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Booking;


@Dao
public interface InventoryBookingDao {
    @Insert
    void insert(Booking invetoryBooking);
}
