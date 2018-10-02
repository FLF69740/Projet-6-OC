package com.example.francoislf.go4lunch.business_service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.example.francoislf.go4lunch.R;

public class GPSTracker extends Service implements LocationListener{


    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Location mLocation;
    private LocationManager mLocationManager;

    public GPSTracker(){}

    //GetLocation methods


    public Location getLocation(Context context) {
        try{
            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER);
            isNetworkEnabled = mLocationManager.isProviderEnabled(mLocationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled){
                    if (mLocation == null){
                        mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 10000, 0, this);
                        if (mLocationManager != null) mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
                    }
                }

                if (mLocation == null) {
                    if (isNetworkEnabled){
                            mLocationManager.requestLocationUpdates(mLocationManager.NETWORK_PROVIDER, 10000, 10, this);
                            if (mLocationManager != null) mLocation = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
                        }
                    }
            }

        } catch (Exception ex){
            Log.e(getString(R.string.Log_i), ex.getMessage().toString());
        }

        return mLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
