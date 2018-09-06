package com.example.francoislf.go4lunch.models;

import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.business_service.GPSTracker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.util.List;

public class RecyclerViewItemTransformer {

    Context mContext;
    private Location mLocation;

    public RecyclerViewItemTransformer(){}

    public RecyclerViewItemTransformer(Context context){mContext = context;}

    // method in order to have the adress without the town, postal code, and country
    public String getShortAdress(String longAdress){
        String verif = longAdress.split(",")[1];
        if (Character.isDigit(verif.charAt(1)) && Character.isDigit(verif.charAt(2))
                && Character.isDigit(verif.charAt(3)) && Character.isDigit(verif.charAt(4)))
        return longAdress.split(",")[0];
        else return (longAdress.split(",")[0] + longAdress.split(",")[1]);
    }

    // return the situation between the current hour and opening time
    public String getOpeningAnswer(List<String> stringList){

        if (!stringList.isEmpty()){

            DateTime dt = new DateTime();
            String period = mContext.getString(R.string.fermé);
            double openingHour, closeHour, actualHour;

            for (int i = 0 ; i < stringList.size() ; i++){
                if (stringList.get(i).contains("Open" + dt.getDayOfWeek())) {
                    if (stringList.get(i-1).contains("Close" + dt.getDayOfWeek())) {

                        openingHour = Integer.parseInt(stringList.get(i).split(",")[1]);
                        closeHour = Integer.parseInt(stringList.get(i-1).split(",")[1]);
                        actualHour = dt.getHourOfDay() * 100 + dt.getMinuteOfHour();

                        if ((actualHour >= openingHour && actualHour < (closeHour - 1))){
                            period =  mContext.getString(R.string.ouvert) + " " + changeHourFormat(closeHour, false);
                            i = stringList.size();
                        }
                        else if (actualHour >= openingHour && actualHour >= (closeHour - 30) && actualHour < closeHour) {
                            period =  mContext.getString(R.string.bientôt_fermé) + closeHour;
                            i = stringList.size();
                        }
                        else if (actualHour < openingHour){
                            period = mContext.getString(R.string.pause) + " " + changeHourFormat(openingHour, false);
                            i = stringList.size();
                        }
                    }
                    if (stringList.get(i-1).contains("Close" + String.valueOf(dt.getDayOfWeek()+1))){
                        closeHour = Integer.parseInt(stringList.get(i-1).split(",")[1]);
                        period =  mContext.getString(R.string.ouvert) + " " + changeHourFormat(closeHour, true);
                        i = stringList.size();
                    }
                }
            }


            return period;
        } else return "";
    }

    // transform the state of the schedules (exemple from 13h00 to 1:00pm)
    private String changeHourFormat(double hour, boolean inverse){

        String result;
        DateTime dt = new DateTime();
        double hourChange = 0;

        if (hour != 0) hourChange = hour / 100;

        if (hour >= 0 && hour < 1) hourChange = 12;

        result = String.valueOf(hourChange);
        result = result.replace('.',':');
        if (result.contains(":0")) result += "0";

        if (hour >= 1200 && hour < 1300) result = "12:00pm";
        else if (!inverse) {
            if (dt.get(DateTimeFieldType.halfdayOfDay()) == 0) {
                result += "am";
            }
            else {
                result = String.valueOf(hourChange - 12);
                result = result.replace('.',':');
                if (result.contains(":0")) result += "0";
                result += "pm";
            }
        } else {
            result += "am";
        }

        return result;
    }

    // return the distance between the current localisation to the target localisation
    public String getDistance(double goLat, double goLng){

        GPSTracker gPSTracker = new GPSTracker();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLocation = gPSTracker.getLocation(mContext);
        }
        double currentLat = mLocation.getLatitude();
        double currentLng = mLocation.getLongitude();

        double sinLat, sinLat2, cosLat, cosLat2, cosLong, sinHE, he, difLong = Math.abs(currentLng - goLng);

        sinLat = Math.sin(Math.toRadians(currentLat));
        sinLat2 = Math.sin(Math.toRadians(goLat));
        cosLat = Math.cos(Math.toRadians(currentLat));
        cosLat2 = Math.cos(Math.toRadians(goLat));
        cosLong = Math.cos(Math.toRadians(difLong));

        sinHE = (sinLat*sinLat2) + (cosLat*cosLat2*cosLong);

        he = Math.toDegrees(Math.asin(sinHE));

        double distance = (90 - he) * 60 * 1.85185 * 1000;
        int result = (int) distance;

        return String.valueOf(result) + "m";
    }


}
