package com.example.splashactivity.models;

public class BikeModel {
    private String id;
    private String brand;
    private String model;
    private String price;
    private String image;
    private String description; // ⭐ NEW FIELD

    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public String getDescription() { return description; } // ⭐ NEW GETTER
}
