package com.example.francoislf.go4lunch.controllers.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.business_service.GPSTracker;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_DATE_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_HOUR_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_CHOICE;


public class MainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, EventListener<QuerySnapshot> {

    private MapView mMapView;
    private View mView;
    private GoogleMap mMap;
    private GPSTracker mGPSTracker;
    private Location mLocation;
    private double mLatitude, mLongitude;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private Marker[] mListMarker;
    private ArrayList<RestaurantProfile> mRestaurantProfileList;

    private OnClickedResultMarker mCallback;

    public interface OnClickedResultMarker{
        void onResultMarkerTransmission(View view, String title);
        void executePlacesWithExtractor(View view, String coordinates);
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {mCallback = (OnClickedResultMarker) getActivity();}
        catch (ClassCastException e) {throw new ClassCastException(e.toString()+ " must implement OnClickedResultMarker");}
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) mLocation = mGPSTracker.getLocation(this.getContext());
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
            } else {ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);}
        } else {ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);}
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
                            return;}
                    }
                    mLocationPermissionsGranted = true;
                }}}
    }

    @SuppressLint("NewApi")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;}
        mMap.setMyLocationEnabled(true);
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 10, 30);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLatitude, mLongitude)));
        mCallback.executePlacesWithExtractor(this.mView,String.valueOf(mLatitude)+","+String.valueOf(mLongitude));
    }

    // Create markers from Observable
    public void markersCreation(ArrayList<RestaurantProfile> results){
        mRestaurantProfileList = results;
        mListMarker = new Marker[results.size()];
        mMap.clear();
        for (int i = 0 ; i < results.size() ; i++){
            mListMarker[i] = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(results.get(i).getLat(), results.get(i).getLng()))
                    .title(results.get(i).getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.punaise_orange)));
            if (results.get(i).getNumberOfParticipant() == 0) mListMarker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.punaise_orange));
            else mListMarker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.punaise_verte));
            mListMarker[i].setTag(i);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer markerTag = (Integer) marker.getTag();
        if (markerTag != null){
            mCallback.onResultMarkerTransmission(this.mView, marker.getTitle());
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        UserHelper.getUsersCollection().addSnapshotListener(this); // Add a listener when database FireStore change
    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        if (queryDocumentSnapshots != null && mRestaurantProfileList != null) {
            for (int i = 0; i < mRestaurantProfileList.size(); i++) mRestaurantProfileList.get(i).setNumberOfParticipant(0);
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (!document.getString(USER_DATE_CHOICE).equals(BLANK_ANSWER)) {
                    if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE), document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                        for (int i = 0; i < mRestaurantProfileList.size(); i++) {
                            if (document.getString(USER_RESTAURANT_CHOICE).equals(mRestaurantProfileList.get(i).getName())) {
                                int newNumber = mRestaurantProfileList.get(i).getNumberOfParticipant() + 1;
                                mRestaurantProfileList.get(i).setNumberOfParticipant(newNumber);
                            }}}}}
            this.markersCreation(mRestaurantProfileList);
        }
    }
}