package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.abc.knowledgemanagersystems.model.Sops;

import java.util.List;

@Dao
public interface SopsDao {
    @Insert
    void insert(Sops sops);
    @Query("SELECT * FROM Sops ORDER BY sopsName ASC")
    List<Sops> getAllSops();

    @Query("SELECT * FROM sops WHERE sopsName LIKE '%' || :keyword || '%' OR title LIKE '%' || :keyword || '%' ORDER BY sopsName ASC")
    List<Sops> searchSops(String keyword);

    @Query("DELETE FROM sops WHERE sop_id = :id")
    void deleteSop(int id);
}
