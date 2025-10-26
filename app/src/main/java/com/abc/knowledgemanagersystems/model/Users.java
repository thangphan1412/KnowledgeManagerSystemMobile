package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.abc.knowledgemanagersystems.status.RoleName;

import org.jetbrains.annotations.NotNull;

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
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name="user_id")
    private int id;
    private String email;
    private String password;
    private String address;
    private String numberphone;
    private String username;
    private RoleName roleName;


}
