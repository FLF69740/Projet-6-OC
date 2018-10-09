package com.example.francoislf.go4lunch.controllers.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.SettingsFragment;
import java.util.Objects;

public class SettingsActivity extends BaseActivity implements SettingsFragment.OnClickObjectFragmentSettings {

    private SharedPreferences mSharedPreferences;
    public static final String SWITCH_STATE = "SWITCH_STATE";
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
        bundle.putString(USER_UID, Objects.requireNonNull(getCurrentUser()).getUid());
        bundle.putString(USER_NAME, getCurrentUser().getDisplayName());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        bundle.putString(SWITCH_STATE, mSharedPreferences.getString(SWITCH_STATE, null));
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(bundle);
        return settingsFragment;
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
        UserHelper.deleteUser(Objects.requireNonNull(getCurrentUser()).getUid()).addOnFailureListener(this.onFailureListener());
    }

    @Override
    public void OnClickButtonSwitchNotifications(View view, boolean bool) {
        if (bool) {
            mSharedPreferences.edit().putString(SWITCH_STATE, "true").apply();
            Toast.makeText(this,"NOTIFICATION SYSTEM ACTIVATED", Toast.LENGTH_SHORT).show();
        } else {
            mSharedPreferences.edit().putString(SWITCH_STATE, "false").apply();
            Toast.makeText(this,"NOTIFICATION SYSTEM STOPPED", Toast.LENGTH_SHORT).show();
        }
    }
}
