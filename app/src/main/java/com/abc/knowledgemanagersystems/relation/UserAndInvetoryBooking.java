package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

public class UserAndInvetoryBooking {
    @Embedded
    public Users users;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "users_id"
    )
    public List<Booking> invetoryBookings;
}
