package com.example.francoislf.go4lunch.models;

import com.example.francoislf.go4lunch.models.HttpRequest.Places;

import java.util.ArrayList;
import java.util.List;

public class PlacesExtractor {

    private ArrayList<RestaurantProfile> mRestaurantProfileList;
    List<Places> mPlacesList;

    public PlacesExtractor(){}

    public void setPlacesList(List<Places> placesList){
        mRestaurantProfileList = new ArrayList<>();
        this.mPlacesList = placesList;
        extractData();
    }

    private void extractData(){
        for (int i = 0 ; i < mPlacesList.size() ; i++) {
            mRestaurantProfileList.add(new RestaurantProfile());
            mRestaurantProfileList.get(i).setName(mPlacesList.get(i).getResult().getName());
            mRestaurantProfileList.get(i).setPlaceId(mPlacesList.get(i).getResult().getPlaceId());
            mRestaurantProfileList.get(i).setAdress(mPlacesList.get(i).getResult().getFormattedAddress());
            mRestaurantProfileList.get(i).setLat(mPlacesList.get(i).getResult().getGeometry().getLocation().getLat());
            mRestaurantProfileList.get(i).setLng(mPlacesList.get(i).getResult().getGeometry().getLocation().getLng());
            if (mPlacesList.get(i).getResult().getOpeningHours() != null)
                mRestaurantProfileList.get(i).setWeekHour(mPlacesList.get(i).getResult().getOpeningHours().getWeekdayText());
            if (mPlacesList.get(i).getResult().getFormattedPhoneNumber() != null)
                mRestaurantProfileList.get(i).setPhoneNumber(mPlacesList.get(i).getResult().getFormattedPhoneNumber());
            if (mPlacesList.get(i).getResult().getWebsite() != null)
                mRestaurantProfileList.get(i).setWebSite(mPlacesList.get(i).getResult().getWebsite());
        }
    }

    public ArrayList<RestaurantProfile> getRestaurantProfileList() {
        return mRestaurantProfileList;
    }

    public RestaurantProfile getRestaurantProfile(String name){
        int goodId = 0;
        for (int i = 0 ; i < mRestaurantProfileList.size() ; i++){
            if (mRestaurantProfileList.get(i).getName().equals(name)) goodId = i;
        }
        return mRestaurantProfileList.get(goodId);
    }

    public List<String> organisePhotoAndProfile(ArrayList<RestaurantProfile> restaurantProfileList, List<String> stringList){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> resultFinal = new ArrayList<>();
        for (int i = 0 ; i < restaurantProfileList.size() ; i++){
            for (int j = 0 ; j < stringList.size() ; j++){
                if (stringList.get(j).startsWith(restaurantProfileList.get(i).getName())) result.add(stringList.get(j));
            }
        }

        for (int i = 0 ; i < result.size() ; i++){
            resultFinal.add(result.get(i).split("#")[1]);
        }

        return resultFinal;
    }
}
