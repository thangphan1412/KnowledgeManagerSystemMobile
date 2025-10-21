package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Equipment;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.InventoryItem;

import java.util.List;

public class ExperimentAndExperimentLog {
    @Embedded
    public Experiment experiment;
    @Relation(
            parentColumn = "experiment_id",
            entityColumn = "experiments_id"
    )
    public List<ExperimentLogs> experimentLogs;
}
