package com.abc.knowledgemanagersystems.service;

import android.content.Context;

import com.abc.knowledgemanagersystems.dao.ExperimentDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Experiment;

public class ExperimentService {
    private final ExperimentDao experimentDao;
    public ExperimentService(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        experimentDao = db.experimentDao();
    }

    /**
     * Lưu Experiment vào DB và trả về ID mới.
     * Cần được gọi từ luồng nền (ExecutorService).
     */
    public long insertExperiment(Experiment experiment) {
        return experimentDao.insert(experiment);
    }
}
