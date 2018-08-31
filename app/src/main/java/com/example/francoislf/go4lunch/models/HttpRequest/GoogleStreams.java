package com.example.francoislf.go4lunch.models.HttpRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GoogleStreams {

    public static Observable<NearbySearch> streamNearbySearch(String localisation, String radius, String type, String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getGoogleRequest(localisation, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }

    public static Observable<Places> streamPlaces(String placeid, String key){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.getPlaces(placeid, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS);
    }


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

    public static Observable<List<String>> streamListPhotos(String localisation, String radius, String type, final String key){
        return streamNearbySearch(localisation, radius, type, key)
                .flatMap(new Function<NearbySearch, ObservableSource<NearbySearch.Result>>() {
                    @Override
                    public ObservableSource<NearbySearch.Result> apply(NearbySearch nearbySearch) throws Exception {
                        return Observable.fromIterable(nearbySearch.getResults());
                    }
                })
                .map(new Function<NearbySearch.Result, String>() {
                    @Override
                    public String apply(NearbySearch.Result result) throws Exception {
                        if (result.getPhotos() != null)
                        return result.getName() + "#https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()
                                + "&key=" + key;
                        else return result.getName() + "#Empty";
                    }
                })
                .toList()
                .toObservable();
    }


}
