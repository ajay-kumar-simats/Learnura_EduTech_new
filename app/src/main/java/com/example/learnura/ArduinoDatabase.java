package com.example.learnura;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArduinoEntity.class}, version = 1)
public abstract class ArduinoDatabase extends RoomDatabase {
    private static ArduinoDatabase instance;

    public abstract ArduinoDao arduinoDao();

    public static synchronized ArduinoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ArduinoDatabase.class, "arduino_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}