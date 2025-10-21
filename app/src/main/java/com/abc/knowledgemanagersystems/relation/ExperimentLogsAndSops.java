package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Sops;

public class ExperimentLogsAndSops {
        @Embedded
        public ExperimentLogs experimentLogs;

        @Relation(
                parentColumn = "experiment_log_id",
                entityColumn = "experiments_log_id"
        )
        public Sops sops;


}
