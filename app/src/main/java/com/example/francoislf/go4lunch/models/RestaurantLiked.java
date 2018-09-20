package com.example.francoislf.go4lunch.models;

public class RestaurantLiked {

    private String mRestaurantName;
    private String mPlaceId;
    private int mLike;
    private String mParticipants;


    public RestaurantLiked(String restaurantName, String placeId, String user){
        this.mRestaurantName = restaurantName;
        this.mPlaceId = placeId;
        this.mLike = 1;
        this.mParticipants = user + ";";
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
        return mLike;
    }

    public void setNumberOfLike(int like) {
        mLike = like;
    }

    public String getParticipants() {
        return mParticipants;
    }

    public void setParticipants(String participants) {
        mParticipants = participants;
    }
}
