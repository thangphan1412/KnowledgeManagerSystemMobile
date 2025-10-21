package com.abc.knowledgemanagersystems.db;

import android.app.Application;
import androidx.room.Room;

public class MyApplication extends Application {
    private static AppDataBase database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDataBase.class,
                        "manager_knowleage_system.db"
                )
                .fallbackToDestructiveMigration()
                .build();


        database.getOpenHelper().getWritableDatabase();
    }

    public static AppDataBase getDatabase() {
        return database;
    }
}
