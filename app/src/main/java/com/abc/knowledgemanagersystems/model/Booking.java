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
                        entity = Users.class, // Giữ lại: Cần biết AI là người đặt
                        parentColumns = "user_id",
                        childColumns = "users_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        // SỬA LOGIC: Trỏ đến Equipment (Máy móc)
                        entity = Equipment.class,
                        // Dùng "equipment_id" từ file Equipment.java mới
                        parentColumns = "equipment_id",
                        childColumns = "equipment_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        // Thêm Index để tăng tốc độ truy vấn
        indices = {@Index("users_id"), @Index("equipment_id")}
)
// ĐỔI TÊN CLASS TỪ "InvetoryBooking" -> "Booking"
public class Booking {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "booking_id")
    private int id;

    // Đổi kiểu String sang long (Unix Timestamp) để dễ so sánh
    @ColumnInfo(name = "start_time")
    private long startTime;

    @ColumnInfo(name = "end_time")
    private long endTime;

    // Trạng thái đặt lịch: "Confirmed", "Pending", "Cancelled"
    private String status;

    @ColumnInfo(name = "users_id")
    private int userId; // Người đặt

    @ColumnInfo(name = "equipment_id") // ID của máy móc được đặt
    private int equipmentId;

    /*
     * ĐÃ XÓA:
     * - private int inventoryItemId;
     */
}