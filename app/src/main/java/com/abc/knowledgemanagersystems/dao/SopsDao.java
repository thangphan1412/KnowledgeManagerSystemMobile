package com.abc.knowledgemanagersystems.dao;

import androidx.room.Insert;

import com.abc.knowledgemanagersystems.model.Sops;

public interface SopsDao {
    @Insert
    void insert(Sops sops);
}
