package com.abc.knowledgemanagersystems.service;

import android.content.Context;

import com.abc.knowledgemanagersystems.dao.UserDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Users;

public class UserService {
    private final UserDao userDao;

    public UserService(Context context) {
        // Khởi tạo Room Database và lấy UserDao
        AppDataBase db = AppDataBase.getInstance(context);
        userDao = db.userDao();
    }

    /**
     * Lấy thông tin người dùng từ Room theo Email.
     * Cần được gọi từ luồng nền (ExecutorService).
     */
    public Users getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    // Phương thức insert người dùng mới (dùng cho đăng ký hoặc Admin)
    public void insertUser(Users user) {
        userDao.insert(user);
    }
}
