package com.abc.knowledgemanagersystems.config;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DataConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @TypeConverter
    public static UUID toUUID(String uuid) {
        return uuid == null ? null : UUID.fromString(uuid);
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formatter);
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        return dateTimeString == null ? null : LocalDateTime.parse(dateTimeString, formatter);
    }


}
