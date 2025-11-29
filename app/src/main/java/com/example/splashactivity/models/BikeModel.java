package com.example.splashactivity.models;

import java.io.Serializable;

public class BikeModel implements Serializable {

    private String id;
    private String brand;
    private String model;
    private String price;
    private String engine;
    private String description;
    private String img_top;
    private String img_front;
    private String img_left;
    private String img_right;

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getEngine() {
        return engine;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_top() {
        return img_top;
    }

    public String getImg_front() {
        return img_front;
    }

    public String getImg_left() {
        return img_left;
    }

    public String getImg_right() {
        return img_right;
    }
}
