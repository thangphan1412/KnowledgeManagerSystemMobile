package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Users;

@Dao
public interface UserDao {
    @Insert
    void insert(Users users);
}
