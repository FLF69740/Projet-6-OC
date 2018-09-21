package com.example.francoislf.go4lunch.controllers.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected View mViewHeader;
    protected ImageView mImageViewProfile;
    protected TextView mTextViewName;
    protected TextView mTextViewEmail;
    public static final String USER_RESTAURANT_CHOICE = "restaurantChoice";
    public static final String USER_DATE_CHOICE = "dateChoice";
    public static final String USER_HOUR_CHOICE = "hourChoice";
    public static final String USER_UID = "uid";
    public static final String BLANK_ANSWER = "Empty";
    public static final String LIKE_NUMBER_OF_LIKE = "numberOfLike";
    public static final String LIKE_PARTICIPANTS = "participants";
    public static final String LIKE_PLACEID = "placeId";
    public static final String LIKE_RESTAURANT_NAME = "restaurantName";

    private static final int SIGN_OUT_TASK = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewBoolean()){
            setContentView(this.getContentView());
            configureFragment(savedInstanceState);
            ButterKnife.bind(this);
        }
    }

    // Apply or not setContentView
    protected abstract boolean getContentViewBoolean();

    // Load the view
    protected abstract int getContentView();

    // Link the fragment to the View
    protected void configureFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(getFragmentLayout(),newInstance())
                    .commit();
        }
    }

    // Load the fragment
    protected abstract Fragment newInstance();

    // Load the layout id
    protected abstract int getFragmentLayout();

    /**
     * UTILS
     */

    @Nullable
    protected FirebaseUser getCurrentUser(){return FirebaseAuth.getInstance().getCurrentUser();}

    /**
     *  REST REQUEST
     */

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRestRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case SIGN_OUT_TASK:
                        Intent intent = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        startActivity(intent);
                        break;
                    default: break;
                }
            }
        };
    }

    // User LogOut
    protected void signOutFormFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, updateUIAfterRestRequestsCompleted(SIGN_OUT_TASK));
    }

    /**
     *  ERROR HANDLER
     */

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error),Toast.LENGTH_LONG).show();
            }
        };
    }



}
