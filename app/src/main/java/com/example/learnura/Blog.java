package com.example.learnura;

public class Blog {
    private String title;
    private String detail;
    private String link;
    private String description;

    public Blog(String title, String detail, String link, String description) {
        this.title = title;
        this.detail = detail;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }
}
