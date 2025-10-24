package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// (Bỏ import StatusInventoryItem vì nó không còn liên quan)

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// ĐỔI TÊN BẢNG TỪ "inventory_booking" -> "bookings"
@Entity(tableName = "bookings",
        foreignKeys = {
                @ForeignKey(
                        entity = Users.class,
                        parentColumns = "user_id",
                        childColumns = "users_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(

                        entity = Equipment.class,
                        parentColumns = "equipment_id",
                        childColumns = "equipment_id",
                        onDelete = ForeignKey.CASCADE
                )
        },

        indices = {@Index("users_id"), @Index("equipment_id")}
)

public class Booking {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "booking_id")
    private int id;


    @ColumnInfo(name = "start_time")
    private long startTime;

    @ColumnInfo(name = "end_time")
    private long endTime;


    private String status;

    @ColumnInfo(name = "users_id")
    private int userId;

    @ColumnInfo(name = "equipment_id")
    private int equipmentId;


}