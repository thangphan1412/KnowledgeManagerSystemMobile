package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.model.Step;

import java.util.List;

public class SopsAndStep {
    @Embedded
    public Sops sops;
    @Relation(
            parentColumn = "sop_id",
            entityColumn = "sops_id"
    )
    public List<Step> steps;
}
