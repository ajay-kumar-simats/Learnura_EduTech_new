package com.example.learnura;
public class PerformanceData {
    private int totalQuestions;
    private int correctAnswers;
    private long totalTimeSpent;

    public PerformanceData(int totalQuestions, int correctAnswers, long totalTimeSpent) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.totalTimeSpent = totalTimeSpent;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }
}
