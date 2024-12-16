package com.example.learnura;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_messages")
public class ChatMessageEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String message;
    public boolean isSentByUser;
    public String timestamp;

    public ChatMessageEntity(String message, boolean isSentByUser, String timestamp) {
        this.message = message;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
    }
}


