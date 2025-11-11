package com.abc.knowledgemanagersystems.service;

import android.content.Context;

import com.abc.knowledgemanagersystems.dao.ExperimentLogsDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;

import java.util.List;

public class LogbookService {
    private final ExperimentLogsDao logEntryDao;

    public LogbookService(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        this.logEntryDao = db.experimentLogsDao();
    }

    /**
     * Lưu mục nhập nhật ký vào Room DB. Cần gọi trên luồng nền.
     */
    public void insertLogEntry(ExperimentLogs entry) {
        logEntryDao.insert(entry);
    }

    /**
     * Lấy các mục nhập nhật ký theo ID thí nghiệm. Cần gọi trên luồng nền.
     */
    public List<ExperimentLogs> getLogEntriesByExperimentId(int experimentId) {
        return logEntryDao.getLogEntriesByExperimentId(experimentId);
    }
}
