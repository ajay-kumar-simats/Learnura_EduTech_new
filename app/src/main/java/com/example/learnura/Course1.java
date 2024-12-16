package com.example.learnura;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course1 {
    @SerializedName("course_id")
    private int courseId;
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("file_path")
    private String filePath;
    @SerializedName("uploaded_by")
    private String uploadedBy;
    @SerializedName("uploaded_at")
    private String uploadedAt;
    @SerializedName("category")
    private String category;
    private List<Challenge> challenges; // List to store challenges for this course

    // Getters and Setters for all fields
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}
