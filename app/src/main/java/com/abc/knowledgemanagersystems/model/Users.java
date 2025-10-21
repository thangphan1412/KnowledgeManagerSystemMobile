package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName= "users")
public class Users {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name="user_id")
    private UUID id;
    private String email;
    private String password;
    private String address;
    private String numberphone;
    private String username;


}
