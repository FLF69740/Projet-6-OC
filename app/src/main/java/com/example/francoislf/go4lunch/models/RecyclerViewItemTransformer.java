package com.example.francoislf.go4lunch.models;

import org.joda.time.DateTime;

import java.util.List;

public class RecyclerViewItemTransformer {

    public RecyclerViewItemTransformer(){}

    public String getShortAdress(String longAdress){
        String verif = longAdress.split(",")[1];
        if (Character.isDigit(verif.charAt(1)) && Character.isDigit(verif.charAt(2))
                && Character.isDigit(verif.charAt(3)) && Character.isDigit(verif.charAt(4)))
        return longAdress.split(",")[0];
        else return (longAdress.split(",")[0] + longAdress.split(",")[1]);
    }

    public String getOpeningAnswer(List<String> stringList){

        if (!stringList.isEmpty()){

            DateTime dt = new DateTime();



            return "Open";
        //    return stringList.get(dt.getDayOfWeek() -1);
        } else return "";
    }


}
