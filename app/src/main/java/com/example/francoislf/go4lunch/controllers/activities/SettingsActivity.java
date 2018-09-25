package com.example.francoislf.go4lunch.controllers.activities;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.SettingsFragment;


public class SettingsActivity extends BaseActivity {

    private SettingsFragment mSettingsFragment;

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
        mSettingsFragment = new SettingsFragment();
        return mSettingsFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.frame_layout_settings;
    }
}
