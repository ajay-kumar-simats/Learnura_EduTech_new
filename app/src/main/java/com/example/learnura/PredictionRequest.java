package com.example.learnura;

public class PredictionRequest {
    private int correctAnswers;
    private int timeSpent;
    private String currentDifficulty;

    // Constructor
    public PredictionRequest(int correctAnswers, int timeSpent, String currentDifficulty) {
        this.correctAnswers = correctAnswers;
        this.timeSpent = timeSpent;
        this.currentDifficulty = currentDifficulty;
    }

    // Getters and setters
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(String currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }
}
