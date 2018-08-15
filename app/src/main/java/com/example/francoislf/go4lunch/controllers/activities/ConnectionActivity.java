package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

public class ConnectionActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 100;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_firebase_auth)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, R.string.connection_succeed, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                if (idpResponse == null) Toast.makeText(this, R.string.connection_canceled, Toast.LENGTH_SHORT).show();
                else if (idpResponse.getError().equals(ErrorCodes.NO_NETWORK)) Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, R.string.error_unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}