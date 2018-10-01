package com.example.francoislf.go4lunch.models.HttpRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GoogleStreams {

    /**
     *  simple stream
     */

    // request for map places localisation and general informations information
    public static Observable<NearbySearch> streamNearbySearch(String localisation, String radius, String type, String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getGoogleRequest(localisation, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }

    // request for complete informations about a place
    public static Observable<Places> streamPlaces(String placeid, String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getPlaces(placeid, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }

    // request for a prediction
    public static Observable<Prediction> streamPrediction(String input, String localisation, String radius, String type, final String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getPredictions(input, localisation, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }

    /**
     *  complex streams
     */

    // request for a list of complete informations about places
    public static Observable<List<Places>> streamListPlaces(String localisation, String radius, String type, final String key) {
        return streamNearbySearch(localisation, radius, type, key)
                .flatMap(new Function<NearbySearch, ObservableSource<NearbySearch.Result>>() {
                    @Override
                    public ObservableSource<NearbySearch.Result> apply(NearbySearch nearbySearch) throws Exception {
                        return Observable.fromIterable(nearbySearch.getResults());
                    }
                })
                .flatMap(new Function<NearbySearch.Result, ObservableSource<Places>>() {
                    @Override
                    public ObservableSource<Places> apply(NearbySearch.Result result) throws Exception {
                        return streamPlaces(result.getPlaceId(), key);
                    }
                })
                .toList()
                .toObservable();

    }

    // request for a list of places with prediction
    public static Observable<List<Places>> streamListPlacesPrediction(String input, String localisation, String radius, String type, final String key){
        return streamPrediction(input, localisation, radius, type, key)
                .flatMap(new Function<Prediction, ObservableSource<Prediction.Prediction_>>() {
                    @Override
                    public ObservableSource<Prediction.Prediction_> apply(Prediction prediction) throws Exception {
                        return Observable.fromIterable(prediction.getPredictions());
                    }
                })
                .flatMap(new Function<Prediction.Prediction_, ObservableSource<Places>>() {
                    @Override
                    public ObservableSource<Places> apply(Prediction.Prediction_ prediction_) throws Exception {
                        return streamPlaces(prediction_.getPlaceId(), key);
                    }
                })
                .toList()
                .toObservable();

    }


}
