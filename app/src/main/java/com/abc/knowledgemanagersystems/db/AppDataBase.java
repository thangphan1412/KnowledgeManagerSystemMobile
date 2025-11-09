package com.abc.knowledgemanagersystems.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abc.knowledgemanagersystems.config.DataConverter;
// Import tất cả DAO
import com.abc.knowledgemanagersystems.dao.EquipmentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentLogsDao;
import com.abc.knowledgemanagersystems.dao.InventoryBookingDao;
import com.abc.knowledgemanagersystems.dao.InventoryItemDao;
import com.abc.knowledgemanagersystems.dao.InventoryLogDao;
import com.abc.knowledgemanagersystems.dao.SopsDao;
import com.abc.knowledgemanagersystems.dao.StepDao;
import com.abc.knowledgemanagersystems.dao.UserDao;
// Import tất cả Model
import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Step;
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
        Step.class,
        Users.class
}, version = 1, exportSchema = false
)
@TypeConverters({DataConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    // Khai báo tất cả DAO
    public abstract EquipmentDao equipmentDao();
    public abstract ExperimentDao experimentDao();
    public abstract ExperimentLogsDao experimentLogsDao();
    public abstract InventoryLogDao inventoryLogDao();
    public abstract InventoryItemDao inventoryItemDao();
    public abstract InventoryBookingDao inventoryBookingDao();
    public abstract SopsDao sopsDao();
    public abstract StepDao stepDao();
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
     * CALLBACK ĐÃ SỬA (LOGIC 6 CẤP - ĐÃ HẾT LỖI VÒNG LẶP):
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Lấy tất cả DAO cần thiết
                UserDao userDao = INSTANCE.userDao();
                ExperimentDao experimentDao = INSTANCE.experimentDao();
                ExperimentLogsDao experimentLogsDao = INSTANCE.experimentLogsDao();
                SopsDao sopsDao = INSTANCE.sopsDao();
                InventoryItemDao inventoryItemDao = INSTANCE.inventoryItemDao();
                EquipmentDao equipmentDao = INSTANCE.equipmentDao();

                // 1. TẠO ADMIN (CẤP 1 - TỔ TIÊN)
                Users admin = new Users();
                admin.setEmail("admin@lab.com");
                admin.setPassword("admin123");
                admin.setUsername("Quản Lý Hệ Thống");
                admin.setRoleName(RoleName.MANAGER);
                long adminId = userDao.insert(admin);
                int validUserId = (int) adminId;

                // 2. TẠO EXPERIMENT (CẤP 1 - TỔ TIÊN)
                Experiment dummyExperiment = new Experiment();
                // TODO: Set các trường bắt buộc cho Experiment
                dummyExperiment.setUserId(validUserId);
                // (sopId là Integer (null), nên chúng ta KHÔNG set)
                long experimentId = experimentDao.insert(dummyExperiment);
                int validExperimentId = (int) experimentId;

                // 3. TẠO EXPERIMENT LOGS (CẤP 2 - CỤ)
                ExperimentLogs dummyLog = new ExperimentLogs();
                // TODO: Set các trường bắt buộc cho ExperimentLogs
                dummyLog.setUser_owner_id(validUserId);
                dummyLog.setExperimentId(validExperimentId);
                long logId = experimentLogsDao.insert(dummyLog);
                int validLogId = (int) logId;

                // 4. TẠO SOPS (CẤP 3 - ÔNG)
                Sops dummySop = new Sops();
                // TODO: Set các trường bắt buộc cho Sops
                dummySop.setExperimentId(validLogId);
                long sopId = sopsDao.insert(dummySop);
                int validSopId = (int) sopId;

                // 5. TẠO INVENTORY ITEM (CẤP 4 - CHA)
                InventoryItem dummyItem = new InventoryItem();
                // TODO: Set các trường bắt buộc khác cho InventoryItem
                dummyItem.setUserId(validUserId);
                dummyItem.setSopId(validSopId);
                long itemId = inventoryItemDao.insert(dummyItem);
                int validItemId = (int) itemId;

                // 6. TẠO 3 EQUIPMENT (CẤP 5 - CON)
                Equipment eq1 = new Equipment();
                eq1.setName("HPLC Machine #1");
                eq1.setModel("Agilent 1260");
                eq1.setUserId(validUserId);
                eq1.setInventoryItemId(validItemId);
                equipmentDao.insert(eq1);

                Equipment eq2 = new Equipment();
                eq2.setName("Centrifuge");
                eq2.setModel("Eppendorf 5424 R");
                eq2.setUserId(validUserId);
                eq2.setInventoryItemId(validItemId);
                equipmentDao.insert(eq2);

                Equipment eq3 = new Equipment();
                eq3.setName("PCR Machine");
                eq3.setModel("Bio-Rad T100");
                eq3.setUserId(validUserId);
                eq3.setInventoryItemId(validItemId);
                equipmentDao.insert(eq3);
            });
        }
    };
}