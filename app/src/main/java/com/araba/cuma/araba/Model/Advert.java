
package com.araba.cuma.araba.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Advert {

    private String status;
    private String imageUrl;
    private String advertId;
    private String userId;
    private String nameSurname;
    private String fromCity;
    private String toCity;
    private String material;
    private String travelerPerson;
    private String carModel;
    private String driverPerson;
    private String plate;
    private String date;
    private String time;
    private String description;
    private String price;
    public Advert() {

    }

    public Advert(String status, String advertId, String userId, String nameSurname, String fromCity,
                  String toCity, String material, String travelerPerson, String carModel, String driverPerson,
                  String plate, String date, String time, String description, String price,String imageUrl) {
        this.status = status;
        this.advertId = advertId;
        this.userId = userId;
        this.nameSurname = nameSurname;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.material = material;
        this.travelerPerson = travelerPerson;
        this.carModel = carModel;
        this.driverPerson = driverPerson;
        this.plate = plate;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.imageUrl=imageUrl;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTravelerPerson() {
        return travelerPerson;
    }

    public void setTravelerPerson(String travelerPerson) {
        this.travelerPerson = travelerPerson;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getDriverPerson() {
        return driverPerson;
    }

    public void setDriverPerson(String driverPerson) {
        this.driverPerson = driverPerson;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
