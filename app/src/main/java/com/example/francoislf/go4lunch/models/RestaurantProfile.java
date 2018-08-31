package com.example.francoislf.go4lunch.models;

import java.util.List;

public class RestaurantProfile {

    private String mPlaceId;
    private String mName;
    private String mPhoneNumber;
    private String mAdress;
    private String mWebSite;
    private List<String> mWeekHour;
    private double lat;
    private double lng;
    private String mPhoto;


    public RestaurantProfile(){}


    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String adress) {
        mAdress = adress;
    }

    public String getWebSite() {
        return mWebSite;
    }

    public void setWebSite(String webSite) {
        mWebSite = webSite;
    }

    public List<String> getWeekHour() {
        return mWeekHour;
    }

    public void setWeekHour(List<String> weekHour) {
        mWeekHour = weekHour;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}
