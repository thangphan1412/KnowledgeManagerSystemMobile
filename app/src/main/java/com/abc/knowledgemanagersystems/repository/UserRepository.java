//package com.abc.knowledgemanagersystems.repository;
//
//import android.app.Application;
//
//import com.abc.knowledgemanagersystems.dao.UserDao;
//import com.abc.knowledgemanagersystems.db.AppDataBase;
//import com.abc.knowledgemanagersystems.model.Users;
//
//import java.util.List;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class UserRepository {
//    private final UserDao userDao;
//    private final Executor executor = Executors.newSingleThreadExecutor();
//
//    public UserRepository(Application application) {
//        AppDataBase db = AppDataBase.getInstance(application);
//        userDao = db.userDao();
//    }
//
//    // Insert asynchronous, trả về callback id
//    public void insert(Users user, InsertCallback callback) {
//        executor.execute(() -> {
//            int id = userDao.insert(user);
//            if (callback != null) callback.onInserted(id);
//        });
//    }
//
//    // Lấy danh sách (synchronous trong executor)
//    public void getAll(GetAllCallback callback) {
//        executor.execute(() -> {
//            List<Users> list = userDao.getAllUsers();
//            if (callback != null) callback.onResult(list);
//        });
//    }
//
//    // Callback interfaces
//    public interface InsertCallback {
//        void onInserted(long id);
//    }
//
//    public interface GetAllCallback {
//        void onResult(List<Users> users);
//    }
//}
