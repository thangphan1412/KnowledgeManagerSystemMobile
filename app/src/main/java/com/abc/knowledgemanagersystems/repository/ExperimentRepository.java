package com.abc.knowledgemanagersystems.repository;

import android.content.Context;

import com.abc.knowledgemanagersystems.dao.ExperimentDao;
import com.abc.knowledgemanagersystems.dao.SopsDao;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.Sops;

import java.util.List;

public class ExperimentRepository {
    private final ExperimentDao experimentDao;
    private final SopsDao sopsDao;

    public ExperimentRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        this.experimentDao = db.experimentDao();
        this.sopsDao = db.sopsDao();
    }
    public List<Sops> getAllSops() {

        return sopsDao.getAllSops();
    }


    public long createAndSyncExperiment(Experiment experiment) {

        long localId = experimentDao.insert(experiment);


        String serverId = "EXP-SVR-" + localId;

        experiment.serverExperimentId = serverId;
        experiment.isSynced = true;

        return localId;
    }
}
