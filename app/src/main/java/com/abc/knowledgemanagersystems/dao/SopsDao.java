package com.abc.knowledgemanagersystems.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.abc.knowledgemanagersystems.model.Sops;
import java.util.List;

@Dao
public interface SopsDao {

    // SỬA HÀM NÀY (từ void thành long)
    @Insert
    long insert(Sops sops); // <-- Phải trả về 'long'

    // --- Các hàm khác của bạn ---
    @Query("SELECT * FROM Sops ORDER BY sopsName ASC")
    List<Sops> getAllSops();
    @Query("SELECT * FROM sops WHERE sopsName LIKE '%' || :keyword || '%' OR title LIKE '%' || :keyword || '%' ORDER BY sopsName ASC")
    List<Sops> searchSops(String keyword);
    @Query("DELETE FROM sops WHERE sop_id = :id")
    void deleteSop(int id);
    @Query("SELECT * FROM sops WHERE sopsName LIKE :query OR title LIKE :query")
    List<Sops> searchSDS(String query);


}