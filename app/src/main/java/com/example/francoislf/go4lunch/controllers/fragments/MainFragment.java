package com.example.francoislf.go4lunch.controllers.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.business_service.GPSTracker;
import com.example.francoislf.go4lunch.models.HttpRequest.GoogleStreams;
import com.example.francoislf.go4lunch.models.HttpRequest.NearbySearch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class MainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mMapView;
    private View mView;
    private GoogleMap mMap;
    private GPSTracker mGPSTracker;
    private Location mLocation;
    private double mLatitude, mLongitude;
    private static final String PARAMETER_PLACE_API_RADIUS = "1000";
    private static final String PARAMETER_PLACE_API_TYPE = "restaurant";
    private static final String PARAMETER_PLACE_API_KEY = "AIzaSyBop_LoznRWmdx8u9VjSFu0PVaAqO0mO8U";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private Marker[] mListMarker;

    private OnClickedResultMarker mCallback;

    public interface OnClickedResultMarker{
        void onResultMarkerTransmission(View view, String snippet);
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {
            mCallback = (OnClickedResultMarker) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnClickedResultMarker");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        getLocalPermissions();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mLocationPermissionsGranted) {
            mGPSTracker = new GPSTracker();
            mLocation = mGPSTracker.getLocation(getContext());
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();

            mMapView = mView.findViewById(R.id.map);
            if (mMapView != null) {
                mMapView.onCreate(null);
                mMapView.onResume();
                mMapView.getMapAsync(this);
            }
        }
    }

    // getting location permissions
    private void getLocalPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 10, 30);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLatitude, mLongitude)));
        executeHttpRequestWithRetrofit();
    }

    // Create marker from Observable
    private void markersCreation(List<NearbySearch.Result> results){
        mListMarker = new Marker[results.size()];
        for (int i = 0 ; i < results.size() ; i++){
            mListMarker[i] = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(results.get(i).getGeometry().getLocation().getLat(), results.get(i).getGeometry().getLocation().getLng()))
                    .title(results.get(i).getName())
                    .snippet(results.get(i).getPlaceId())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.punaise_orange)));
            mListMarker[i].setTag(i);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer markerTag = (Integer) marker.getTag();
        if (markerTag != null){
            Toast.makeText(getContext(), "name : " + marker.getTitle().toString() +
                    "\ninfo :" + marker.getSnippet().toString(), Toast.LENGTH_SHORT).show();
            mCallback.onResultMarkerTransmission(this.mView, marker.getSnippet().toString());
        }
        return false;
    }

    /**
     *  HTTP (RxJAVA)
     */

    private Disposable mDisposable;

    private void executeHttpRequestWithRetrofit(){
        this.mDisposable = GoogleStreams.streamNearbySearch(String.valueOf(mLatitude)+","+String.valueOf(mLongitude),PARAMETER_PLACE_API_RADIUS, PARAMETER_PLACE_API_TYPE,PARAMETER_PLACE_API_KEY)
                .subscribeWith(new DisposableObserver<NearbySearch>() {
                    @Override
                    public void onNext(NearbySearch nearbySearch) {
                        if (!nearbySearch.getResults().isEmpty()) {
                            StringBuilder stringBuilder = new StringBuilder();
                            List<NearbySearch.Result> results = nearbySearch.getResults();
                            markersCreation(results);
                            for (int i = 0 ; i < results.size() ; i++){
                                stringBuilder.append("-" + results.get(i).getName().toString() + " ; ");
                            }
                            Log.i("TAGAA", "réponse chargée!");
                            Log.i("TAGAA", stringBuilder.toString());
                        }
                        else Log.i("TAGAA", "aucune réponse trouvée!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }


}
