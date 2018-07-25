package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK) Toast.makeText(this, R.string.connection_succeed, Toast.LENGTH_SHORT).show();
            else {
                if (idpResponse == null) Toast.makeText(this, R.string.connection_canceled, Toast.LENGTH_SHORT).show();
                else if (idpResponse.getError().equals(ErrorCodes.NO_NETWORK)) Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, R.string.error_unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * NAVIGATION
     */

    // Firebase-UI activity creation
    private void signInActivity(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
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


}
