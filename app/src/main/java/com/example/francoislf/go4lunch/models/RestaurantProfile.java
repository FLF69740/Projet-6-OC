package com.example.francoislf.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RestaurantProfile implements Parcelable {

    private String mPlaceId;
    private String mName;
    private String mPhoneNumber;
    private String mAdress;
    private String mWebSite;
    private List<String> mWeekHour;
    private double lat;
    private double lng;
    private String mPhoto;


    public RestaurantProfile(){mWeekHour = new ArrayList<>();}


    protected RestaurantProfile(Parcel in) {
        mPlaceId = in.readString();
        mName = in.readString();
        mPhoneNumber = in.readString();
        mAdress = in.readString();
        mWebSite = in.readString();
        mWeekHour = in.createStringArrayList();
        lat = in.readDouble();
        lng = in.readDouble();
        mPhoto = in.readString();
    }

    public static final Creator<RestaurantProfile> CREATOR = new Creator<RestaurantProfile>() {
        @Override
        public RestaurantProfile createFromParcel(Parcel in) {
            return new RestaurantProfile(in);
        }

        @Override
        public RestaurantProfile[] newArray(int size) {
            return new RestaurantProfile[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPlaceId);
        dest.writeString(mName);
        dest.writeString(mPhoneNumber);
        dest.writeString(mAdress);
        dest.writeString(mWebSite);
        dest.writeStringList(mWeekHour);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(mPhoto);
    }

    public static Parcelable.Creator<RestaurantProfile> getCreator(){
        return CREATOR;
    }
}
