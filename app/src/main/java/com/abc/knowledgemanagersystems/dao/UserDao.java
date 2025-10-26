package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.abc.knowledgemanagersystems.model.Users;

@Dao
public interface UserDao {
    @Insert
    void insert(Users users);

    @Query("SELECT * FROM users LIMIT 1")
    Users getLoggedInUser();

    @Query("DELETE FROM users")
    void logoutUser();
}
