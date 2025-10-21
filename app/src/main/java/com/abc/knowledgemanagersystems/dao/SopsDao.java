package com.abc.knowledgemanagersystems.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Sops;
@Dao
public interface SopsDao {
    @Insert
    void insert(Sops sops);
}
