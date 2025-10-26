package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.abc.knowledgemanagersystems.status.StatusExperiment;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "experiment",
        foreignKeys = {
        @ForeignKey(
                        entity = Users.class,
                        parentColumns = "user_id",
                        childColumns = "users_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Sops.class,
                        parentColumns = "sop_id",
                        childColumns = "sops_id",
                        onDelete = ForeignKey.CASCADE

                )
        }

)
public class Experiment {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "experiment_id")
    private int id;
    private String title;
    private String startDate;
    private String endDate;
    private StatusExperiment statusExperiment;
    @ColumnInfo(name = "users_id")
    private int userId;
    @ColumnInfo(name = "sops_id")
    private int sopId;

    public String serverExperimentId;
    public boolean isSynced;

}
