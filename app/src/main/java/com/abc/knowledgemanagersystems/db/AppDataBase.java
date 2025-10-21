package com.abc.knowledgemanagersystems.db;

import androidx.room.Database;
import androidx.room.TypeConverters;

import com.abc.knowledgemanagersystems.dao.EquipmentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentDao;
import com.abc.knowledgemanagersystems.dao.ExperimentLogsDao;
import com.abc.knowledgemanagersystems.dao.InventoryBookingDao;
import com.abc.knowledgemanagersystems.dao.InventoryItemDao;
import com.abc.knowledgemanagersystems.dao.InventoryLogDao;
import com.abc.knowledgemanagersystems.dao.SopsDao;
import com.abc.knowledgemanagersystems.dao.UserDao;
import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.model.InvetoryBooking;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Users;

@Database(entities = {
        Equipment.class,
        Experiment.class,
        ExperimentLogs.class,
        InventoryItem.class,
        InventoryLogs.class,
        InvetoryBooking.class,
        Sops.class,
        Users.class
    }, version = 1
)
@TypeConverters()
public abstract class AppDataBase extends DoomDatabase {
//    public abs
    public abstract EquipmentDao equipmentDao();
    public abstract ExperimentDao experimentDao();
    public abstract ExperimentLogsDao experimentLogsDao();
    public abstract InventoryLogDao inventoryLogDao();
    public abstract InventoryItemDao inventoryItemDao();
    public abstract InventoryBookingDao inventoryBookingDao();
    public abstract SopsDao sopsDao();
    public abstract UserDao userDao();
}
