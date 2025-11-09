package com.abc.knowledgemanagersystems.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.repository.BookingRepository;
import java.util.List;
import java.util.concurrent.Future;

public class BookingViewModel extends AndroidViewModel {
    private BookingRepository mRepository;
    public BookingViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookingRepository(application);
    }
    public LiveData<List<Booking>> getBookingsForEquipment(int equipmentId) {
        return mRepository.getBookingsForEquipment(equipmentId);
    }
    public void insert(Booking booking) {
        mRepository.insert(booking);
    }

    // Core Logic: Kiểm tra Xung đột (Conflict Validation)
    public Future<Boolean> hasConflict(int equipmentId, long newStartTime, long newEndTime) {
        return AppDataBase.databaseWriteExecutor.submit(() -> {
            List<Booking> existingBookings = mRepository.getBookingsForEquipmentSync(equipmentId);
            for (Booking existing : existingBookings) {
                if (newStartTime < existing.getEndTime() && newEndTime > existing.getStartTime()) {
                    return true; // Bị trùng lịch!
                }
            }
            return false; // Không trùng
        });
    }
}