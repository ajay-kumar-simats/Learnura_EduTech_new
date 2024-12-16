package com.example.learnura;

public class ChatMessage {
    private int id; // Add this field
    private String message;
    private boolean isSentByUser;
    private String timestamp;

    // Constructor with ID
    public ChatMessage(int id, String message, boolean isSentByUser, String timestamp) {
        this.id = id;
        this.message = message;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
    }

    // Constructor without ID (for new messages before saving to database)
    public ChatMessage(String message, boolean isSentByUser, String timestamp) {
        this.message = message;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
