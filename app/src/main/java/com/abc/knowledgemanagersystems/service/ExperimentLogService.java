package com.abc.knowledgemanagersystems.service;

import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.repository.ExperimentRepository;

import java.util.List;

public class ExperimentLogService {
    private final ExperimentRepository repository;

    public ExperimentLogService(ExperimentRepository repository) {
        this.repository = repository;
    }


    public List<Sops> getAvailableSops() {

        return repository.getAllSops();
    }


    public long createAndSyncNewExperiment(Experiment experiment) {

        long localId = repository.insertExperimentLocal(experiment);


        try {
            String serverId = "EXP-SVR-" + localId;
            repository.updateSyncStatus((int)localId, serverId);

        } catch (Exception e) {

            System.err.println("Lỗi đồng bộ server cho ID cục bộ " + localId + ": " + e.getMessage());
        }


        return localId;
    }

}
