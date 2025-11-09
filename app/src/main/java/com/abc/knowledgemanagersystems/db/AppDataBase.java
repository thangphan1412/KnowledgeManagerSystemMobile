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
import com.abc.knowledgemanagersystems.dao.BookingDao;
import com.abc.knowledgemanagersystems.dao.InventoryItemDao;
import com.abc.knowledgemanagersystems.dao.InventoryLogDao;
// 🔥 THÊM DAO MỚI
import com.abc.knowledgemanagersystems.dao.MaintenanceLogDao;
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
// 🔥 THÊM MODEL MỚI
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Step;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.status.RoleName;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 🔥 THAY ĐỔI: Tăng version lên 6
@Database(entities = {
        Equipment.class,
        Experiment.class,
        ExperimentLogs.class,
        InventoryItem.class,
        InventoryLogs.class,
        Booking.class,
        Sops.class,
        Step.class,
        Users.class,
        MaintenanceLog.class
}, version = 6, exportSchema = false // <-- TĂNG VERSION
)
@TypeConverters({DataConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    // Khai báo tất cả DAO
    public abstract EquipmentDao equipmentDao();
    public abstract ExperimentDao experimentDao();
    public abstract ExperimentLogsDao experimentLogsDao();
    public abstract InventoryLogDao inventoryLogDao();
    public abstract InventoryItemDao inventoryItemDao();
    public abstract BookingDao bookingDao();
    public abstract SopsDao sopsDao();
    public abstract StepDao stepDao();
    public abstract UserDao userDao();
    public abstract MaintenanceLogDao maintenanceLogDao();

    private static volatile AppDataBase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    // 🔥 THAY ĐỔI: Đổi tên DB thành v6
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "lab_management_db_v6") // <-- ĐỔI TÊN DB
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

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
                MaintenanceLogDao logDao = INSTANCE.maintenanceLogDao();

                // 🔥 THAY ĐỔI: Dùng 2 link dummy ổn định
                String manualLink_Dummy1 = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
                String manualLink_Dummy2 = "https://www.orimi.com/pdf-test.pdf";

                // 1. TẠO ADMIN (CẤP 1)
                Users admin = new Users();
                admin.setEmail("admin@lab.com");
                admin.setPassword("admin123");
                admin.setUsername("Quản Lý Hệ Thống");
                admin.setRoleName(RoleName.MANAGER);
                long adminId = userDao.insert(admin);
                int validUserId = (int) adminId;

                // 2. TẠO EXPERIMENT (CẤP 1)
                Experiment dummyExperiment = new Experiment();
                dummyExperiment.setUserId(validUserId);
                dummyExperiment.setSopId(null);
                long experimentId = experimentDao.insert(dummyExperiment);
                int validExperimentId = (int) experimentId;

                // 3. TẠO EXPERIMENT LOGS (CẤP 2)
                ExperimentLogs dummyLog = new ExperimentLogs();
                dummyLog.setUser_owner_id(validUserId);
                dummyLog.setExperimentId(validExperimentId);
                long logId = experimentLogsDao.insert(dummyLog);
                int validLogId = (int) logId;

                // 4. TẠO SOPS (CẤP 3)
                Sops dummySop = new Sops();
                dummySop.setExperimentId(validLogId);
                long sopId = sopsDao.insert(dummySop);
                int validSopId = (int) sopId;

                // 5. TẠO INVENTORY ITEM (CẤP 4)
                InventoryItem dummyItem = new InventoryItem();
                dummyItem.setUserId(validUserId);
                dummyItem.setSopId(validSopId);
                long itemId = inventoryItemDao.insert(dummyItem);
                int validItemId = (int) itemId;

                // 6. TẠO 3 EQUIPMENT CŨ (CẤP 5)

                // (Giả định EquipmentDao.insert trả về 'long')
                Equipment eq1 = new Equipment();
                eq1.setName("HPLC Machine #1");
                eq1.setModel("Agilent 1260");
                eq1.setUserId(validUserId);
                eq1.setInventoryItemId(validItemId);
                eq1.setManualUrl(manualLink_Dummy1); // <-- Dùng link test dummy 1
                long eq1_id = equipmentDao.insert(eq1);

                Equipment eq2 = new Equipment();
                eq2.setName("Centrifuge");
                eq2.setModel("Eppendorf 5424 R");
                eq2.setUserId(validUserId);
                eq2.setInventoryItemId(validItemId);
                eq2.setManualUrl(manualLink_Dummy2); // <-- Dùng link test dummy 2
                long eq2_id = equipmentDao.insert(eq2);

                Equipment eq3 = new Equipment();
                eq3.setName("PCR Machine");
                eq3.setModel("Bio-Rad T100");
                eq3.setUserId(validUserId);
                eq3.setInventoryItemId(validItemId);
                eq3.setManualUrl(manualLink_Dummy1); // <-- Dùng link test dummy 1
                long eq3_id = equipmentDao.insert(eq3);

                // 7. 🔥 THÊM VÀO: TẠO 3 EQUIPMENT MỚI
                Equipment eq4 = new Equipment();
                eq4.setName("Microscope");
                eq4.setModel("Olympus CX23");
                eq4.setUserId(validUserId);
                eq4.setInventoryItemId(validItemId);
                eq4.setManualUrl(manualLink_Dummy2); // <-- Dùng link test dummy 2
                long eq4_id = equipmentDao.insert(eq4);

                Equipment eq5 = new Equipment();
                eq5.setName("Autoclave");
                eq5.setModel("Tuttnauer 2340M");
                eq5.setUserId(validUserId);
                eq5.setInventoryItemId(validItemId);
                eq5.setManualUrl(manualLink_Dummy1); // <-- Dùng link test dummy 1
                long eq5_id = equipmentDao.insert(eq5);

                Equipment eq6 = new Equipment();
                eq6.setName("pH Meter");
                eq6.setModel("Mettler Toledo S220");
                eq6.setUserId(validUserId);
                eq6.setInventoryItemId(validItemId);
                eq6.setManualUrl(manualLink_Dummy2); // <-- Dùng link test dummy 2
                long eq6_id = equipmentDao.insert(eq6);


                // 8. 🔥 CẬP NHẬT: TẠO LOG BẢO TRÌ MẪU
                long now = new Date().getTime();

                MaintenanceLog log1 = new MaintenanceLog();
                log1.setEquipmentId((int) eq1_id); // Log cho máy HPLC
                log1.setDate(now);
                log1.setDescription("Hiệu chuẩn hàng năm.");
                log1.setTechnicianName("Kỹ thuật viên A");
                logDao.insert(log1);

                MaintenanceLog log2 = new MaintenanceLog();
                log2.setEquipmentId((int) eq1_id); // Log cho máy HPLC
                log2.setDate(now - 86400000L); // (Hôm qua)
                log2.setDescription("Thay thế cột lọc.");
                log2.setTechnicianName("Kỹ thuật viên B");
                logDao.insert(log2);

                MaintenanceLog log3 = new MaintenanceLog();
                log3.setEquipmentId((int) eq3_id); // Log cho máy PCR
                log3.setDate(now - 172800000L); // (Hôm kia)
                log3.setDescription("Kiểm tra khối nhiệt.");
                log3.setTechnicianName("Kỹ thuật viên A");
                logDao.insert(log3);

                // 🔥 THÊM LOG MỚI
                MaintenanceLog log4 = new MaintenanceLog();
                log4.setEquipmentId((int) eq4_id); // Log cho Kính hiển vi
                log4.setDate(now - 259200000L); // (3 ngày trước)
                log4.setDescription("Lau sạch vật kính.");
                log4.setTechnicianName("Kỹ thuật viên B");
                logDao.insert(log4);
            });
        }
    };
}