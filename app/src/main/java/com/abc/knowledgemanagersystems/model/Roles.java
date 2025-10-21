package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity(tableName = "roles")
public class Roles {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "role_id")
    private UUID id;
    private String roleName;

}
