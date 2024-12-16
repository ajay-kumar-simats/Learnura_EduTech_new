package com.example.learnura;

public class Avatar {
    private int imageResId;
    private String name;

    public Avatar(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }
}
