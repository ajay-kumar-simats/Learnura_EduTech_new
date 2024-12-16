package com.example.learnura;

public class AppUpdate {

    private String appName;
    private String version;
    private String imageUrl;

    // Constructor
    public AppUpdate(String appName, String version, String imageUrl) {
        this.appName = appName;
        this.version = version;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getAppName() {
        return appName;
    }

    public String getVersion() {
        return version;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods (optional, if required)
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
