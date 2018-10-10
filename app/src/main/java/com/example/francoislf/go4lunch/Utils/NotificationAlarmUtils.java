package com.example.francoislf.go4lunch.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import org.joda.time.DateTime;
import java.util.Calendar;
import java.util.Objects;
import static com.example.francoislf.go4lunch.controllers.activities.FileRestaurantActivity.UID_SETTINGS;

public class NotificationAlarmUtils {

    public static PendingIntent getAlarmManagerPendingIntent(Context context, String uid){
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra(UID_SETTINGS, Objects.requireNonNull(uid));
        return PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void startAlarm(Context context, PendingIntent pendingIntent){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);

        DateTime dt = new DateTime();
        if (dt.getHourOfDay() > 12) calendar.add(Calendar.DAY_OF_YEAR, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public static void stopAlarm(Context context, PendingIntent pendingIntent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

}
