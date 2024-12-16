package com.example.learnura;

public class CourseItem {
    private String courseId;
    private String courseName;
    private String filePath;

    public CourseItem(String courseId, String courseName, String filePath) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.filePath = filePath;
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
}
