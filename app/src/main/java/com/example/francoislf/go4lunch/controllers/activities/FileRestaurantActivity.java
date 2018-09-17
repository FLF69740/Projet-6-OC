package com.example.francoislf.go4lunch.controllers.activities;

import android.view.View;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.LikedHelper;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.FileRestaurantFragment;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.example.francoislf.go4lunch.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.joda.time.DateTime;

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
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (!currentUser.getHourChoice().equals(BLANK_ANSWER) &&
                new ChoiceRestaurantCountdown(currentUser.getHourChoice(), currentUser.getDateChoice()).getCountdownResult())
                    mFileRestaurantFragment.setRestaurantChoiceForButton(BLANK_ANSWER);
                else mFileRestaurantFragment.setRestaurantChoiceForButton(currentUser.getRestaurantChoice());

            }
        });
    }

    // callback from fragment child
    @Override
    public void onResultChoiceTransmission(View view, String name, String placeId, int hour, int date, int participant) {
        DateTime dt = new DateTime();
        if (!name.equals(BLANK_ANSWER)) {
            UserHelper.updateRestaurantChoice(name, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(String.valueOf(dt.getHourOfDay()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(String.valueOf(dt.getDayOfYear()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());

            if (!new ChoiceRestaurantCountdown(String.valueOf(hour), String.valueOf(date)).getCountdownResult()) {
                LikedHelper.updateParticipant(placeId, (participant + 1)).addOnFailureListener(this.onFailureListener());
                mFileRestaurantFragment.setNumberOfParticipant(participant + 1);
            }
            else {
                LikedHelper.createLiked(name, placeId).addOnFailureListener(this.onFailureListener());
                LikedHelper.updateParticipant(placeId, 1).addOnFailureListener(this.onFailureListener());
                LikedHelper.updateDateChoice(placeId, dt.getDayOfYear()).addOnFailureListener(this.onFailureListener());
                LikedHelper.updateHourChoice(placeId, dt.getHourOfDay()).addOnFailureListener(this.onFailureListener());
                mFileRestaurantFragment.setNumberOfParticipant(1);
            }
        }
        else {
            UserHelper.updateRestaurantChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            LikedHelper.updateParticipant(placeId, participant-1).addOnFailureListener(this.onFailureListener());
            mFileRestaurantFragment.setNumberOfParticipant(participant - 1);
        }
    }
}
