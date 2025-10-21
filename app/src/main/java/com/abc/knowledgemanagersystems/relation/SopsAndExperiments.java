package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.model.Sops;

import java.util.List;

public class SopsAndExperiments {
    @Embedded
    public Sops sops;
    @Relation(
            parentColumn = "sop_id",
            entityColumn = "sops_id"
    )
    public List<Experiment> experiments;
}
