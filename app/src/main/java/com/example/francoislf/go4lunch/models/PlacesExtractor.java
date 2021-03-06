package com.example.francoislf.go4lunch.models;

import android.content.Context;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.HttpRequest.Places;
import java.util.ArrayList;
import java.util.List;

public class PlacesExtractor {

    private ArrayList<RestaurantProfile> mRestaurantProfileList;
    private List<Places> mPlacesList;
    private RecyclerViewItemTransformer mRecyclerViewItemTransformer;
    private Context mContext;

    public PlacesExtractor(Context context){mContext = context;}

    public void setPlacesList(List<Places> placesList){
        mRestaurantProfileList = new ArrayList<>();
        this.mPlacesList = placesList;
        mRecyclerViewItemTransformer = new RecyclerViewItemTransformer(mContext);
        extractData();
    }

    // Extract the datas from the POJO Places to the java class RestaurantProfile (into a List)
    private void extractData(){
        for (int i = 0 ; i < mPlacesList.size() ; i++) {
            mRestaurantProfileList.add(new RestaurantProfile());
            mRestaurantProfileList.get(i).setName(mPlacesList.get(i).getResult().getName());
            mRestaurantProfileList.get(i).setPlaceId(mPlacesList.get(i).getResult().getPlaceId());
            mRestaurantProfileList.get(i).setAdress(mRecyclerViewItemTransformer.getShortAdress(mPlacesList.get(i).getResult().getFormattedAddress()));
            mRestaurantProfileList.get(i).setLat(mPlacesList.get(i).getResult().getGeometry().getLocation().getLat());
            mRestaurantProfileList.get(i).setLng(mPlacesList.get(i).getResult().getGeometry().getLocation().getLng());
            if (mPlacesList.get(i).getResult().getOpeningHours() != null)
                mRestaurantProfileList.get(i).setWeekHour(getOpeningHours(mPlacesList.get(i)));
            if (mPlacesList.get(i).getResult().getFormattedPhoneNumber() != null)
                mRestaurantProfileList.get(i).setPhoneNumber(mPlacesList.get(i).getResult().getFormattedPhoneNumber());

            if (mPlacesList.get(i).getResult().getPhotos() != null)
            mRestaurantProfileList.get(i).setPhoto("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    mPlacesList.get(i).getResult().getPhotos().get(0).getPhotoReference() + "&key=" + mContext.getString(R.string.google_place_api_key));

            if (mPlacesList.get(i).getResult().getWebsite() != null)
                mRestaurantProfileList.get(i).setWebSite(mPlacesList.get(i).getResult().getWebsite());
        }
    }

    // transform the Close and Open java class from POJO API Place to String List
    private List<String> getOpeningHours(Places places){
        List<String> results = new ArrayList<>();

        if (places.getResult().getOpeningHours().getPeriods().get(0).getClose() != null) {
            for (int i = 0; i < places.getResult().getOpeningHours().getPeriods().size(); i++) {

                results.add("Close" + String.valueOf(places.getResult().getOpeningHours().getPeriods().get(i).getClose().getDay()) + "," +
                        places.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime());

                results.add("Open" + String.valueOf(places.getResult().getOpeningHours().getPeriods().get(i).getOpen().getDay()) + "," +
                        places.getResult().getOpeningHours().getPeriods().get(i).getOpen().getTime());
            }
        }

        return results;
    }


    // return the List of RestaurantProfile after the job
    public ArrayList<RestaurantProfile> getRestaurantProfileList() {
        return mRestaurantProfileList;
    }

    // return the ProfileRestaurant with his name
    public RestaurantProfile getRestaurantProfile(String name){
        int goodId = 0;
        for (int i = 0 ; i < mRestaurantProfileList.size() ; i++){
            if (mRestaurantProfileList.get(i).getName().equals(name)) goodId = i;
        }
        return mRestaurantProfileList.get(goodId);
    }

}
