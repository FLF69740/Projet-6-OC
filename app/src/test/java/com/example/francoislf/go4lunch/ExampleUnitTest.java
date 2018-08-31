package com.example.francoislf.go4lunch;

import com.example.francoislf.go4lunch.models.PlacesExtractor;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPhotoAndPlaceOrganisation(){

        ArrayList<String> restaurantProfileList = new ArrayList<>();
        List<String> stringList = new ArrayList<String>();

        restaurantProfileList.add("AAA");
        restaurantProfileList.add("BBB");
        restaurantProfileList.add("CCC");

        stringList.add("CCC#Photo3");
        stringList.add("AAA#Photo2");
        stringList.add("BBB#Photo1");

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0 ; i < restaurantProfileList.size() ; i++){
            for (int j = 0 ; j < stringList.size() ; j++){
                if (stringList.get(j).startsWith(restaurantProfileList.get(i))) result.add(stringList.get(j));
            }
        }

        assert(result.get(0).contains("AAA"));
        assert(result.get(1).contains("BBB"));
        assert(result.get(2).contains("CCC"));

    }

    @Test
    public void testPhotoSplitSection(){

        ArrayList<String> restaurantProfileList = new ArrayList<>();
        List<String> stringList = new ArrayList<String>();

        restaurantProfileList.add("AAA");
        restaurantProfileList.add("BBB");
        restaurantProfileList.add("CCC");

        stringList.add("CCC#Photo3");
        stringList.add("AAA#Photo1");
        stringList.add("BBB#Photo2");

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0 ; i < restaurantProfileList.size() ; i++){
            for (int j = 0 ; j < stringList.size() ; j++){
                if (stringList.get(j).startsWith(restaurantProfileList.get(i))) result.add(stringList.get(j));
            }
        }

        ArrayList<String> resultFinal = new ArrayList<>();

        for (int i = 0 ; i < result.size() ; i++){
            resultFinal.add(result.get(i).split("#")[1]);
        }

        assert(resultFinal.get(0).equals("Photo1"));
        assert(resultFinal.get(1).equals("Photo2"));
        assert(resultFinal.get(2).equals("Photo3"));

    }
}