package com.abc.knowledgemanagersystems.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.abc.knowledgemanagersystems.model.Users;
import java.util.List;

@Dao
public interface UserDao {

    // SỬA HÀM NÀY (từ void thành long)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Users user); // <-- Phải trả về 'long'

    // --- Các hàm khác của bạn ---
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
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);
}