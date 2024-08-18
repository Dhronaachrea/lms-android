package com.skilrock.bean;

/**
 * Created by vishalp on 3/16/2015.
 */
public class ScratchBean {
    private double height;
    private double width;
    private double xPos;
    private double yPos;

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }


    public ScratchBean(double height, double width, double xPos, double yPos) {
        this.height = height;
        this.width = width;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
