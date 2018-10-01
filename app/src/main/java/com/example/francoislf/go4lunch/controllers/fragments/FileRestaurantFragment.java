package com.example.francoislf.go4lunch.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Views.IsJoiningAdapter;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.example.francoislf.go4lunch.models.User;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;

public class FileRestaurantFragment extends Fragment {

    TextView mRestaurantName, mRestaurantLocalisation;
    @BindView(R.id.button_restaurant_choice)Button mButtonChoiceRestaurant;
    @BindView(R.id.restaurant_choice_no_validate)ImageView mImageViewRestaurantChoiceKO;
    @BindView(R.id.restaurant_choice_validate)ImageView mImageViewRestaurantChoiceOK;
    @BindView(R.id.Like)TextView mLikeTextViewButton;
    @BindView(R.id.yellow_star_1)ImageView mYellowStarOne;
    @BindView(R.id.yellow_star_2)ImageView mYellowStarTwo;
    @BindView(R.id.yellow_star_3)ImageView mYellowStarThree;
    @BindView(R.id.recyclerview_joining)RecyclerView mRecyclerView;
    ImageView mRestaurantImage;
    Boolean isRestaurantChosen;
    String mPhoneNumber, mWebSite;
    View mView;
    RestaurantProfile mRestaurantProfile;
    int mHour, mDate;
    int mNumberOfLike;
    String mListOfParticipant;
    boolean mToCreate;

    public FileRestaurantFragment(){}

    private OnClicChoiceRestaurant mCallback;

    public interface OnClicChoiceRestaurant{
        void onResultChoiceTransmission(View view, String name, String adress, String placeId, int hour, int date);
        void onResultLikeTransmission(View view, String listOfParticipant, String placeId, String decision, int like, boolean toCreate);
        void webSiteVisiting(View view, String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_restaurant, container, false);
        this.mRestaurantName = mView.findViewById(R.id.restaurant_name);
        this.mRestaurantLocalisation = mView.findViewById(R.id.restaurant_localisation);
        this.mRestaurantImage = mView.findViewById(R.id.restaurant_photo);
        ButterKnife.bind(this, mView);
        mButtonChoiceRestaurant.setEnabled(false);
        return mView;
    }

    public void setRestaurantProfileInformation(RestaurantProfile restaurantProfile){
        mRestaurantProfile = restaurantProfile;
        mRestaurantName.setText(mRestaurantProfile.getName());
        mRestaurantLocalisation.setText(mRestaurantProfile.getAdress());
        mPhoneNumber = mRestaurantProfile.getPhoneNumber();
        if (mPhoneNumber != null) mPhoneNumber = mPhoneNumber.replace(" ","");
        mWebSite = mRestaurantProfile.getWebSite();
        if (mRestaurantProfile.getPhoto() != null) {
            if (!mRestaurantProfile.getPhoto().equals(BLANK_ANSWER)) {
                Glide.with(mView)
                        .load(mRestaurantProfile.getPhoto())
                        .apply(RequestOptions.centerCropTransform())
                        .into(mRestaurantImage);
            }
        }
    }

    // get the name of restaurant choice from activity to fragment for button choice Restaurant setting
    public void setRestaurantChoiceForButton(String name){
        if (mRestaurantName.getText().equals(name)) isRestaurantChosen = true;
        else isRestaurantChosen = false;
        setCircleLogoRestaurantChoice(isRestaurantChosen, false);
        mButtonChoiceRestaurant.setEnabled(true);
    }

    // get the state of Like Button from activity to fragment
    public void setLikeButton(boolean like, int numberOfLike, String listOfParticipant, boolean toCreate){
        mNumberOfLike = numberOfLike;
        mListOfParticipant = listOfParticipant;
        mToCreate = toCreate;
        if (like) mLikeTextViewButton.setText(mView.getContext().getString(R.string.DISLIKE));
        else mLikeTextViewButton.setText(mView.getContext().getString(R.string.LIKE));
        if (numberOfLike >= 1) mYellowStarOne.setVisibility(View.VISIBLE);
        else mYellowStarOne.setVisibility(View.INVISIBLE);
        if (numberOfLike >= 5) mYellowStarTwo.setVisibility(View.VISIBLE);
        else mYellowStarTwo.setVisibility(View.INVISIBLE);
        if (numberOfLike >= 10) mYellowStarThree.setVisibility(View.VISIBLE);
        else mYellowStarThree.setVisibility(View.INVISIBLE);
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
            if (isAction) mCallback.onResultChoiceTransmission(mView, mRestaurantProfile.getName(), mRestaurantProfile.getAdress(), mRestaurantProfile.getPlaceId(), mHour, mDate);
        } else {
            mImageViewRestaurantChoiceKO.setVisibility(View.VISIBLE);
            if (isAction) mCallback.onResultChoiceTransmission(mView, BLANK_ANSWER, BLANK_ANSWER, mRestaurantProfile.getPlaceId(), mHour, mDate);
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.CALL)
    public void launchRestaurantCall(){
        if (mPhoneNumber == null) Toast.makeText(getContext(),getString(R.string.callDisabled),Toast.LENGTH_LONG).show();
        else {
            Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhoneNumber));
            startActivity(appel);
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.website)
    public void launchRestaurantWebSite(){
        if (mWebSite == null) Toast.makeText(getContext(), getString(R.string.websiteDisabled), Toast.LENGTH_LONG).show();
        else mCallback.webSiteVisiting(this.mView, mWebSite);
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

    @OnClick(R.id.Like)
    public void registrationLike(){
        mCallback.onResultLikeTransmission(mView, mListOfParticipant, mRestaurantProfile.getPlaceId(), mLikeTextViewButton.getText().toString(), mNumberOfLike, mToCreate);
    }

    // configure recyclerView with workmates participation for the restaurant
    public void configureRecyclerView(List<User> userList){
        IsJoiningAdapter isJoiningAdapter = new IsJoiningAdapter(userList);
        this.mRecyclerView.setAdapter(isJoiningAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        isJoiningAdapter.notifyDataSetChanged();
    }
}
