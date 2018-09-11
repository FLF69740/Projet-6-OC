package com.example.francoislf.go4lunch.controllers.activities;

import android.view.View;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.FileRestaurantFragment;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

public class FileRestaurantActivity extends BaseActivity implements FileRestaurantFragment.OnClicChoiceRestaurant{

    public static final String EXTRA_SNIPPET_MARKER = "EXTRA_SNIPPET_MARKER";
    private static final String BLANK_ANSWER = "Empty";
    private FileRestaurantFragment mFileRestaurantFragment;

    @Override
    protected boolean getContentViewBoolean() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_file_restaurant;
    }

    @Override
    protected android.app.Fragment newInstance() {
        mFileRestaurantFragment = new FileRestaurantFragment();
        return mFileRestaurantFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.frame_layout_file_restaurant;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.snippetMarkerTransmissionToFragment();
    }

    // Update UI
    private void snippetMarkerTransmissionToFragment(){
        RestaurantProfile restaurantProfile = getIntent().getExtras().getParcelable(EXTRA_SNIPPET_MARKER);
        mFileRestaurantFragment.setRestaurantProfileInformation(restaurantProfile);
    }

    // callback from fragment child
    @Override
    public void onResultChoiceTransmission(View view, String name) {
        if (!name.equals(BLANK_ANSWER)) UserHelper.updateRestaurantChoice(name, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
        else UserHelper.updateRestaurantChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
    }
}
