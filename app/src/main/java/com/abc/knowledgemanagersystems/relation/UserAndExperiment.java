package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

public class UserAndExperiment {
    @Embedded
    public Users users;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "experiment_id"
    )
    public List<Experiment> experiment;
}
