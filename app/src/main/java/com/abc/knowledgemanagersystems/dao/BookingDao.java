package com.abc.knowledgemanagersystems.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.abc.knowledgemanagersystems.model.Booking;
import java.util.List;

@Dao
public interface BookingDao { // <-- Đã đổi tên
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Booking booking);

    @Query("SELECT * FROM bookings WHERE equipment_id = :equipmentId ORDER BY start_time ASC")
    LiveData<List<Booking>> getBookingsForEquipment(int equipmentId);

    @Query("SELECT * FROM bookings WHERE equipment_id = :equipmentId")
    List<Booking> getBookingsForEquipmentSync(int equipmentId);
}