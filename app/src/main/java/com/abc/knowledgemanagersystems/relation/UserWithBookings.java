package com.abc.knowledgemanagersystems.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.model.Users;

import java.util.List;

public class UserWithBookings {
    @Embedded
    public Users user;

    @Relation(
            parentColumn = "user_id", // Khóa chính của Users
            entityColumn = "users_id"  // Khóa ngoại trong Booking
    )
    public List<Booking> bookings;
}
