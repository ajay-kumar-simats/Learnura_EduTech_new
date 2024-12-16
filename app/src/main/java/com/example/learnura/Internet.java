package com.example.learnura;

public class Internet {

    private int image;
    private String main_title;
    private String sub_title;


    public Internet(int image,String main_title,String sub_title){

        this.image = image;
        this.main_title = main_title;
        this.sub_title = sub_title;

    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}




