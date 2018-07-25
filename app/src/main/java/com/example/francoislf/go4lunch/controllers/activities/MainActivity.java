package com.example.francoislf.go4lunch.controllers.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 100;

    @Override
    protected int getContentView() {return R.layout.activity_main;}

    @Override
    protected Fragment newInstance() {return new MainFragment();}

    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.signInActivity();
    }

    private void signInActivity(){
        //I rather using a list, this way handling providers is separeted from the login methods
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_firebase_auth)
                        .build(),
                RC_SIGN_IN);
    }
}
