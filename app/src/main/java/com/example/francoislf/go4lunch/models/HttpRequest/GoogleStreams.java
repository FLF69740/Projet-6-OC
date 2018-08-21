package com.example.francoislf.go4lunch.models.HttpRequest;


import com.example.francoislf.go4lunch.models.GoogleService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoogleStreams {

    public static Observable<NearbySearch> streamNearbySearch(String localisation, String radius, String type, String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getGoogleRequest(localisation, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }

}
