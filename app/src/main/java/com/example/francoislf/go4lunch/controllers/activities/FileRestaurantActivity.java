package com.example.francoislf.go4lunch.controllers.activities;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.FileRestaurantFragment;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

public class FileRestaurantActivity extends BaseActivity {

    public static final String EXTRA_SNIPPET_MARKER = "EXTRA_SNIPPET_MARKER";
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
        String snippet = getIntent().getStringExtra(EXTRA_SNIPPET_MARKER);
        RestaurantProfile restaurantProfile;
        restaurantProfile = getJsonToPlace(snippet);
        mFileRestaurantFragment.setRestaurantProfileInformation(restaurantProfile);
    }
}
