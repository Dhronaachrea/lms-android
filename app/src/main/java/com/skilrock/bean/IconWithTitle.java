package com.skilrock.bean;

public class IconWithTitle {
    private String title;
    private int imageID;
    private int color;
    private String itemCode;

    public String getItemCode() {
        return itemCode;
    }

    public IconWithTitle(String title, int imageID) {
        this.title = title;
        this.imageID = imageID;
    }

    public IconWithTitle(String itemCode, String title, int imageID) {
        this.title = title;
        this.imageID = imageID;
        this.itemCode = itemCode;
    }

    public IconWithTitle(String title, int imageID, int color) {
        this.title = title;
        this.imageID = imageID;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getImageID() {
        return imageID;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
