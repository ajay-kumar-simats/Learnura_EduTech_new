package com.example.learnura;

public class LearningData {
    private String difficultyLevel;
    private boolean isCorrect;
    private long timeSpent;
    private String dateTime; // To store the date and time of the quiz.

    public LearningData(String difficultyLevel, boolean isCorrect, long timeSpent, String dateTime) {
        this.difficultyLevel = difficultyLevel;
        this.isCorrect = isCorrect;
        this.timeSpent = timeSpent;
        this.dateTime = dateTime;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public String getDateTime() {
        return dateTime;
    }
}
