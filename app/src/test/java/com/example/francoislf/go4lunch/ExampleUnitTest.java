package com.example.francoislf.go4lunch;

import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RecyclerViewItemTransformer;
import org.joda.time.DateTime;
import org.junit.Test;
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
    public void testChoiceRestaurantCountdownWithYesterdayPm(){

        DateTime dt = new DateTime();
        String date = String.valueOf(dt.getDayOfYear() -1);
        String hour = "14";

        boolean isWaiting = new ChoiceRestaurantCountdown(hour,date).getCountdownResult();

        if (dt.getHourOfDay() > 13) assertTrue(isWaiting);
        else assertFalse(isWaiting);
    }

    @Test
    public void testChoiceRestaurantCountdownWithTodayPm(){

        DateTime dt = new DateTime();
        String date = String.valueOf(dt.getDayOfYear());
        String hour = "13";

        boolean isWaiting = new ChoiceRestaurantCountdown(hour,date).getCountdownResult();

        if (dt.getHourOfDay() >= 14) assertFalse(isWaiting);
    }

    @Test
    public void testChoiceRestaurantCountdownWithTodayAm(){

        DateTime dt = new DateTime();
        String date = String.valueOf(dt.getDayOfYear());
        String hour = "9";

        boolean isWaiting = new ChoiceRestaurantCountdown(hour,date).getCountdownResult();

        if (dt.getHourOfDay() >= 13) assertTrue(isWaiting);
        else assertFalse(isWaiting);
    }

    @Test
    public void testChoiceRestaurantCountdownWithBeforeYesterday(){

        DateTime dt = new DateTime();
        String date = String.valueOf(dt.getDayOfYear() -2);
        String hour = "14";

        boolean isWaiting = new ChoiceRestaurantCountdown(hour,date).getCountdownResult();

        assertTrue(isWaiting);
    }

    /**
     *  TEST ABOUT RECYCLERVIEW ITEM TRANSFORMER
     */

    @Test
    public void testGoodSectionLocalisationWithRecyclerViewItemTransformer(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        String string = "AAA Rue machin chose, 69000 Lyon, France";
        String change = recyclerViewItemTransformer.getShortAdress(string);

        assert (change.equals("AAA Rue machin chose"));

        string = "BB C, Rue du truc, 69000 Lyon, France";
        change = recyclerViewItemTransformer.getShortAdress(string);

        assert (change.equals("BB C Rue du truc"));
    }

    @Test
    public void testChangeHourFormatFor16h(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        double numeric = 1600;
        String hour = recyclerViewItemTransformer.changeHourFormat(numeric, false);
        assertEquals ("04:00pm", hour);
    }

    @Test
    public void testChangeHourFormatFor1h(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        double numeric = 100;
        String hour = recyclerViewItemTransformer.changeHourFormat(numeric, true);
        assertEquals ("01:00am", hour);
    }

    @Test
    public void testChangeHourFormatForMidnight(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        double numeric = 0;
        String hour = recyclerViewItemTransformer.changeHourFormat(numeric, true);
        assertEquals ("12:00am", hour);
    }

    @Test
    public void testChangeHourFormatFor12h(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        double numeric = 1200;
        String hour = recyclerViewItemTransformer.changeHourFormat(numeric, true);
        assertEquals ("12:00pm", hour);
    }

    @Test
    public void testSizeStringMoreMaximum(){
        RecyclerViewItemTransformer recyclerViewItemTransformer = new RecyclerViewItemTransformer();

        StringBuilder before = new StringBuilder();
        for (int i = 0 ; i < 40 ; i++) before.append(i);
        String name = recyclerViewItemTransformer.getGoodSizeName(before.toString());

        assert(before.toString().length() > 32);
        assertEquals(32, name.length());
    }

}