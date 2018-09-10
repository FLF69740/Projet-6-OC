package com.example.francoislf.go4lunch.models;

public class RestaurantLiked {

    private String mRestaurantName;
    private String mPlaceId;
    private int mNumberOfLike;

    public RestaurantLiked(String restaurantName, String placeId){
        this.mRestaurantName = restaurantName;
        this.mPlaceId = placeId;
        this.mNumberOfLike = 0;
    }

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
}
