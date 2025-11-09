package com.abc.knowledgemanagersystems.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.abc.knowledgemanagersystems.dao.BookingDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Booking;
import java.util.List;

public class BookingRepository {
    private BookingDao mBookingDao;
    public BookingRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        mBookingDao = db.bookingDao();
    }
    public LiveData<List<Booking>> getBookingsForEquipment(int equipmentId) {
        return mBookingDao.getBookingsForEquipment(equipmentId);
    }
    public List<Booking> getBookingsForEquipmentSync(int equipmentId) {
        return mBookingDao.getBookingsForEquipmentSync(equipmentId);
    }
    public void insert(Booking booking) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mBookingDao.insert(booking);
        });
    }
}