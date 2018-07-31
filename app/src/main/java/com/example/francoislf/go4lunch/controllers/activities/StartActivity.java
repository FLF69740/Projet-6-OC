package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

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
    protected Fragment newInstance() {return null;}

    @Override
    protected int getFragmentLayout() {return 0;}

    @Override
    protected int getToolbarView() {return 0;}

    @Override
    protected int getToolbarTitle() {return 0;}

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
