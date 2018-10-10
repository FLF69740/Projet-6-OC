package com.example.francoislf.go4lunch.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            NotificationAlarmUtils.startAlarm(context, NotificationAlarmUtils
                    .getAlarmManagerPendingIntent(context, FirebaseAuth.getInstance().getCurrentUser().getUid()));
        }
    }
}
