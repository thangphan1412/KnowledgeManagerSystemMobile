package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

public class UserAndExperiemtLogs {
    @Embedded
    public Users users;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "experiment_log_id"
    )
    public List<ExperimentLogs> experimentLogs;
}
