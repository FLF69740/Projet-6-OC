package com.example.francoislf.go4lunch.controllers.activities;

import android.support.v4.app.Fragment;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentView() {return R.layout.activity_main;}

    @Override
    protected Fragment newInstance() {return new MainFragment();}

    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}


}
