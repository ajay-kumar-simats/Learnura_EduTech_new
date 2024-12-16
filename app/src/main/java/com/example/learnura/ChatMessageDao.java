package com.example.learnura;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Insert
    long insert(ChatMessageEntity chatMessageEntity);

    @Query("SELECT * FROM chat_messages")
    List<ChatMessageEntity> getAllChatMessages();

    @Query("SELECT * FROM chat_messages WHERE message = :message AND timestamp = :timestamp LIMIT 1")
    ChatMessageEntity findByMessage(String message, String timestamp);

    @Delete
    void delete(ChatMessageEntity chatMessageEntity);
}
