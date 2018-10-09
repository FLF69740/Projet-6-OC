package com.example.francoislf.go4lunch.models;

import android.support.annotation.Nullable;

import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;

public class User {

    private String mUid;
    private String mUsername;
    private String mRestaurantChoice;
    private String mDateChoice;
    private String mHourChoice;
    private String mAdressRestaurant;
    @Nullable private String mUrlPicture;

    public User(String uid, String username, @Nullable String urlPicture){
        this.mUid = uid;
        this.mUsername = username;
        this.mUrlPicture = urlPicture;
        this.mRestaurantChoice = BLANK_ANSWER;
        this.mDateChoice = BLANK_ANSWER;
        this.mHourChoice = BLANK_ANSWER;
        this.mAdressRestaurant = BLANK_ANSWER;
    }

    public User(){}

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getRestaurantChoice() {
        return mRestaurantChoice;
    }

    public void setRestaurantChoice(String restaurantChoice) {
        mRestaurantChoice = restaurantChoice;
    }

    public String getDateChoice() {
        return mDateChoice;
    }

    public void setDateChoice(String dateChoice) {
        mDateChoice = dateChoice;
    }

    public String getHourChoice() {
        return mHourChoice;
    }

    public void setHourChoice(String hourChoice) {
        mHourChoice = hourChoice;
    }

    @Nullable
    public String getUrlPicture() {
        return mUrlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        mUrlPicture = urlPicture;
    }

    public String getAdressRestaurant() {
        return mAdressRestaurant;
    }

    public void setAdressRestaurant(String adressRestaurant) {
        mAdressRestaurant = adressRestaurant;
    }
}
