package com.example.francoislf.go4lunch.controllers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Utils.MyAlarmReceiver;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.SettingsFragment;
import java.util.Calendar;


public class SettingsActivity extends BaseActivity implements SettingsFragment.OnClickObjectFragmentSettings {

    private SettingsFragment mSettingsFragment;
    private SharedPreferences mSharedPreferences;
    private PendingIntent mPendingIntent;

    private static final String SWITCH_STATE = "SWITCH_STATE";
    public static final String UID_SETTINGS = "UID_SETTINGS";
    public static final int NOTIFICATION_CODE = 100;

    @Override
    protected boolean getContentViewBoolean() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected android.app.Fragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putString(USER_UID, getCurrentUser().getUid());
        bundle.putString(USER_NAME, getCurrentUser().getDisplayName());
        bundle.putString(SWITCH_STATE, getPreferences(MODE_PRIVATE).getString(SWITCH_STATE, null));
        mSettingsFragment = new SettingsFragment();
        mSettingsFragment.setArguments(bundle);
        return mSettingsFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.frame_layout_settings;
    }

    @Override
    protected String getFragmentTag() {
        return TAG_SETTINGS_FRAGMENT;
    }

    @Override
    public void OnClickButtonDeleteUserAccount(View view) {
        deleteUserFromFirebase();
        UserHelper.deleteUser(getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
    }

    @Override
    public void OnClickButtonSwitchNotifications(View view, boolean bool) {
        this.configureAlarmManager();
        if (bool) startAlarm();
        else stopAlarm();
    }

    /**
     *  ALARM MANAGER
     */

    private void configureAlarmManager(){
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra(UID_SETTINGS, getCurrentUser().getUid());
        mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startAlarm(){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mSharedPreferences.edit().putString(SWITCH_STATE, "true").apply();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingIntent);
        Toast.makeText(this,"NOTIFICATION SYSTEM ACTIVATED", Toast.LENGTH_SHORT).show();
    }

    private void stopAlarm(){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mSharedPreferences.edit().putString(SWITCH_STATE, "false").apply();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(mPendingIntent);
        Toast.makeText(this,"NOTIFICATION SYSTEM STOPPED", Toast.LENGTH_SHORT).show();
    }





}
