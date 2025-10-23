package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.model.Equipment;

import java.util.List;

public class EquipmentWithBookings {
    @Embedded
    public Equipment equipment;

    @Relation(
            parentColumn = "equipment_id", // Khóa chính của Equipment
            entityColumn = "equipment_id"  // Khóa ngoại trong Booking
    )
    public List<Booking> bookings;
}