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
import com.abc.knowledgemanagersystems.model.MaintenanceLog;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Step;
import com.abc.knowledgemanagersystems.model.Users;
import com.abc.knowledgemanagersystems.status.RoleName;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 🔥 THAY ĐỔI: Tăng version lên 10
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
}, version = 12, exportSchema = false // <-- TĂNG VERSION
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
                    // 🔥 THAY ĐỔI: Đổi tên DB thành v10
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "lab_management_db_v12") // <-- ĐỔI TÊN DB
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration() // Quan trọng: Cho phép xóa DB cũ khi nâng cấp
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

                // Dùng 2 link dummy ổn định
                String manualLink_Dummy1 = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
                String manualLink_Dummy2 = "https://www.orimi.com/pdf-test.pdf";

                // === 1. TẠO 3 USERS (ADMIN, TECH, RESEARCHER) ===

                // 1.1. ADMIN (CẤP 1)
                Users admin = new Users();
                admin.setEmail("admin@lab.com");
                admin.setPassword("admin123");
                admin.setUsername("Lab Manager");
                admin.setRoleName(RoleName.MANAGER);
                long adminId = userDao.insert(admin);
                int validUserId = (int) adminId;

                // 1.2. USER KỸ THUẬT VIÊN
                Users tech = new Users();
                tech.setEmail("tech@lab.com");
                tech.setPassword("tech123");
                tech.setUsername("Technician");
                tech.setRoleName(RoleName.TECHNICIAN);
                long techId = userDao.insert(tech);
                int validTechId = (int) techId;

                // 1.3. USER NHÀ NGHIÊN CỨU
                Users researcher = new Users();
                researcher.setEmail("researcher@lab.com");
                researcher.setPassword("researcher123");
                researcher.setUsername("Researcher");
                researcher.setRoleName(RoleName.RESEARCHER);
                long researcherId = userDao.insert(researcher);
                int validResearcherId = (int) researcherId;


                // === 2. TẠO CÁC BẢNG TRUNG GIAN (CẤP 2, 3, 4) ===

                // 2.1. TẠO EXPERIMENT (CẤP 2)
                Experiment dummyExperiment = new Experiment();
                dummyExperiment.setUserId(validUserId);
                dummyExperiment.setSopId(null);
                long experimentId = experimentDao.insert(dummyExperiment);
                int validExperimentId = (int) experimentId;

                // 2.2. TẠO EXPERIMENT LOGS (CẤP 3)
                ExperimentLogs dummyLog = new ExperimentLogs();
                dummyLog.setUser_owner_id(validUserId);
                dummyLog.setExperimentId(validExperimentId);
                long logId = experimentLogsDao.insert(dummyLog);
                int validLogId = (int) logId;

                // 2.3. TẠO SOPS (CẤP 4)
                Sops dummySop = new Sops();
                dummySop.setExperimentId(validLogId);
                long sopId = sopsDao.insert(dummySop);
                int validSopId = (int) sopId;

                // 2.4. TẠO INVENTORY ITEM (CẤP 5)
                InventoryItem dummyItem = new InventoryItem();
                dummyItem.setUserId(validUserId);
                dummyItem.setSopId(validSopId);
                long itemId = inventoryItemDao.insert(dummyItem);
                int validItemId = (int) itemId;


                // === 3. TẠO 12 EQUIPMENT (CẤP 6) ===

                // (Giả định EquipmentDao.insert trả về 'long')

                // 3.1. THIẾT BỊ CỦA ADMIN
                Equipment eq1 = new Equipment();
                eq1.setName("HPLC Machine #1");
                eq1.setModel("Agilent 1260");
                eq1.setUserId(validUserId);
                eq1.setInventoryItemId(validItemId);
                eq1.setManualUrl(manualLink_Dummy1);
                eq1.setQuantity(1.0);
                long eq1_id = equipmentDao.insert(eq1);

                Equipment eq2 = new Equipment();
                eq2.setName("Centrifuge");
                eq2.setModel("Eppendorf 5424 R");
                eq2.setUserId(validUserId);
                eq2.setInventoryItemId(validItemId);
                eq2.setManualUrl(manualLink_Dummy2);
                eq2.setQuantity(2.0);
                long eq2_id = equipmentDao.insert(eq2);

                Equipment eq3 = new Equipment();
                eq3.setName("PCR Machine");
                eq3.setModel("Bio-Rad T100");
                eq3.setUserId(validUserId);
                eq3.setInventoryItemId(validItemId);
                eq3.setManualUrl(manualLink_Dummy1);
                eq3.setQuantity(1.0);
                long eq3_id = equipmentDao.insert(eq3);

                Equipment eq4 = new Equipment();
                eq4.setName("Microscope");
                eq4.setModel("Olympus CX23");
                eq4.setUserId(validUserId);
                eq4.setInventoryItemId(validItemId);
                eq4.setManualUrl(manualLink_Dummy2);
                eq4.setQuantity(3.0);
                long eq4_id = equipmentDao.insert(eq4);

                Equipment eq5 = new Equipment();
                eq5.setName("Autoclave");
                eq5.setModel("Tuttnauer 2340M");
                eq5.setUserId(validUserId);
                eq5.setInventoryItemId(validItemId);
                eq5.setManualUrl(manualLink_Dummy1);
                eq5.setQuantity(1.0);
                long eq5_id = equipmentDao.insert(eq5);

                Equipment eq6 = new Equipment();
                eq6.setName("pH Meter");
                eq6.setModel("Mettler Toledo S220");
                eq6.setUserId(validUserId);
                eq6.setInventoryItemId(validItemId);
                eq6.setManualUrl(manualLink_Dummy2);
                eq6.setQuantity(5.0);
                long eq6_id = equipmentDao.insert(eq6);

                // 3.2. THIẾT BỊ CỦA TECHNICIAN
                Equipment eq7 = new Equipment();
                eq7.setName("Ultrasonic Cleaner");
                eq7.setModel("Branson 2800");
                eq7.setUserId(validTechId); // <-- Gán cho Tech
                eq7.setInventoryItemId(validItemId);
                eq7.setManualUrl(manualLink_Dummy1);
                eq7.setQuantity(1.0);
                long eq7_id = equipmentDao.insert(eq7);

                Equipment eq8 = new Equipment();
                eq8.setName("Water Bath");
                eq8.setModel("Polyscience WBE");
                eq8.setUserId(validTechId); // <-- Gán cho Tech
                eq8.setInventoryItemId(validItemId);
                eq8.setManualUrl(manualLink_Dummy2);
                eq8.setQuantity(2.0);
                long eq8_id = equipmentDao.insert(eq8);

                Equipment eq9 = new Equipment();
                eq9.setName("Analytical Balance");
                eq9.setModel("Ohaus AX224");
                eq9.setUserId(validTechId); // <-- Gán cho Tech
                eq9.setInventoryItemId(validItemId);
                eq9.setManualUrl(manualLink_Dummy1);
                eq9.setQuantity(2.0);
                long eq9_id = equipmentDao.insert(eq9);

                // 3.3. THIẾT BỊ CỦA RESEARCHER
                Equipment eq10 = new Equipment();
                eq10.setName("Fume Hood");
                eq10.setModel("Labconco Protector");
                eq10.setUserId(validResearcherId); // <-- Gán cho Researcher
                eq10.setInventoryItemId(validItemId);
                eq10.setManualUrl(manualLink_Dummy2);
                eq10.setQuantity(4.0);
                long eq10_id = equipmentDao.insert(eq10);

                Equipment eq11 = new Equipment();
                eq11.setName("Gel Electrophoresis");
                eq11.setModel("Bio-Rad PowerPac");
                eq11.setUserId(validResearcherId); // <-- Gán cho Researcher
                eq11.setInventoryItemId(validItemId);
                eq11.setManualUrl(manualLink_Dummy1);
                eq11.setQuantity(3.0);
                long eq11_id = equipmentDao.insert(eq11);

                Equipment eq12 = new Equipment();
                eq12.setName("Vortex Mixer");
                eq12.setModel("Fisher Scientific");
                eq12.setUserId(validResearcherId); // <-- Gán cho Researcher
                eq12.setInventoryItemId(validItemId);
                eq12.setManualUrl(manualLink_Dummy2);
                eq12.setQuantity(5.0);
                long eq12_id = equipmentDao.insert(eq12);


                // === 4. TẠO LOG BẢO TRÌ MẪU ===
                long now = new Date().getTime();
                long oneDay = 86400000L;

                MaintenanceLog log1 = new MaintenanceLog();
                log1.setEquipmentId((int) eq1_id); // Log cho máy HPLC
                log1.setDate(now);
                log1.setDescription("Hiệu chuẩn hàng năm.");
                log1.setTechnicianName(admin.getUsername());
                logDao.insert(log1);

                MaintenanceLog log2 = new MaintenanceLog();
                log2.setEquipmentId((int) eq1_id); // Log cho máy HPLC
                log2.setDate(now - oneDay); // (Hôm qua)
                log2.setDescription("Thay thế cột lọc.");
                log2.setTechnicianName(tech.getUsername());
                logDao.insert(log2);

                MaintenanceLog log3 = new MaintenanceLog();
                log3.setEquipmentId((int) eq3_id); // Log cho máy PCR
                log3.setDate(now - (oneDay * 2)); // (Hôm kia)
                log3.setDescription("Kiểm tra khối nhiệt.");
                log3.setTechnicianName(admin.getUsername());
                logDao.insert(log3);

                MaintenanceLog log4 = new MaintenanceLog();
                log4.setEquipmentId((int) eq4_id); // Log cho Kính hiển vi
                log4.setDate(now - (oneDay * 3)); // (3 ngày trước)
                log4.setDescription("Lau sạch vật kính.");
                log4.setTechnicianName(tech.getUsername());
                logDao.insert(log4);

                MaintenanceLog log5 = new MaintenanceLog();
                log5.setEquipmentId((int) eq2_id); // Log cho Centrifuge
                log5.setDate(now - (oneDay * 4)); // (4 ngày trước)
                log5.setDescription("Kiểm tra Roto và bôi trơn.");
                log5.setTechnicianName(tech.getUsername());
                logDao.insert(log5);

                MaintenanceLog log6 = new MaintenanceLog();
                log6.setEquipmentId((int) eq7_id); // Log cho Ultrasonic Cleaner
                log6.setDate(now - (oneDay * 5)); // (5 ngày trước)
                log6.setDescription("Thay dung dịch làm sạch.");
                log6.setTechnicianName(tech.getUsername());
                logDao.insert(log6);

                MaintenanceLog log7 = new MaintenanceLog();
                log7.setEquipmentId((int) eq10_id); // Log cho Fume Hood
                log7.setDate(now - (oneDay * 6)); // (6 ngày trước)
                log7.setDescription("Kiểm tra luồng khí và bộ lọc.");
                log7.setTechnicianName(admin.getUsername());
                logDao.insert(log7);

                // 🔥 THÊM 4 LOG BẢO TRÌ MỚI
                MaintenanceLog log8 = new MaintenanceLog();
                log8.setEquipmentId((int) eq8_id); // Log cho Water Bath
                log8.setDate(now - (oneDay * 7)); // (7 ngày trước)
                log8.setDescription("Kiểm tra nhiệt độ.");
                log8.setTechnicianName(tech.getUsername());
                logDao.insert(log8);

                MaintenanceLog log9 = new MaintenanceLog();
                log9.setEquipmentId((int) eq9_id); // Log cho Analytical Balance
                log9.setDate(now - (oneDay * 8)); // (8 ngày trước)
                log9.setDescription("Hiệu chuẩn quả cân.");
                log9.setTechnicianName(admin.getUsername());
                logDao.insert(log9);

                MaintenanceLog log10 = new MaintenanceLog();
                log10.setEquipmentId((int) eq11_id); // Log cho Gel Electrophoresis
                log10.setDate(now - (oneDay * 9)); // (9 ngày trước)
                log10.setDescription("Kiểm tra nguồn điện.");
                log10.setTechnicianName(researcher.getUsername()); // Researcher tự log
                logDao.insert(log10);

                MaintenanceLog log11 = new MaintenanceLog();
                log11.setEquipmentId((int) eq12_id); // Log cho Vortex Mixer
                log11.setDate(now - (oneDay * 10)); // (10 ngày trước)
                log11.setDescription("Kiểm tra độ rung.");
                log11.setTechnicianName(researcher.getUsername()); // Researcher tự log
                logDao.insert(log11);

            });
        }
    };
}