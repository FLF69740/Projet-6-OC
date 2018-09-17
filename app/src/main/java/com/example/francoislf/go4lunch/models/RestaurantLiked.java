package com.example.francoislf.go4lunch.models;

public class RestaurantLiked {

    private String mRestaurantName;
    private String mPlaceId;
    private int mNumberOfLike;
    private int mParticipantsOfTheDay;
    private int mDateChoice;
    private int mHourChoice;

    public RestaurantLiked(String restaurantName, String placeId){
        this.mRestaurantName = restaurantName;
        this.mPlaceId = placeId;
        this.mParticipantsOfTheDay = 0;
        this.mNumberOfLike = 0;
        this.mDateChoice = 0;
        this.mHourChoice = 0;
    }

    public RestaurantLiked(){}

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public int getNumberOfLike() {
        return mNumberOfLike;
    }

    public void setNumberOfLike(int numberOfLike) {
        mNumberOfLike = numberOfLike;
    }

    public int getParticipantsOfTheDay() {
        return mParticipantsOfTheDay;
    }

    public void setParticipantsOfTheDay(int participantsOfTheDay) {
        mParticipantsOfTheDay = participantsOfTheDay;
    }

    public int getDateChoice() {
        return mDateChoice;
    }

    public void setDateChoice(int dateChoice) {
        mDateChoice = dateChoice;
    }

    public int getHourChoice() {
        return mHourChoice;
    }

    public void setHourChoice(int hourChoice) {
        mHourChoice = hourChoice;
    }
}
