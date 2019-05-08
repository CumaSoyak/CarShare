package com.araba.cuma.araba.Model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("imageResource")
    private int imageResource;
    @SerializedName("city")
    private String city;
    @SerializedName("area")
    private String area;

    public Location(){

    }
    public Location(int imageResource, String city, String area) {
        this.imageResource = imageResource;
        this.city = city;
        this.area = area;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
