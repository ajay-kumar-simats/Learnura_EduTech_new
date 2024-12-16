package com.example.learnura;

public class Teacher_Course {
    private String courseId;
    private String courseName;
    private String filePath;
    private int thumbnailResId;

    public Teacher_Course(String courseId, String courseName, String filePath, int thumbnailResId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.filePath = filePath;
        this.thumbnailResId = thumbnailResId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public String getTitle() {
        return courseName;
    }
}
