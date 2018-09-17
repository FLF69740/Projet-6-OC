package com.example.francoislf.go4lunch.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.LikedHelper;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileRestaurantFragment extends Fragment {

    TextView mRestaurantName, mRestaurantLocalisation;
    @BindView(R.id.button_restaurant_choice)Button mButtonChoiceRestaurant;
    @BindView(R.id.restaurant_choice_no_validate)ImageView mImageViewRestaurantChoiceKO;
    @BindView(R.id.restaurant_choice_validate)ImageView mImageViewRestaurantChoiceOK;
    private static final String BLANK_ANSWER = "Empty";
    ImageView mRestaurantImage;
    Boolean isRestaurantChosen;
    String mPhoneNumber, mWebSite;
    View mView;
    RestaurantProfile mRestaurantProfile;
    int mHour, mDate, mParticipant;

    public FileRestaurantFragment(){}

    private OnClicChoiceRestaurant mCallback;

    public interface OnClicChoiceRestaurant{
        void onResultChoiceTransmission(View view, String name, String placeId, int hour, int date, int participant);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_restaurant, container, false);
        this.mRestaurantName = mView.findViewById(R.id.restaurant_name);
        this.mRestaurantLocalisation = mView.findViewById(R.id.restaurant_localisation);
        this.mRestaurantImage = mView.findViewById(R.id.restaurant_photo);
        ButterKnife.bind(this, mView);
        return mView;
    }

    public void setRestaurantProfileInformation(RestaurantProfile restaurantProfile){
        mRestaurantProfile = restaurantProfile;
        mRestaurantName.setText(mRestaurantProfile.getName());
        mRestaurantLocalisation.setText(mRestaurantProfile.getAdress());
        mPhoneNumber = mRestaurantProfile.getPhoneNumber();
        mWebSite = mRestaurantProfile.getWebSite();
        if (!mRestaurantProfile.getPhoto().equals(BLANK_ANSWER)) {
            Glide.with(mView)
                    .load(mRestaurantProfile.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestaurantImage);
        }
        LikedHelper.getLiked(restaurantProfile.getPlaceId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mHour = documentSnapshot.getLong("hourChoice").intValue();
                    mDate = documentSnapshot.getLong("dateChoice").intValue();
                    mParticipant = documentSnapshot.getLong("participantsOfTheDay").intValue();
                }
            }
        });
    }

    // Update the number of participant after the activity upload for this one
    public void setNumberOfParticipant(int number){
        mParticipant = number;
    }

    // get the name of restaurant choice from activity to fragment for button choice Restaurant setting
    public void setRestaurantChoiceForButton(String name){
        if (mRestaurantName.getText().equals(name)) isRestaurantChosen = true;
        else isRestaurantChosen = false;
        setCircleLogoRestaurantChoice(isRestaurantChosen, false);
    }

    @OnClick(R.id.button_restaurant_choice)
    public void changeRestaurantState(){
        isRestaurantChosen = !isRestaurantChosen;
        setCircleLogoRestaurantChoice(isRestaurantChosen, true);
    }

    // define the logo of the circle customer restaurant choice (green or red flag)
    private void setCircleLogoRestaurantChoice(boolean isChoosen, boolean isAction){
        mImageViewRestaurantChoiceKO.setVisibility(View.INVISIBLE);
        mImageViewRestaurantChoiceOK.setVisibility(View.INVISIBLE);
        if (isChoosen){
            mImageViewRestaurantChoiceOK.setVisibility(View.VISIBLE);
            if (isAction) mCallback.onResultChoiceTransmission(mView, mRestaurantProfile.getName(), mRestaurantProfile.getPlaceId(), mHour, mDate, mParticipant);
        } else {
            mImageViewRestaurantChoiceKO.setVisibility(View.VISIBLE);
            if (isAction) mCallback.onResultChoiceTransmission(mView, BLANK_ANSWER, mRestaurantProfile.getPlaceId(), mHour, mDate, mParticipant);
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.CALL)
    public void launchRestaurantCall(){
        if (mPhoneNumber == null) Toast.makeText(getContext(),getString(R.string.callDisabled),Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),mPhoneNumber,Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.WEBSITE)
    public void launchRestaurantWebSite(){
        if (mWebSite == null) Toast.makeText(getContext(), getString(R.string.websiteDisabled), Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),mWebSite,Toast.LENGTH_LONG).show();
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {
            mCallback = (OnClicChoiceRestaurant) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnClickedResultMarker");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }




}
