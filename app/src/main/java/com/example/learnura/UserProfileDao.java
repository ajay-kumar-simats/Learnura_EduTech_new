package com.example.learnura;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserProfileDao {
    @Insert
    void insert(UserProfile userProfile);

    @Query("SELECT * FROM UserProfile WHERE id = (SELECT MAX(id) FROM UserProfile)")
    UserProfile getLatestProfile();
}

