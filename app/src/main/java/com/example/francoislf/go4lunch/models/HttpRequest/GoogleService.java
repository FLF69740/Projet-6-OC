package com.example.francoislf.go4lunch.models.HttpRequest;

import com.example.francoislf.go4lunch.models.HttpRequest.NearbySearch;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {

    @GET("nearbysearch/json?radius=1000")
    Observable<NearbySearch> getGoogleRequest(@Query("location") String localisation,
                                              @Query("radius") String radius,
                                              @Query("type") String type,
                                              @Query("key") String key);

    @GET("details/json?langage=en")
    Observable<Places> getPlaces(@Query("placeid") String placeid,
                                 @Query("key") String key);

    @GET("autocomplete/json?strictbounds")
    Observable<Prediction> getPredictions(@Query("input") String input,
                                          @Query("location") String localisation,
                                          @Query("radius") String radius,
                                          @Query("type") String type,
                                          @Query("key") String key);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
