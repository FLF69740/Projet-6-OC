package com.example.francoislf.go4lunch.models;

import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.business_service.GPSTracker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.text.DecimalFormat;
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
        } else return "24h/24";
    }

    // transform the state of the schedules (exemple from 13h00 to 1:00pm)
    public String changeHourFormat(double hour, boolean inverse){

        String result;
        DateTime dt = new DateTime();
        double hourChange = 0;

        if (hour != 0) hourChange = hour / 100;

        if (hour >= 0 && hour < 1) hourChange = 12;

        DecimalFormat numberFormat = new DecimalFormat("#.00");

        result = String.valueOf(numberFormat.format(hourChange));
        if (result.contains(".")) result = result.replace('.',':');
        if (result.contains(",")) result = result.replace(',',':');

        if (hour >= 1200 && hour < 1300) result = "12:00pm";
        else if (!inverse) {
            if (dt.get(DateTimeFieldType.halfdayOfDay()) == 0) {
                result += "am";
            }
            else {
                result = String.valueOf(numberFormat.format(hourChange - 12));
                if (result.contains(".")) result = result.replace('.',':');
                if (result.contains(",")) result = result.replace(',',':');
                result += "pm";
            }
        } else {
            result += "am";
        }

        if (result.charAt(0) == ':') result = "00" + result;
        if (result.charAt(1) == ':') result = "0" + result;
        if (result.charAt(4) == 'a') result = result.replace("am", "0am");
        if (result.charAt(4) == 'p') result = result.replace("pm", "0pm");

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

    // return a shorter string name if this one is too long
    public String getGoodSizeName(String name){

        if (name.toCharArray().length < 29) return name;
        else {
            String result = "";
            char[] chars = name.toCharArray();
            for (int i = 0 ; i < 29 ; i++) result += chars[i];
            result += "...";
            return result;
        }
    }


}
