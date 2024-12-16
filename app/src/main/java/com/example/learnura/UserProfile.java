package com.example.learnura;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int imageResId;

    public UserProfile(int imageResId) {
        this.imageResId = imageResId;
    }
}

