package com.example.francoislf.go4lunch.Utils;


import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.example.francoislf.go4lunch.Utils.App.CHANNEL;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_DATE_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_HOUR_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_NAME;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_ADRESS;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_UID;
import static com.example.francoislf.go4lunch.controllers.activities.SettingsActivity.NOTIFICATION_CODE;
import static com.example.francoislf.go4lunch.controllers.activities.SettingsActivity.UID_SETTINGS;


public class MyAlarmReceiver extends BroadcastReceiver {

    String mUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        mUser = Objects.requireNonNull(intent.getExtras()).getString(UID_SETTINGS);
        this.launchSearchInformationBeforeNotification(context);
    }

    private void notificationShow(Context context, String restaurant, String adress, List<String> workmates){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        StringBuilder contentText = new StringBuilder(context.getString(R.string.content_text) + " " + restaurant + "\n");
        contentText.append(context.getString(R.string.adress_notification)).append(" ").append(adress);
        if (!workmates.isEmpty()){
            contentText.append("\n").append(context.getString(R.string.participation_notification)).append(" ");
            for (int i = 0 ; i < workmates.size() ; i++) {
                contentText.append(workmates.get(i));
                if (i < (workmates.size() - 1)) contentText.append(", ");
            }
            contentText.append(".");
        }
        Notification notification = new NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.logo_only)
                .setContentTitle("GO4LUNCH")
                .setContentText(contentText.toString())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText.toString()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(NOTIFICATION_CODE, notification);
    }

    private void launchSearchInformationBeforeNotification(final Context context){
        UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> restaurantList = new ArrayList<>();
                List<String> tempUsernameList = new ArrayList<>();
                String targetRestaurant = null, targetAdress = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!Objects.equals(document.getString(USER_DATE_CHOICE), BLANK_ANSWER)){
                            if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE), document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                                if (Objects.equals(document.getString(USER_UID), mUser)) {
                                    targetRestaurant = document.getString(USER_RESTAURANT_CHOICE);
                                    targetAdress = document.getString(USER_RESTAURANT_ADRESS);
                                }
                                else {
                                    restaurantList.add(document.getString(USER_RESTAURANT_CHOICE));
                                    tempUsernameList.add(document.getString(USER_NAME));
                                }
                            }
                        }
                    }
                    if (targetRestaurant != null){
                        List<String> finalUsernameList = new ArrayList<>();
                        for (int i = 0 ; i < restaurantList.size() ; i++){
                            if (restaurantList.get(i).equals(targetRestaurant)) finalUsernameList.add(tempUsernameList.get(i));
                        }
                        notificationShow(context, targetRestaurant, targetAdress, finalUsernameList);
                    }
                }
            }
        });
    }


}
