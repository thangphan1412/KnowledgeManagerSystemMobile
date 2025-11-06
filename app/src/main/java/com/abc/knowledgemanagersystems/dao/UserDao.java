package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

@Dao
public interface UserDao {
//    @Insert
//    void insert(Users users);

    @Query("SELECT * FROM users LIMIT 1")
    Users getLoggedInUser();

    @Query("DELETE FROM users")
    void logoutUser();

    @Query("SELECT * FROM users")
    List<Users> getAllUsers();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Users> users);
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    Users authenticateUser(String email, String password);

//     Lấy tất cả để kiểm tra
//    @Query("SELECT * FROM users ORDER BY id DESC")
//    List<Users> getAll();
// Thêm 1 user (trả về id đã insert)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Users user);
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);
}
