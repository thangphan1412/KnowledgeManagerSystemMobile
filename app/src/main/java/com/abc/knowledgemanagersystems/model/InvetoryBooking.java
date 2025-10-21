package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity(tableName = "inventory_booking")
public class InvetoryBooking {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "inventory_booking_id")
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;

}
