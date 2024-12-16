package com.example.learnura;


import java.util.List;

public class CourseResponse {
    private String status;
    private Categories categories;

    public String getStatus() {
        return status;
    }

    public Categories getCategories() {
        return categories;
    }

    public static class Categories {
        private List<Course> Arduino;
        private List<Course> IoT;
        private List<Course> Programming;

        public List<Course> getArduino() {
            return Arduino;
        }

        public List<Course> getIoT() {
            return IoT;
        }

        public List<Course> getProgramming() {
            return Programming;
        }
    }

    public static class Course {
        private String title;

        public String getTitle() {
            return title;
        }
    }
}
