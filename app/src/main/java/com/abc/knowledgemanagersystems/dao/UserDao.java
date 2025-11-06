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

//     Lấy tất cả để kiểm tra
//    @Query("SELECT * FROM users ORDER BY id DESC")
//    List<Users> getAll();
// Thêm 1 user (trả về id đã insert)
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    int insert(Users user);
}
