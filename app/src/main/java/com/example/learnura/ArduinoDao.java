package com.example.learnura;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ArduinoDao {
    @Insert
    void insert(ArduinoEntity arduino);

    @Query("SELECT * FROM arduino_courses WHERE isFavorite = 1")
    LiveData<List<ArduinoEntity>> getFavoriteCourses();

    @Query("SELECT * FROM arduino_courses")
    LiveData<List<ArduinoEntity>> getAllCourses();

    @Update
    void update(ArduinoEntity arduino);
}