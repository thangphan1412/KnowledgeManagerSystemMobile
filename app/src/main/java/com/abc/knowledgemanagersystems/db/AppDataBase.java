package com.abc.knowledgemanagersystems.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abc.knowledgemanagersystems.config.DataConverter;
import com.abc.knowledgemanagersystems.dao.EquipmentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentLogsDao;
import com.abc.knowledgemanagersystems.dao.InventoryBookingDao;
import com.abc.knowledgemanagersystems.dao.InventoryItemDao;
import com.abc.knowledgemanagersystems.dao.InventoryLogDao;
import com.abc.knowledgemanagersystems.dao.SopsDao;
import com.abc.knowledgemanagersystems.dao.UserDao;
import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.status.RoleName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Equipment.class,
        Experiment.class,
        ExperimentLogs.class,
        InventoryItem.class,
        InventoryLogs.class,

        Booking.class,
        Sops.class,
        Users.class
    }, version = 1
)
@TypeConverters({DataConverter.class})
public abstract class AppDataBase extends RoomDatabase {
//    public abs
    public abstract EquipmentDao equipmentDao();
    public abstract ExperimentDao experimentDao();
    public abstract ExperimentLogsDao experimentLogsDao();
    public abstract InventoryLogDao inventoryLogDao();
    public abstract InventoryItemDao inventoryItemDao();
    public abstract InventoryBookingDao inventoryBookingDao();
    public abstract SopsDao sopsDao();
    public abstract UserDao userDao();
    private static volatile AppDataBase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "lab_management_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    /**
     * CALLBACK để chèn dữ liệu ban đầu (Tài khoản Admin cứng).
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                UserDao dao = INSTANCE.userDao();

                // TẠO TÀI KHOẢN ADMIN FIX CỨNG
                Users admin = new Users();
                admin.setEmail("admin@lab.com");
                admin.setPassword("admin123");
                admin.setUsername("Quản Lý Hệ Thống");
                admin.setRoleName(RoleName.MANAGER);
                // Các trường khác như address, numberphone sẽ là null/mặc định

                dao.insert(admin);
            });
        }
    };
}
