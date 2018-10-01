package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.francoislf.go4lunch.api.LikedHelper;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.connectionVerification();
    }

    @Override
    protected boolean getContentViewBoolean() {return false;}

    @Override
    protected int getContentView() {return 0;}

    @Override
    protected android.app.Fragment newInstance() {return null;}

    @Override
    protected int getFragmentLayout() {return 0;}

    @Override
    protected String getFragmentTag() {
        return "start";
    }

    private void connectionVerification(){
        if (getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ConnectionActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
