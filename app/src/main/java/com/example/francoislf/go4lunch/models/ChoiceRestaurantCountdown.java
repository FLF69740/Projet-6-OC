package com.example.francoislf.go4lunch.models;

import org.joda.time.DateTime;

public class ChoiceRestaurantCountdown {

    String mHour, mDateChoice;

    public ChoiceRestaurantCountdown(String hour, String dateChoice){
        this.mHour = hour;
        this.mDateChoice = dateChoice;
    }

    public boolean getCountdownResult(){
        boolean result = false;
        int hour = Integer.parseInt(mHour);
        int date = Integer.parseInt(mDateChoice);

        DateTime dt = new DateTime();

        if (date == dt.getDayOfYear() && hour < 12 && dt.getHourOfDay() >= 12) result = true;
        if (date == (dt.getDayOfYear()-1) && dt.getHourOfDay() >= 13) result = true;
        if (date < (dt.getDayOfYear()-1)) result = true;

        return result;
    }
}
