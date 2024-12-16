package com.example.learnura;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "arduino_courses")
public class ArduinoEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mainTitle;
    private String subTitle;
    private int image;
    private boolean isFavorite;

    // Constructor
    public ArduinoEntity(int image, String mainTitle, String subTitle, boolean isFavorite) {
        this.image = image;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.isFavorite = isFavorite;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMainTitle() { return mainTitle; }
    public void setMainTitle(String mainTitle) { this.mainTitle = mainTitle; }
    public String getSubTitle() { return subTitle; }
    public void setSubTitle(String subTitle) { this.subTitle = subTitle; }
    public int getImage() { return image; }
    public void setImage(int image) { this.image = image; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
