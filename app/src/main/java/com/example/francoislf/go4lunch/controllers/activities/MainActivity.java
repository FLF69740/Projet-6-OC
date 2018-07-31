package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Override
    protected int getContentView() {return R.layout.activity_main;}

    @Override
    protected Fragment newInstance() {return new MainFragment();}

    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}

    @Override
    protected int getToolbarView() {return R.id.activity_main_toolbar;}

    @Override
    protected int getToolbarTitle() {return R.string.general_toolbar_title;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.signInActivity();
        ButterKnife.bind(this);
        this.configureBottomNavigationView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, R.string.connection_succeed, Toast.LENGTH_SHORT).show();
                this.updateProfileInformations();
            }
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


    // Update Nave_header informations about user informations
    protected void updateProfileInformations() {
        if (this.getCurrentUser() != null){
            if (this.getCurrentUser().getPhotoUrl() != null){
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            }


            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            this.mTextViewEmail.setText(email);
            this.mTextViewName.setText(username);
        }
    }

    /**
     * BOTTOM NAVIGATION BAR
     */

    private void configureBottomNavigationView(){
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_onglet_map_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_1)); break;
                    case R.id.ic_onglet_list_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_2)); break;
                    case R.id.ic_onglet_workmates: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_3)); break;
                }
                return true;
            }
        });
    }


}
